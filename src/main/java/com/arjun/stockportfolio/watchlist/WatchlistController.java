package com.arjun.stockportfolio.watchlist;

import com.arjun.stockportfolio.watchlist.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public List<WatchlistSummaryResponse> list(@RequestHeader("X-User-Id") Long userId) {
        return watchlistService.listWatchlists(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WatchlistResponse create(@RequestHeader("X-User-Id") Long userId,
                                    @Valid @RequestBody CreateWatchlistRequest request) {
        return watchlistService.createWatchlist(userId, request);
    }

    @GetMapping("/{id}")
    public WatchlistResponse get(@RequestHeader("X-User-Id") Long userId,
                                 @PathVariable Long id) {
        return watchlistService.getWatchlist(userId, id);
    }

    @PatchMapping("/{id}")
    public WatchlistResponse rename(@RequestHeader("X-User-Id") Long userId,
                                    @PathVariable Long id,
                                    @Valid @RequestBody RenameWatchlistRequest request) {
        return watchlistService.renameWatchlist(userId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader("X-User-Id") Long userId,
                       @PathVariable Long id) {
        watchlistService.deleteWatchlist(userId, id);
    }

    @PostMapping("/{id}/stocks")
    @ResponseStatus(HttpStatus.CREATED)
    public WatchlistResponse addStock(@RequestHeader("X-User-Id") Long userId,
                                      @PathVariable Long id,
                                      @Valid @RequestBody AddStockRequest request) {
        return watchlistService.addStock(userId, id, request);
    }

    @DeleteMapping("/{id}/stocks/{stockId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStock(@RequestHeader("X-User-Id") Long userId,
                            @PathVariable Long id,
                            @PathVariable Long stockId) {
        watchlistService.removeStock(userId, id, stockId);
    }
}