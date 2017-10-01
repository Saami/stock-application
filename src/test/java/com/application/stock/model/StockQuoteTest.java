package com.application.stock.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

/**
 * Created by sasiddi on 3/22/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockQuoteTest {

    @Test
    public void stockQuoteComparatorTest() {
        Date today = new Date();
        StockQuote quote1 = new StockQuote(today, 123d);
        StockQuote quote2 = new StockQuote(new Date(today.getTime() -1), 123d);
        StockQuote quote3 = new StockQuote(new Date(today.getTime() + 1), 123d);
        StockQuote quote4 = new StockQuote(today, 123d);

        assertEquals(quote1.compareTo(quote2), 1);
        assertEquals(quote1.compareTo(quote3), -1);
        assertEquals(quote1.compareTo(quote4), 0);
    }
}
