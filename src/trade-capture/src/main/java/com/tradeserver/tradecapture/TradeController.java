package com.tradeserver.tradecapture;

import com.tradeserver.tradecapture.model.Trade;
import com.tradeserver.tradecapture.model.TradeDTO;
import com.tradeserver.tradecapture.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping
    public ResponseEntity<Trade> captureTrade(@RequestBody TradeDTO tradeDTO) {
        Trade capturedTrade = tradeService.captureAndPublishTrade(tradeDTO);
        return ResponseEntity.ok(capturedTrade);
    }
}