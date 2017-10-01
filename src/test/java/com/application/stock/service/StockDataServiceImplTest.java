package com.application.stock.service;

import com.application.stock.model.HistoricalDataResponse;
import com.application.stock.service.impl.StockDataServiceImpl;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by sasiddi on 3/19/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockDataServiceImplTest {

    @Autowired
    private StockDataServiceImpl historicalDataService;

    private static final String JSON_API_RESPONSE = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"date\": \"2017-01-03\",\n" +
            "      \"value\": 116.15\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-04\",\n" +
            "      \"value\": 116.02\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-05\",\n" +
            "      \"value\": 116.61\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-06\",\n" +
            "      \"value\": 117.91\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-09\",\n" +
            "      \"value\": 118.99\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-10\",\n" +
            "      \"value\": 119.11\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-11\",\n" +
            "      \"value\": 119.75\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-12\",\n" +
            "      \"value\": 119.25\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-13\",\n" +
            "      \"value\": 119.04\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-17\",\n" +
            "      \"value\": 120\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-18\",\n" +
            "      \"value\": 119.99\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-19\",\n" +
            "      \"value\": 119.78\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-20\",\n" +
            "      \"value\": 120\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-23\",\n" +
            "      \"value\": 120.08\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-24\",\n" +
            "      \"value\": 119.97\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-25\",\n" +
            "      \"value\": 121.88\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-26\",\n" +
            "      \"value\": 121.94\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-27\",\n" +
            "      \"value\": 121.95\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-30\",\n" +
            "      \"value\": 121.63\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-01-31\",\n" +
            "      \"value\": 121.35\n" +
            "    },\n" +
            "    {\n" +
            "      \"date\": \"2017-02-01\",\n" +
            "      \"value\": 128.75\n" +
            "    }\n" +
            "  ],\n" +
            "  \"identifier\": \"AAPL\",\n" +
            "  \"item\": \"close_price\",\n" +
            "  \"result_count\": 21,\n" +
            "  \"page_size\": 50000,\n" +
            "  \"current_page\": 1,\n" +
            "  \"total_pages\": 1,\n" +
            "  \"api_call_credits\": 1\n" +
            "}";

    @Test
    public void testResponseJsonToHistoricalDataResponse() throws Exception {
        final HistoricalDataResponse response = historicalDataService.responseJsonToHistoricalDataResponse(new JSONObject(JSON_API_RESPONSE));

        assertNotNull(response);
        assertEquals("AAPL", response.getStockTicker());
        assertEquals(21, response.getPrices().size());

    }

    @Test
    public void testResponseJsonToHistoricalDataResponse1() throws Exception {
        final HistoricalDataResponse response = historicalDataService.responseJsonToHistoricalDataResponse(null);

        assertNull(response);

    }
}
