package com.luxoft.assignment.manager;

import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Manager {
    CompletableFuture<Boolean> add(Quote quote);
    Elvl get(String isin);
    Collection<Elvl> get();
}
