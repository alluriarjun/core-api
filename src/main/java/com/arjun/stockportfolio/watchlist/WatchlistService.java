package com.arjun.stockportfolio.watchlist;

import com.arjun.stockportfolio.auth.User;
import com.arjun.stockportfolio.auth.UserRepository;
import com.arjun.stockportfolio.config.ResourceNotFoundException;
import com.arjun.stockportfolio.marketdata.Stock;
import com.arjun.stockportfolio.marketdata.StockRepository;
import com.arjun.stockportfolio.watchlist.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistItemRepository watchlistItemRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            WatchlistItemRepository watchlistItemRepository,
                            UserRepository userRepository,
                            StockRepository stockRepository) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistItemRepository = watchlistItemRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<WatchlistSummaryResponse> listWatchlists(Long userId) {
        requireUser(userId);
        return watchlistRepository.findByUserId(userId).stream()
                .map(WatchlistSummaryResponse::from)
                .toList();
    }

    public WatchlistResponse createWatchlist(Long userId, CreateWatchlistRequest request) {
        User user = requireUser(userId);
        if (watchlistRepository.existsByUserIdAndName(userId, request.name())) {
            throw new IllegalStateException("A watchlist named '" + request.name() + "' already exists");
        }
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setName(request.name());
        return WatchlistResponse.from(watchlistRepository.save(watchlist));
    }

    @Transactional(readOnly = true)
    public WatchlistResponse getWatchlist(Long userId, Long watchlistId) {
        return WatchlistResponse.from(requireWatchlist(userId, watchlistId));
    }

    public WatchlistResponse renameWatchlist(Long userId, Long watchlistId, RenameWatchlistRequest request) {
        Watchlist watchlist = requireWatchlist(userId, watchlistId);
        if (!watchlist.getName().equals(request.name()) &&
                watchlistRepository.existsByUserIdAndName(userId, request.name())) {
            throw new IllegalStateException("A watchlist named '" + request.name() + "' already exists");
        }
        watchlist.setName(request.name());
        return WatchlistResponse.from(watchlistRepository.save(watchlist));
    }

    public void deleteWatchlist(Long userId, Long watchlistId) {
        Watchlist watchlist = requireWatchlist(userId, watchlistId);
        watchlistRepository.delete(watchlist);
    }

    public WatchlistResponse addStock(Long userId, Long watchlistId, AddStockRequest request) {
        Watchlist watchlist = requireWatchlist(userId, watchlistId);
        Stock stock = findOrCreateStock(request.symbol().toUpperCase(), request.resolvedExchange());

        if (watchlistItemRepository.existsByWatchlistIdAndStockId(watchlistId, stock.getId())) {
            throw new IllegalStateException(request.symbol() + " is already in this watchlist");
        }

        WatchlistItem item = new WatchlistItem();
        item.setWatchlist(watchlist);
        item.setStock(stock);
        watchlist.getItems().add(item);

        return WatchlistResponse.from(watchlistRepository.save(watchlist));
    }

    public void removeStock(Long userId, Long watchlistId, Long stockId) {
        requireWatchlist(userId, watchlistId);
        WatchlistItem item = watchlistItemRepository.findByWatchlistIdAndStockId(watchlistId, stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found in this watchlist"));
        watchlistItemRepository.delete(item);
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
    }

    private Watchlist requireWatchlist(Long userId, Long watchlistId) {
        return watchlistRepository.findByIdAndUserId(watchlistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist " + watchlistId + " not found"));
    }

    private Stock findOrCreateStock(String symbol, String exchange) {
        return stockRepository.findBySymbolAndExchange(symbol, exchange)
                .orElseGet(() -> {
                    Stock stock = new Stock();
                    stock.setSymbol(symbol);
                    stock.setExchange(exchange);
                    return stockRepository.save(stock);
                });
    }
}