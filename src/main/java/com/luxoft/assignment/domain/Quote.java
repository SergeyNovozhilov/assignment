package com.luxoft.assignment.domain;

import com.luxoft.assignment.model.QuoteModel;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
public class Quote {
    private UUID id;
    private UUID isin;
    private double bid;
    private double ask;
    private Timestamp date;

    public Quote(QuoteModel quoteModel, UUID isin) {
        this.id = UUID.randomUUID();
        this.isin = isin;
        this.bid = quoteModel.getBid();
        this.ask = quoteModel.getAsk();
        this.date = new Timestamp(new Date().getTime());
    }

    public Quote(UUID id, UUID isin, double bid, double ask, Timestamp date) {
        this.id = id;
        this.isin = isin;
        this.bid = bid;
        this.ask = ask;
        this.date = date;
    }
}
