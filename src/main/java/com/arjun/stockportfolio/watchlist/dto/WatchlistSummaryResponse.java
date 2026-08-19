package com.arjun.stockportfolio.watchlist.dto;

import com.arjun.stockportfolio.watchlist.Watchlist;
import java.time.Instant;

public record WatchlistSummaryResponse(
        Long id,
        String name,
        int stockCount,
        Instant createdAt
) {
    public static WatchlistSummaryResponse from(Watchlist watchlist) {
        return new WatchlistSummaryResponse(
                watchlist.getId(),
                watchlist.getName(),
                watchlist.getItems().size(),
                watchlist.getCreatedAt()
        );
    }
}