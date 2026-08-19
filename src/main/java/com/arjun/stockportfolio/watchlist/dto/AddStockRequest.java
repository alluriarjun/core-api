package com.arjun.stockportfolio.watchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddStockRequest(
        @NotBlank @Size(max = 20) @Pattern(regexp = "[A-Z0-9.\\-]+") String symbol,
        @Size(max = 20) String exchange
) {
    public String resolvedExchange() {
        return (exchange != null && !exchange.isBlank()) ? exchange.toUpperCase() : "US";
    }
}