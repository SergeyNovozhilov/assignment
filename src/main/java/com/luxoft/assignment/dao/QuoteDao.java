package com.luxoft.assignment.dao;

import com.luxoft.assignment.model.Quote;

import java.util.List;

public interface QuoteDao {
    void add (Quote quote);
    List<Quote> get(String isin);
    List<Quote> get();
}
