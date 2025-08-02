package com.tradeserver.tradeenrichment.repository;

import java.util.Optional;

import com.tradeserver.tradeenrichment.model.Instrument;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstrumentRepository extends JpaRepository<Instrument, String> {
    Optional<Instrument> findByTicker(String ticker);
}
