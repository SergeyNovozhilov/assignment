package com.luxoft.assignment.manager;

import com.luxoft.assignment.dao.QuoteDao;
import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;
import com.luxoft.assignment.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;

import static com.luxoft.assignment.cache.CacheConfig.ELVL_CACHE;

@Service
public class ManagerImpl implements Manager{
    private static final int ISIN_LENGTH = 12;

    private static final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private QuoteDao quoteDao;
    private CacheManager cacheManager;

    public ManagerImpl(QuoteDao quoteDao, CacheManager cacheManager) {
        this.quoteDao = quoteDao;
        this.cacheManager = cacheManager;
    }

    @Override
    public Collection<Elvl> get() {
        LOGGER.info("Request to get a list of elvl's");
        ConcurrentMap<String, Elvl> map = (ConcurrentMap<String, Elvl>)cacheManager.getCache(ELVL_CACHE).getNativeCache();
        return map.values();
    }

    @Override
    public Elvl get(String isin) {
        LOGGER.info("Request to get elvl by isin");
        final Elvl elvl = cacheManager.getCache(ELVL_CACHE).get(isin, Elvl.class);
        return elvl;
    }

    @Override
    @Async
    public CompletableFuture<Boolean> add(Quote quote) {
        LOGGER.info("Request to add quote");
        if (!checkQuote(quote)) {
            return CompletableFuture.supplyAsync(() -> false);
        }
        processQuote(quote);
        quoteDao.add(quote);
        return CompletableFuture.supplyAsync(() -> true);
    }

    private synchronized void processQuote(Quote quote) {
        Elvl elvl = cacheManager.getCache(ELVL_CACHE).get(quote.getIsin(), Elvl.class);
        if(elvl == null) {
            elvl = new Elvl();
            elvl.setIsin(quote.getIsin());
            Util.setElvl(true, elvl, quote);
        } else {
            Util.setElvl(false, elvl, quote);
        }
        cacheManager.getCache(ELVL_CACHE).put(elvl.getIsin(), elvl);
    }

    private boolean checkQuote(Quote quote) {
        String message = "";
        if (quote.getIsin().length() != ISIN_LENGTH) {
            message = "Isin length is incorrect.";
        }
        if (quote.getBid() != null && Double.compare(quote.getBid(), quote.getAsk()) >= 0) {
            message.concat(" Bid is greater then ask");
        }
        if (StringUtils.isNotEmpty(message)) {
            LOGGER.error(message);
            return false;
        }
        return true;
    }
}
