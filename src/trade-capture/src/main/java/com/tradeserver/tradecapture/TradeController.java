package com.tradeserver.tradecapture;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradeserver.tradecapture.model.Trade;
import com.tradeserver.tradecapture.model.TradeDTO;
import com.tradeserver.tradecapture.service.TradeService;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping
    @org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
    public ResponseEntity<Trade> captureTrade(@RequestBody TradeDTO tradeDTO) {
        System.out.println("Received trade DTO: " + tradeDTO);
        Trade capturedTrade = tradeService.captureAndPublishTrade(tradeDTO);
        return ResponseEntity.ok(capturedTrade);
    }

    @GetMapping("/trades")
    @org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
    public ResponseEntity<Trade> getTrade() {
        Trade trade = new Trade();
        trade.setTradeId(UUID.randomUUID());
        trade.setTicker("AAPL");
        trade.setQuantity(new BigDecimal("100"));
        trade.setPrice(new BigDecimal("150.00"));
        trade.setBuySell("BUY");        
        
        return ResponseEntity.ok(trade);
    }
}