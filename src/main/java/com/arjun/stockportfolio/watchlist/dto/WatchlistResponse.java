package com.arjun.stockportfolio.watchlist.dto;

import com.arjun.stockportfolio.watchlist.Watchlist;
import java.time.Instant;
import java.util.List;

public record WatchlistResponse(
        Long id,
        String name,
        Instant createdAt,
        List<WatchlistItemResponse> stocks
) {
    public static WatchlistResponse from(Watchlist watchlist) {
        List<WatchlistItemResponse> stocks = watchlist.getItems().stream()
                .map(WatchlistItemResponse::from)
                .toList();
        return new WatchlistResponse(
                watchlist.getId(),
                watchlist.getName(),
                watchlist.getCreatedAt(),
                stocks
        );
    }
}