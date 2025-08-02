package com.tradeserver.tradecapture;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeserver.tradecapture.model.Trade;
import com.tradeserver.tradecapture.service.TradeService;
import com.tradeserver.tradecapture.service.LifeCycleService;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private LifeCycleService lifeCycleService;

    @PostMapping
    @org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
    public ResponseEntity<Trade> captureTrade(@RequestBody Trade trade) {
        System.out.println("Received trade: " + trade);
        Trade capturedTrade = tradeService.captureAndPublishTrade(trade);
        return ResponseEntity.ok(capturedTrade);
    }

    @GetMapping
    @org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
    public ResponseEntity<List<Trade>> getTrade(@RequestParam String clientId) {
               
        // String clientId = org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()
        //     .getAttribute("clientId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST)
        //     != null ? (String) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()
        //     .getAttribute("clientId", org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST) : null;

        java.util.List<Trade> trades = lifeCycleService.getAllTrades(clientId);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Content-Type", "application/json");
        return ResponseEntity.ok().headers(headers).body(trades);
    }
}