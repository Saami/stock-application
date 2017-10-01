package com.application.stock.service.impl;

import com.application.stock.model.HistoricalDataResponse;
import com.application.stock.model.StockQuote;
import com.application.stock.service.api.HistoricalDataService;
import com.application.stock.util.DateUtil;
import com.application.stock.util.DoorDashConstants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by sasiddi on 3/22/17.
 */
@Component
@ConfigurationProperties("application.properties")
public class BitCoinDataServiceImpl implements HistoricalDataService {
    private static final Logger LOG = LoggerFactory.getLogger(BitCoinDataServiceImpl.class);

    @Value("${api.bitcoin.url}")
    String URL;
    private static final String PARAMS = "?start=%s&end=%s&currency=usd";

    @Override
    public HistoricalDataResponse getStockData(String stockTicker, Date start, Date end) {
        if (StringUtils.isEmpty(stockTicker) || start == null || end == null) {
            throw new IllegalArgumentException("stockTicker, start and end dates are mandatory parameters");
        }

        final String urlWithParams = String.format(URL+PARAMS, DoorDashConstants.API_DATE_PARAM_FORMATTER.format(start), DoorDashConstants.API_DATE_PARAM_FORMATTER.format(end));
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {

            httpClient = HttpClientBuilder.create().build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/json");

            LOG.debug("Hitting Bitcoin historical data api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("bitcoin Historical Data api Request Code", responseStatus);
                LOG.error("bitcoin Historical Data Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Bitcoin Historical Data api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Bitcoin historical data api call successful. Status Code: " + responseStatus);

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            return responseJsonToHistoricalDataResponse(new JSONObject(result.toString()));

        } catch (Exception e) {
            LOG.error("Error getting from Bitcoin Historical Data: " + urlWithParams, e);
            return null;
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public HistoricalDataResponse responseJsonToHistoricalDataResponse(JSONObject responseJson) {
        if (responseJson == null) {
            return null;
        }

        List<StockQuote> stockQuotes = new ArrayList<>();
        final JSONObject prices = responseJson.optJSONObject("bpi");
        final String ticker = DoorDashConstants.BITCOIN_TICKER;

        if (prices != null) {
            Iterator<String> keys = prices.keys();
            while(keys.hasNext()) {
                final String key = (String)keys.next();
                final Date date = DateUtil.stringToDate(key);
                final Double price = prices.optDouble(key);

                final StockQuote quote = new StockQuote(date, price);
                stockQuotes.add(quote);
            }
        }

        StockQuote [] quotesArray = stockQuotes.toArray(new StockQuote[stockQuotes.size()]);
        Arrays.sort(quotesArray);
        stockQuotes = Arrays.asList(quotesArray);

        return new HistoricalDataResponse(ticker, stockQuotes);
    }
}
