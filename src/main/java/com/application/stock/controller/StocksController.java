package com.application.stock.controller;

import com.application.stock.model.StockQuote;
import com.application.stock.service.api.ProfitCalculatorService;
import com.application.stock.util.DateUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by sasiddi on 3/19/17.
 */
@RestController
public class StocksController {
    @Autowired
    ProfitCalculatorService profitCalculatorService;

    @RequestMapping(path = "/bestTrade/{ticker}", method = RequestMethod.GET)
    String getBestHistoricTrade(@PathVariable String ticker, @RequestParam(value = "startDate", defaultValue = "2017-01-01", required = false) String startDate,
                                @RequestParam(value = "endDate", required = false) String endDate) {

        final Date start = DateUtil.stringToDate(startDate);
        Date end = DateUtil.stringToDate(endDate);
        end = end != null ? end : new Date();
        final JSONArray resultArray = new JSONArray();

        try {
            final List<StockQuote> optimalTrades = profitCalculatorService.getOptimalTransactionStocks(ticker.toUpperCase(),start, end);

            if (optimalTrades != null && optimalTrades.size() == 2) {
                final Date buyDate = optimalTrades.get(0).getDate();
                final Date sellDate = optimalTrades.get(1).getDate();

                resultArray.put(DateUtil.dateToString(buyDate));
                resultArray.put(DateUtil.dateToString(sellDate));
            }

            return resultArray.toString();

        } catch (Exception e) {
            return resultArray.toString();
        }
    }

    private Boolean validateDate(final Date start, final Date end) {
        return start != null && end != null && start.before(end);
    }
}
