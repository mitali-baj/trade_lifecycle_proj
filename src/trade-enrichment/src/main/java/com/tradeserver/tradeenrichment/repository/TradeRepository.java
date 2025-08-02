package com.tradeserver.tradeenrichment.repository;

import com.tradeserver.tradeenrichment.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID> {
}
