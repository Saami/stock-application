package com.application.stock.model;

import java.util.Date;

/**
 * Created by sasiddi on 3/19/17.
 */
public class StockQuote implements Comparable<StockQuote>{
    Date date;
    Double price;

    public StockQuote(Date date, Double price) {
        this.date = date;
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int compareTo(StockQuote o) {
        return date.compareTo(o.date);
    }
}
