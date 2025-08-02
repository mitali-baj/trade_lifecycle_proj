package com.tradeserver.tradecapture.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.tradeserver.tradecapture.model.Trade;
import com.tradeserver.tradecapture.repository.TradeRepository;

@Service
public class TradeService {

    private static final String TOPIC_NAME = "trade-capture-events";

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private KafkaTemplate<String, Trade> kafkaTemplate;

    public Trade captureAndPublishTrade(Trade trade) {
        trade.setTradeId(UUID.randomUUID());
        trade.setLifecycleState("CAPTURED");
        trade.setCreatedAt(OffsetDateTime.now());
        trade.setUpdatedAt(OffsetDateTime.now());

        // Save to database
        tradeRepository.save(trade);

        // Publish to Kafka
        System.out.println("Sending trade to topic "+TOPIC_NAME);
        kafkaTemplate.send(TOPIC_NAME, trade.getTradeId().toString(), trade);
        System.out.println("Successfully Sent trade to topic "+TOPIC_NAME);

        return trade;
    }
}