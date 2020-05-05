package com.luxoft.assignment.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Quote {
    private String isin;
    private Double bid;
    private Double ask;
    private Timestamp stmp;

    public Quote() {
    }

    public Quote(String isin, Double bid, Double ask, Timestamp stmp) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
        this.stmp = stmp;
    }

    public Quote(String isin, Double bid, Double ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
        this.stmp = null;
    }
}
