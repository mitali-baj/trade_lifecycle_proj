package com.tradeserver.tradevalidationsettlement.repository;

import java.util.Optional;

import com.tradeserver.tradevalidationsettlement.model.Currencies;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrenciesRepository extends JpaRepository<Currencies, String> {
    Optional<Currencies> findByTicker(String ticker);
}
