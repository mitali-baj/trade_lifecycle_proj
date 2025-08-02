package com.tradeserver.tradeenrichment.repository;

import java.util.Optional;

import com.tradeserver.tradeenrichment.model.Currencies;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrenciesRepository extends JpaRepository<Currencies, String> {
    Optional<Currencies> findByTicker(String ticker);
}
