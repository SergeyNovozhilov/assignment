package com.luxoft.assignment.dao;

import com.luxoft.assignment.mapper.QouteMapper;
import com.luxoft.assignment.model.Quote;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class QuoteDaoImpl implements QuoteDao {

    private NamedParameterJdbcOperations jdbc;

    public QuoteDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public void add(Quote quote) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.randomUUID());
        params.put("isin", quote.getIsin());
        params.put("bid", quote.getBid());
        params.put("ask", quote.getAsk());
        params.put("stmp", new Timestamp(new Date().getTime()));

        jdbc.update("insert into QUOTES (id, isin, bid, ask, stmp) " +
                "values (:id, :isin, :bid, :ask, :stmp)", params);
    }

    @Override
    public List<Quote> get() {
        return jdbc.query("select * from QUOTES order by isin, stmp",
                    new QouteMapper());
    }

    @Override
    public List<Quote> get(String isin) {
        Map<String, String> params = Collections.singletonMap("isin", isin);
        return jdbc.query("select * from QUOTES \n" +
                            " where isin = :isin\n" +
                            " order by stmp",
                    params, new QouteMapper());

    }
}
