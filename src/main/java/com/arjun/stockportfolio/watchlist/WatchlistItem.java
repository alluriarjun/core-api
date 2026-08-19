package com.arjun.stockportfolio.watchlist;

import com.arjun.stockportfolio.marketdata.Stock;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "watchlist_items", uniqueConstraints = @UniqueConstraint(columnNames = {"watchlist_id", "stock_id"}))
public class WatchlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "watchlist_id", nullable = false)
    private Watchlist watchlist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false, updatable = false)
    private Instant addedAt;

    @PrePersist
    private void prePersist() {
        addedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Watchlist getWatchlist() { return watchlist; }
    public void setWatchlist(Watchlist watchlist) { this.watchlist = watchlist; }
    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }
    public Instant getAddedAt() { return addedAt; }
}