package com.tradeserver.tradevalidationsettlement.service;

import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tradeserver.tradevalidationsettlement.model.Trade;
import com.tradeserver.tradevalidationsettlement.repository.ClientRepository;
import com.tradeserver.tradevalidationsettlement.repository.InstrumentRepository;
import com.tradeserver.tradevalidationsettlement.repository.RatingRepository;
import com.tradeserver.tradevalidationsettlement.repository.CurrenciesRepository;
import com.tradeserver.tradevalidationsettlement.repository.TradeRepository;

@Service
public class TradeValidationSettlementService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private KafkaTemplate<String, Trade> kafkaTemplate;

    @KafkaListener(topics = "trade_enrichment", groupId = "trade-validation-settlement-group")
    public void validSettlementAndSaveTrade(Trade trade) {
        System.out.println("Received trade for validation and settlement: " + trade);
        Trade validatedTrade = new Trade();
        validatedTrade.setTradeId(trade.getTradeId());
        validatedTrade.setTicker(trade.getTicker());
        validatedTrade.setQuantity(trade.getQuantity());
        validatedTrade.setPrice(trade.getPrice());
        validatedTrade.setBuySell(trade.getBuySell());
        validatedTrade.setClientId(trade.getClientId());
        validatedTrade.setInstrumentName(trade.getInstrumentName());
        validatedTrade.setInstrumentType(trade.getInstrumentType());
        validatedTrade.setClientName(trade.getClientName());
        validatedTrade.setRatingValue(trade.getRatingValue());
        validatedTrade.setRatingDescription(trade.getRatingDescription());
        validatedTrade.setRatingAgency(trade.getRatingAgency());
        validatedTrade.setCurrencyCode(trade.getCurrencyCode());
        validatedTrade.setCurrencyName(trade.getCurrencyName());    

        
        // enrichedTrade.setLogId(null); // Let the database auto-generate the logId
        validatedTrade.setCreatedAt(java.time.OffsetDateTime.now());
        validatedTrade.setUpdatedAt(java.time.OffsetDateTime.now());
        
        BigDecimal amount = trade.getPrice().multiply(trade.getQuantity());
        String valid_status = "VALIDATED";
        if ("buy".equalsIgnoreCase(trade.getBuySell())) {
            if (amount.compareTo(BigDecimal.valueOf(100000)) <= 0) {
                System.out.println("Buy amount " + amount + " is within limit.");
                validatedTrade.setValidationStatus("Buy amount " + amount + " within limit");
            } else {
                validatedTrade.setValidationStatus("Buy amount " + amount + " exceeds limit");
                valid_status = "INVALID";
            }
        } else if ("sell".equalsIgnoreCase(trade.getBuySell())) {
            BigDecimal totalBuyQuantity = tradeRepository.sumQuantityByConditions(trade.getTicker(), "SETTLED", "BUY", trade.getClientId());
            if (totalBuyQuantity == null) {
                totalBuyQuantity = BigDecimal.ZERO;
            }

            BigDecimal totalSellQuantity = tradeRepository.sumQuantityByConditions(trade.getTicker(), "SETTLED", "SELL", trade.getClientId());
            if (totalSellQuantity == null) {
                totalSellQuantity = BigDecimal.ZERO;
            }
            totalSellQuantity = totalSellQuantity.add(trade.getQuantity());
            if (totalBuyQuantity.subtract(totalSellQuantity).compareTo(BigDecimal.ZERO) >= 0) {
                validatedTrade.setValidationStatus("Valid sell");
            } else {
                validatedTrade.setValidationStatus("Invalid sell: No sufficient buy quantity");
                valid_status = "INVALID";
            }
        }

        if ("INVALID".equals(valid_status)) {
            validatedTrade.setSettlementStatus("FAILED");
        } else {
            validatedTrade.setSettlementStatus("SETTLED");
        }

        validatedTrade.setLifecycleState("VALIDATION & SETTLEMENT");
        System.out.println("Validated trade: " + validatedTrade);
        tradeRepository.save(validatedTrade);
        System.out.println("Saving validated trade to repository: " + validatedTrade);
        kafkaTemplate.send("trade_validation", validatedTrade);
        System.out.println("Trade validation completed and sent to Kafka topic.");
    }
}