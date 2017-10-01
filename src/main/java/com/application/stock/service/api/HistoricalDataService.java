package com.application.stock.service.api;

import com.application.stock.model.HistoricalDataResponse;

import java.util.Date;

/**
 * Created by sasiddi on 3/19/17.
 */
public interface HistoricalDataService {
    HistoricalDataResponse getStockData(String stockTicker, Date start, Date end);
}
