# core-api — Stock Portfolio Service (Java / Spring Boot)

Full design doc: @docs/design-doc.md (read this first, every session — covers the whole system, not just this repo)

## What this repo is

This repo is the **Core API Service** + **Background Sync Worker** from the design doc (Section 3.1). It is one of two repos in this project:

- `core-api` (this repo) — Java / Spring Boot. User-facing REST backend.
- `mcp-server-agent` (sibling repo, separate git history) — Python. MCP server + LangGraph agent worker.

**These two repos never share code.** They are independent consumers of the same PostgreSQL/TimescaleDB database. Do not suggest importing code from `mcp-server-agent`, and do not add HTTP calls from this repo to the MCP server or vice versa — see design-doc.md Section 3.3 for why.

## Current Phase

See @docs/roadmap-status.md for what's built, what's in progress, and what's deferred.

## Architecture (this repo's scope)

- Packages: `auth`, `portfolio`, `watchlist`, `marketdata` (design-doc.md Section 3.1)
- Owns these DB tables (writes): `users`, `watchlists`, `watchlist_items`, `portfolios`, `portfolio_holdings`, `portfolio_transactions`, `price_timeseries` (design-doc.md Section 4)
- Does **not** write to `analysis_findings` — that table is owned by the Python repo's MCP write tools
- Real-time quote endpoint (Section 2.5) reads through to Finnhub on every request — never persisted, only short-lived Redis cache (10–15s TTL)
- Daily sync job (`@Scheduled`) writes to `price_timeseries` via Alpha Vantage — see Section 7

## Conventions

<!-- Fill these in as you establish them during Weeks 1-2, e.g.: -->
- Build: `./mvnw clean install`
- Run locally: `./mvnw spring-boot:run`
- Test: `./mvnw test`
- Java version: 21 (LTS)
- Package root: `com.<yourname>.stockportfolio`

## Things to always do

- New external exchange/asset-class support must implement the `DataProvider` interface (design-doc.md Section 5.1) — never hardcode a new provider's calls directly into `marketdata` business logic.
- Any new endpoint touching watchlists/portfolios must account for the multi-named-list model (design-doc.md Section 2.2) — there is no implicit single watchlist/portfolio per user.
- Don't add yfinance or unofficial scrapers as a data source — see design-doc.md Section 14.3.
