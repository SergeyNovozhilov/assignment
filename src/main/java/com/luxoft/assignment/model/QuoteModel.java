package com.luxoft.assignment.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuoteModel {
    private String isin;
    private Double bid;
    private Double ask;

    public QuoteModel(String isin, Double bid, Double ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
    }

    @Override
    public String toString() {
        return "QuoteModel{" +
                "isin='" + isin + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
