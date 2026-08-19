package com.arjun.stockportfolio.watchlist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {
    boolean existsByWatchlistIdAndStockId(Long watchlistId, Long stockId);
    Optional<WatchlistItem> findByWatchlistIdAndStockId(Long watchlistId, Long stockId);
}