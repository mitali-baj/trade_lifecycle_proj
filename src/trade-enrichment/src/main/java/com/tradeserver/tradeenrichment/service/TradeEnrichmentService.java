package com.tradeserver.tradeenrichment.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tradeserver.tradeenrichment.model.Trade;
import com.tradeserver.tradeenrichment.repository.ClientRepository;
import com.tradeserver.tradeenrichment.repository.InstrumentRepository;
import com.tradeserver.tradeenrichment.repository.RatingRepository;
import com.tradeserver.tradeenrichment.repository.CurrenciesRepository;
import com.tradeserver.tradeenrichment.repository.TradeRepository;

@Service
public class TradeEnrichmentService {

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

    @KafkaListener(topics = "trade-capture-events", groupId = "trade-enrichment-group")
    public void enrichAndSaveTrade(Trade trade) {
        System.out.println("Received trade for enrichment: " + trade);
        Trade enrichedTrade = new Trade();
        enrichedTrade.setTradeId(trade.getTradeId());
        enrichedTrade.setTicker(trade.getTicker());
        enrichedTrade.setQuantity(trade.getQuantity());
        enrichedTrade.setPrice(trade.getPrice());
        enrichedTrade.setBuySell(trade.getBuySell());
        enrichedTrade.setClientId(trade.getClientId());
        
        // enrichedTrade.setLogId(null); // Let the database auto-generate the logId
        enrichedTrade.setCreatedAt(java.time.OffsetDateTime.now());
        enrichedTrade.setUpdatedAt(java.time.OffsetDateTime.now());
        System.out.println(">>> Enriching trade: " + trade.getTradeId());
        instrumentRepository.findByTicker(trade.getTicker()).ifPresent(instrument -> {
            //System.out.println("*************** Found instrument: " + instrument.getInstrumentId());
            enrichedTrade.setInstrumentName(instrument.getInstrumentName());
            enrichedTrade.setInstrumentType(instrument.getInstrumentType());
        });
        //System.out.println("Instrument ID set: " + trade.getInstrumentId());

        clientRepository.findByClientId(trade.getClientId()).ifPresent(client -> {
            enrichedTrade.setClientName(client.getClientName());
        });
        //System.out.println("Client ID set: " + trade.getClientId());
        ratingRepository.findByTicker(trade.getTicker()).ifPresent(rating -> {
            enrichedTrade.setRatingValue(rating.getRatingValue());
            enrichedTrade.setRatingDescription(rating.getRatingDescription());
            enrichedTrade.setRatingAgency(rating.getRatingAgency());
        });
       // System.out.println("Rating ID set: " + trade.getRatingId());
        currenciesRepository.findByTicker(trade.getTicker()).ifPresent(currencies -> {
            enrichedTrade.setCurrencyCode(currencies.getCurrencyCode());
            enrichedTrade.setCurrencyName(currencies.getCurrencyName());
        });
        enrichedTrade.setLifecycleState("ENRICHED");
        System.out.println("Enriched trade: " + enrichedTrade);
        tradeRepository.save(enrichedTrade);
        System.out.println("Saving enriched trade to repository: " + enrichedTrade);
        kafkaTemplate.send("trade_enrichment", enrichedTrade);
        System.out.println("Trade enrichment completed and sent to Kafka topic.");
    }
}