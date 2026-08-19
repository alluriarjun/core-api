package com.arjun.stockportfolio.watchlist.dto;

import com.arjun.stockportfolio.watchlist.WatchlistItem;
import java.time.Instant;

public record WatchlistItemResponse(
        Long id,
        Long stockId,
        String symbol,
        String exchange,
        String name,
        Instant addedAt
) {
    public static WatchlistItemResponse from(WatchlistItem item) {
        return new WatchlistItemResponse(
                item.getId(),
                item.getStock().getId(),
                item.getStock().getSymbol(),
                item.getStock().getExchange(),
                item.getStock().getName(),
                item.getAddedAt()
        );
    }
}