package com.luxoft.assignment.dao;

import com.luxoft.assignment.model.QuoteModel;

import java.util.List;

public interface QuoteDao {
    void add (QuoteModel quote);
    List<QuoteModel> get(String isin);
    List<QuoteModel> get();
    List<String> getIsins ();
}
