package com.arjun.stockportfolio.marketdata;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbolAndExchange(String symbol, String exchange);
}