package com.luxoft.assignment.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Quote {
    private String isin;
    private BigDecimal bid;
    private BigDecimal ask;
    private Timestamp stmp;

    public Quote() {
    }

    public Quote(String isin, BigDecimal bid, BigDecimal ask, Timestamp stmp) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
        this.stmp = stmp;
    }

    public Quote(String isin, BigDecimal bid, BigDecimal ask) {
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
        this.stmp = null;
    }
}
