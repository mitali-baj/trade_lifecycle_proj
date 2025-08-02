package com.tradeserver.tradecapture.service;

import com.tradeserver.tradecapture.model.Trade;
import com.tradeserver.tradecapture.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LifeCycleService {

    private final TradeRepository tradeRepository;

    @Autowired
    public LifeCycleService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    /**
     * Fetches all trades from the database.
     * The controller will serialize the returned list to JSON.
     */
    public List<Trade> getAllTrades(String clientId) {
        return tradeRepository.findByClientId(clientId);
    }
}