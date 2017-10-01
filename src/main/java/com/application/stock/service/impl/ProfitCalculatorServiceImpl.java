package com.application.stock.service.impl;

import com.application.stock.model.HistoricalDataResponse;
import com.application.stock.model.StockQuote;
import com.application.stock.service.api.HistoricalDataService;
import com.application.stock.service.api.ProfitCalculatorService;
import com.application.stock.util.DoorDashConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sasiddi on 3/19/17.
 */
@Component
public class ProfitCalculatorServiceImpl implements ProfitCalculatorService {

//    @Autowired
//    private HistoricalDataService historicalDataService;

    @Autowired
    private StockDataServiceImpl stockDataService;

    @Autowired
    private BitCoinDataServiceImpl bitCoinDataService;

    public void setStockDataService(StockDataServiceImpl stockDataService) {
        this.stockDataService = stockDataService;
    }

    public void setBitCoinDataService(BitCoinDataServiceImpl bitCoinDataService) {
        this.bitCoinDataService = bitCoinDataService;
    }

    @Override
    public List<StockQuote> getOptimalTransactionStocks(String stockTicker, Date startDate, Date endDate) {
        if (stockTicker == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("stockTicker, startDate, and endDate are required parameters");
        }
        HistoricalDataResponse historicalDataResponse = null;

        if (stockTicker.equals(DoorDashConstants.BITCOIN_TICKER)) {
            historicalDataResponse = bitCoinDataService.getStockData(stockTicker, startDate, endDate);
        } else {
            historicalDataResponse = stockDataService.getStockData(stockTicker, startDate, endDate);
        }

        return calculateOptimalTransaction(historicalDataResponse.getPrices());
    }

    private List<StockQuote> calculateOptimalTransaction(List<StockQuote> stocks) {
        final List<StockQuote> optimalTransaction = new ArrayList<>();
        if (stocks == null || stocks.size() <= 1) {
            return optimalTransaction;
        }

        StockQuote lowestPriceStock = stocks.get(0);
        StockQuote buy = stocks.get(0);
        StockQuote sell = stocks.get(0);
        Double maxProfitSorFar = 0.0;

        for (int i=1; i < stocks.size(); i++) {
            final StockQuote stockToday = stocks.get(i);
            final Double priceToday = stockToday.getPrice();
            final Double profitToday = priceToday - lowestPriceStock.getPrice();
            if (maxProfitSorFar < profitToday) {
                maxProfitSorFar = profitToday;
                buy = lowestPriceStock;
                sell = stockToday;
            }

            lowestPriceStock = lowestPriceStock.getPrice() > priceToday ? stockToday : lowestPriceStock;
        }

        if (maxProfitSorFar > 0.0) {
            optimalTransaction.add(buy);
            optimalTransaction.add(sell);
        }

        return optimalTransaction;
    }
}
