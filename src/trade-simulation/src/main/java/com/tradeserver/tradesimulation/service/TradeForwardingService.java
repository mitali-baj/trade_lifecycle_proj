// package com.tradeserver.tradesimulation.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
// import org.springframework.stereotype.Service;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.tradeserver.tradesimulation.model.Simulation;


// @Service
// public class TradeForwardingService {

//     private final SimpMessagingTemplate messagingTemplate;
//     private final ObjectMapper objectMapper;

//     @Autowired
//     public TradeForwardingService(SimpMessagingTemplate messagingTemplate) {
//         this.messagingTemplate = messagingTemplate;
//         this.objectMapper = new ObjectMapper();
//     }

//     public void sendTradesFromJsonFile() throws InterruptedException, JsonProcessingException {
//        for (int i = 0; i < 9296; i++) {
//             // Simulate trade data
//             Simulation simulation = new Simulation();
//             simulation.setTicker(ticker);
//             simulation.setTitle(title);
//             simulation.setSentiment(sentiment);
//             // Convert to JSON and send
//             String tradeJson = objectMapper.writeValueAsString(simulation);
//             messagingTemplate.convertAndSend("/topic/simulation", tradeJson);
//                 //messagingTemplate.convertAndSend("/topic/simulation", new Date() + " - Simulated trade #" + i);
//             Thread.sleep(1000);
//             }
            
//     }
// }

package com.tradeserver.tradesimulation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeserver.tradesimulation.model.Simulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;


@Service
public class TradeForwardingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public TradeForwardingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    
    @EventListener(ApplicationReadyEvent.class)
    public void startSendingNewsAfterStartup() {
        new Thread(() -> {
            try {
                sendTradesFromJsonFile(); // Run your existing method in a background thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException ex) {
            }
        }).start();
    }

    public void sendTradesFromJsonFile() throws InterruptedException, JsonProcessingException {
        try {
            // 1. Load JSON from classpath resource
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("extracted_news.json");

            if (inputStream == null) {
                throw new RuntimeException("File not found in classpath: extracted_news.json");
            }

            // 2. Parse the JSON into List<Simulation>
            List<Simulation> simulations = objectMapper.readValue(inputStream, new TypeReference<List<Simulation>>() {});

            // 3. Send each simulation
            for (Simulation simulation : simulations) {
                System.out.println("Processing simulation: " + simulation);
                String tradeJson = objectMapper.writeValueAsString(simulation);
                System.out.println("Sending trade: " + tradeJson);
                messagingTemplate.convertAndSend("/topic/simulation", tradeJson);
                Thread.sleep(1000); // Optional delay
            }

        } catch (IOException e) {
            e.printStackTrace();  // Print the root cause
            throw new RuntimeException("Failed to read or parse extracted_news.json", e);
        }
    }
}
