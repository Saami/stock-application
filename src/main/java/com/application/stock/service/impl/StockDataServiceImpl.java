package com.application.stock.service.impl;

import com.application.stock.model.HistoricalDataResponse;
import com.application.stock.model.StockQuote;
import com.application.stock.service.api.HistoricalDataService;
import com.application.stock.util.DateUtil;
import com.application.stock.util.DoorDashConstants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sasiddi on 3/19/17.
 */
@Component
@ConfigurationProperties("application.properties")
public class StockDataServiceImpl implements HistoricalDataService {
    private static final Logger LOG = LoggerFactory.getLogger(StockDataServiceImpl.class);

    @Value("${api.intrinio.username}")
    String username;

    @Value("${api.intrinio.password}")
    String password;

    @Value("${api.intrinio.url}")
    String URL;

    private static final String PARAMS = "?identifier=%s&item=close_price&start_date=%s&end_date=%s&sort_order=asc";

    @Override
    public HistoricalDataResponse getStockData(String stockTicker, Date start, Date end) {
        if (StringUtils.isEmpty(stockTicker) || start == null || end == null) {
            throw new IllegalArgumentException("stockTicker, start and end dates are mandatory parameters");
        }

        final String urlWithParams = String.format(URL+PARAMS, stockTicker, DoorDashConstants.API_DATE_PARAM_FORMATTER.format(start), DoorDashConstants.API_DATE_PARAM_FORMATTER.format(end));
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;

        try {
            final CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(AuthScope.ANY, credentials);

            httpClient = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .build();
            httpGet = new HttpGet(urlWithParams);
            httpGet.addHeader("content-type", "application/json");

            LOG.debug("Hitting intrinio historical data api: " + urlWithParams);
            response = httpClient.execute(httpGet);

            final int responseStatus = response.getStatusLine().getStatusCode();
            if (responseStatus < 200 || responseStatus > 300) {
                LOG.error("Intrinio Historical Data api Request Code", responseStatus);
                LOG.error("Intrinio Historical Data Request Message", response.getStatusLine());
                throw new Exception(String.format("Call to Intrinio Historical Data api did not return 200. Status:[%s]", responseStatus));
            }
            LOG.debug("Intrinio historical data api call successful. Status Code: " + responseStatus);

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
            LOG.error("Error getting from Intrinio Historical Data: " + urlWithParams, e);
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

        final List<StockQuote> stockQuotes = new ArrayList<>();
        final String ticker = responseJson.optString("identifier");
        final JSONArray prices = responseJson.optJSONArray("data");
        if (prices != null) {
            for (int i=0; i < prices.length(); i++) {
                final JSONObject stockQuoteJson = prices.optJSONObject(i);
                if (stockQuoteJson != null) {
                    final Date date = DateUtil.stringToDate(stockQuoteJson.optString("date"));
                    final Double price = stockQuoteJson.optDouble("value");
                    if (date != null) {
                        final StockQuote quote = new StockQuote(date, price);
                        stockQuotes.add(quote);
                    }
                }

            }
        }
        return new HistoricalDataResponse(ticker, stockQuotes);
    }
}
