package com.luxoft.assignment.manager;

import com.luxoft.assignment.model.ElvlModel;
import com.luxoft.assignment.model.QuoteModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Manager {
    CompletableFuture<Boolean> add(QuoteModel quoteModel);
    CompletableFuture<ElvlModel> get(String isin);
    CompletableFuture<List<ElvlModel>> get();
}
