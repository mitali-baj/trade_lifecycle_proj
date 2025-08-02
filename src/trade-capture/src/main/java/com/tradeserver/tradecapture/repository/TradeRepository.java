package com.tradeserver.tradecapture.repository;

import com.tradeserver.tradecapture.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, UUID> {
    // @Query("SELECT SUM(t.quantity) FROM Trades")
    List<Trade> findByClientId(String clientId);
}
