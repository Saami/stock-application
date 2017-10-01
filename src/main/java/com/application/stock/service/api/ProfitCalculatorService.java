package com.application.stock.service.api;

import com.application.stock.model.StockQuote;

import java.util.Date;
import java.util.List;

/**
 * Created by sasiddi on 3/19/17.
 */
public interface ProfitCalculatorService {
    List<StockQuote> getOptimalTransactionStocks(String stockTicker, Date startDate, Date endDate);
}
