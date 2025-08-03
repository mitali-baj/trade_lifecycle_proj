package com.tradeserver.tradesimulation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeserver.tradesimulation.service.PricesForwardingService;
import com.tradeserver.tradesimulation.service.TradeForwardingService;

@RestController
@RequestMapping("/api")
public class TradeController {

    private final TradeForwardingService tradeForwardingService;
    private final PricesForwardingService pricesForwardingService;

    @Autowired
    public TradeController(TradeForwardingService tradeForwardingService, PricesForwardingService pricesForwardingService) {
        this.tradeForwardingService = tradeForwardingService;
        this.pricesForwardingService = pricesForwardingService;
       
    }

    @GetMapping("/start-trades")
    public String startTrades() {
        // Run in a new thread to avoid blocking
        new Thread(() -> {
            try {
                tradeForwardingService.sendTradesFromJsonFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return "Trade simulation started";
    }

    @GetMapping("/start-prices")
    public String startPricesAapl() {
        // Run in a new thread to avoid blocking
        new Thread(() -> {
            try {
                pricesForwardingService.sendPricesFromCsvFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return "Trade simulation started";
    }
}
