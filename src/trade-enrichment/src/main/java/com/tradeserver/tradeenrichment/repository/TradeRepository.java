package com.tradeserver.tradeenrichment.repository;

import com.tradeserver.tradeenrichment.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID> {
    @Query("SELECT SUM(t.quantity) FROM Trade t " +
           "WHERE t.ticker = :ticker AND t.settlementStatus = :settlementStatus " +
           "AND t.buySell = :buySell AND t.clientId = :clientId")
    BigDecimal sumQuantityByConditions(
        @Param("ticker") String ticker,
        @Param("settlementStatus") String settlementStatus,
        @Param("buySell") String buySell,
        @Param("clientId") String clientId
    );
}
