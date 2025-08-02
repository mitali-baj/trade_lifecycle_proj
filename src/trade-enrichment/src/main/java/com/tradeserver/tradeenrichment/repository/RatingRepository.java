package com.tradeserver.tradeenrichment.repository;

import com.tradeserver.tradeenrichment.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, String> {

    Optional<Rating> findByTicker(String ticker);
}
