package com.tradeserver.tradesimulation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeserver.tradesimulation.model.PricesSimulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


@Service
public class PricesForwardingService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    // @PostConstruct
    // public void initOnStartup() {
    // }

    @Autowired
    public PricesForwardingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = new ObjectMapper();
    }

    
    @EventListener(ApplicationReadyEvent.class)
    public void startSendingPricesAfterStartup() {
        new Thread(() -> {
            try {
                sendPricesFromCsvFile(); // Run your existing method in a background thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void sendPricesFromCsvFile() throws InterruptedException {
        // 1. List of tickers
        List<String> tickers = Arrays.asList("AAPL", "MSFT", "GOOG", "TSLA", "IBM", "UL", "WMT");

        // 2. Map to hold ticker -> list of close prices
        Map<String, List<Double>> tickerCloseMap = new LinkedHashMap<>();

        // 3. Read each CSV and populate close prices
        for (String ticker : tickers) {
            String filename = "simulated_" + ticker + "_live.csv";
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
                if (is == null) throw new RuntimeException("Missing file: " + filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                List<Double> closes = new ArrayList<>();
                String line;
                boolean headerSkipped = false;
                while ((line = reader.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true; // skip header
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;
                    try {
                        double close = Double.parseDouble(parts[4]); // assuming index 4 is "close"
                        closes.add(close);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid close value in " + filename + ": " + line);
                    }
                }
                tickerCloseMap.put(ticker, closes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read file: " + filename, e);
            }
        }

        // 4. Find minimum number of rows among all tickers
        int minRows = tickerCloseMap.values().stream().mapToInt(List::size).min().orElse(0);

        // 5. Round-robin send
        for (int i = 0; i < minRows; i++) {
            for (String ticker : tickers) {
                PricesSimulation pricesSimulation = new PricesSimulation();
                pricesSimulation.setTicker(ticker);
                pricesSimulation.setClose(tickerCloseMap.get(ticker).get(i));

                try {
                    String json = objectMapper.writeValueAsString(pricesSimulation);
                    System.out.println("Sending: " + json);
                    messagingTemplate.convertAndSend("/topic/prices", json);
                    Thread.sleep(1000);
                } catch (JsonProcessingException e) {
                    System.err.println("Failed to serialize simulation for " + ticker + ": " + e.getMessage());
                }
            }
        }
    }
}
