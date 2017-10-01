package com.application.stock.model;

import java.util.List;

/**
 * Created by sasiddi on 3/19/17.
 */
public class HistoricalDataResponse {

    private String stockTicker;
    private List<StockQuote> prices;

    public HistoricalDataResponse(String stockTicker, List<StockQuote> prices) {
        this.stockTicker = stockTicker;
        this.prices = prices;
    }

    public String getStockTicker() {
        return stockTicker;
    }

    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }

    public List<StockQuote> getPrices() {
        return prices;
    }

    public void setPrices(List<StockQuote> prices) {
        this.prices = prices;
    }
}
