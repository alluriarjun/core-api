package com.arjun.stockportfolio.watchlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameWatchlistRequest(
        @NotBlank @Size(max = 255) String name
) {}