package com.application.stock.service;

import com.application.stock.model.HistoricalDataResponse;
import com.application.stock.model.StockQuote;
import com.application.stock.service.api.HistoricalDataService;
import com.application.stock.service.impl.ProfitCalculatorServiceImpl;
import com.application.stock.service.impl.StockDataServiceImpl;
import com.application.stock.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * Created by sasiddi on 3/19/17.
 */
public class ProfitCalculatorServiceImplTest {

    private ProfitCalculatorServiceImpl profitCalculatorService;
    private StockDataServiceImpl stockDataService;

    @Before
    public void initialize() {
        stockDataService = mock(StockDataServiceImpl.class);
        profitCalculatorService = new ProfitCalculatorServiceImpl();
        profitCalculatorService.setStockDataService(stockDataService);
    }

    @Test
    public void calculateOptimalTransactionTest1() { //no optimal trade present

        List<StockQuote> stockQuotes = getNoOptimalTradeData();
        HistoricalDataResponse historicalDataResponse = new HistoricalDataResponse("AAPL", stockQuotes);
        when(stockDataService.getStockData(anyString(), any(), any())).thenReturn(historicalDataResponse);

        List<StockQuote> optimalTrade = profitCalculatorService.getOptimalTransactionStocks("APPL", new Date(), new Date());
        assertEquals(0, optimalTrade.size());

    }

    @Test
    public void calculateOptimalTransactionTest2() { //optimal trade present
        List<StockQuote>  stockQuotes = getOptimalTradeData();
        HistoricalDataResponse historicalDataResponse = new HistoricalDataResponse("AAPL", stockQuotes);
        when(stockDataService.getStockData(anyString(), any(), any())).thenReturn(historicalDataResponse);

        List<StockQuote> optimalTrade = profitCalculatorService.getOptimalTransactionStocks("APPL", new Date(), new Date());
        assertEquals(2, optimalTrade.size());
        assertEquals(optimalTrade.get(0).getDate(), DateUtil.stringToDate("2017-01-02"));
        assertEquals(optimalTrade.get(1).getDate(), DateUtil.stringToDate("2017-01-03"));
    }

    @Test
    public void calculateOptimalTransactionTest3() { //no stock data
        List<StockQuote> stockQuotes = new ArrayList<>();
        HistoricalDataResponse historicalDataResponse = new HistoricalDataResponse("AAPL", stockQuotes);
        when(stockDataService.getStockData(anyString(), any(), any())).thenReturn(historicalDataResponse);
        List<StockQuote> optimalTrade = profitCalculatorService.getOptimalTransactionStocks("APPL", new Date(), new Date());
        assertEquals(0, optimalTrade.size());
    }

    @Test
    public void calculateOptimalTransactionTest4() { //insufficient data
        List<StockQuote> stockQuotes = new ArrayList<>();
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-01"), 5d));
        HistoricalDataResponse historicalDataResponse = new HistoricalDataResponse("AAPL", stockQuotes);
        when(stockDataService.getStockData(anyString(), any(), any())).thenReturn(historicalDataResponse);
        List<StockQuote> optimalTrade = profitCalculatorService.getOptimalTransactionStocks("APPL", new Date(), new Date());
        assertEquals(0, optimalTrade.size());
    }

    //return sample data for when no optimal trade is available
    private List<StockQuote> getNoOptimalTradeData() {
        List<StockQuote> stockQuotes = new ArrayList<>();
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-01"), 5d));
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-02"), 4d));
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-03"), 3d));

        return stockQuotes;
    }

    //return sample data for when an optimal trade is available
    private List<StockQuote> getOptimalTradeData() {
        List<StockQuote> stockQuotes = new ArrayList<>();
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-01"), 5d));
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-02"), 4d));
        stockQuotes.add(new StockQuote(DateUtil.stringToDate("2017-01-03"), 10d));

        return stockQuotes;
    }
}
