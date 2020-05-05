package com.luxoft.assignment.manager;

import com.luxoft.assignment.dao.ElvlDao;
import com.luxoft.assignment.dao.QuoteDao;
import com.luxoft.assignment.domain.Elvl;
import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.model.ElvlModel;
import com.luxoft.assignment.model.QuoteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.luxoft.assignment.cache.CacheConfig.ELVL_CACHE;

@Service
public class ManagerImpl implements Manager{
    private static final int ISIN_LENGTH = 12;

    private static final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

    private QuoteDao quoteDao;
    private ElvlDao elvlDao;
    private CacheManager cacheManager;

    public ManagerImpl(QuoteDao quoteDao, ElvlDao elvlDao, CacheManager cacheManager) {
        this.quoteDao = quoteDao;
        this.elvlDao = elvlDao;
        this.cacheManager = cacheManager;
    }

    @Override
    public CompletableFuture<List<ElvlModel>> get() {
        LOGGER.info("Request to get a list of elvl's");
        final List<ElvlModel> elvlModels = elvlDao.get();
        return CompletableFuture.completedFuture(elvlModels);
    }

    @Override
    @Transactional(readOnly = true)
    @Async
    public CompletableFuture<ElvlModel> get(String isin) {
        LOGGER.info("Request to get elvlModel by isin");
        final ElvlModel elvlModel = elvlDao.get(isin);
        return CompletableFuture.completedFuture(elvlModel);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> add(QuoteModel quoteModel) {
        return CompletableFuture.completedFuture(processQuote(quoteModel));
    }

    private boolean processQuote(QuoteModel quoteModel) {
        if (!checkQuote(quoteModel)) {
            return false;
        };
        ElvlModel elvlModel;
        Cache cache = cacheManager.getCache(ELVL_CACHE);
        Cache.ValueWrapper wrapper = cache.get(quoteModel.getIsin());
        if(wrapper == null) {
            Isin isin = new Isin(quoteModel.getIsin());
            elvlModel = new ElvlModel();
            elvlModel.setIsin(quoteModel.getIsin());
            setElvl(true, elvlModel, quoteModel);
            elvlDao.add(isin);
            elvlDao.add(new Elvl(isin.getId(), elvlModel.getElvl()));
        } else {
            elvlModel = (ElvlModel)wrapper.get();
            setElvl(false, elvlModel, quoteModel);
            elvlDao.update(elvlModel);
        }
        quoteDao.add(quoteModel);
        cache.put(elvlModel .getIsin(), elvlModel);

        return true;
    }

    private void setElvl(boolean isNew, ElvlModel elvlModel, QuoteModel quoteModel) {
        if (isNew) {
            elvlModel.setElvl(quoteModel.getBid() != null ? quoteModel.getBid() : quoteModel.getAsk());
            return;
        }
        if (quoteModel.getBid() == null) {
            elvlModel.setElvl(quoteModel.getAsk());
            return;
        }
        if (Double.compare(quoteModel.getBid(), elvlModel.getElvl()) > 0) {
            elvlModel.setElvl(quoteModel.getBid());
            return;
        }
        if (Double.compare(quoteModel.getAsk(), elvlModel.getElvl()) < 0) {
            elvlModel.setElvl(quoteModel.getAsk());
        }
    }

    private boolean checkQuote(QuoteModel quoteModel) {
        if (quoteModel.getIsin().length() != ISIN_LENGTH) {
            return false;
        }
        if (quoteModel.getBid() != null && Double.compare(quoteModel.getBid(), quoteModel.getAsk()) >= 0) {
            return false;
        }
        return true;
    }
}
