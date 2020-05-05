package com.luxoft.assignment.dao;

import com.luxoft.assignment.mapper.QouteMapper;
import com.luxoft.assignment.model.QuoteModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class QuoteDaoImpl implements QuoteDao {

    private NamedParameterJdbcOperations jdbc;

    public QuoteDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void add(QuoteModel quote) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.randomUUID());
        params.put("isin", quote.getIsin());
        params.put("bid", quote.getBid());
        params.put("ask", quote.getAsk());
        params.put("stmp", new Timestamp(new Date().getTime()));

        jdbc.update("insert into QOUTES (id, isin, bid, ask, stmp) " +
                "values (:id, (select id from ISINS where isin = :isin), :bid, :ask, :stmp)", params);
    }

    @Override
    public List<QuoteModel> get() {
        try {
            return jdbc.query("select i.isin, q.bid, q.ask, q.stmp from QOUTES q left join ISINS i on q.isin = i.id",
                    new QouteMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<QuoteModel> get(String isin) {
        Map<String, String> params = Collections.singletonMap("isin", isin);
        try {
            return jdbc.query("select i.isin, q.bid, q.ask, q.stmp " +
                            "from QOUTES q, ISINS i\n" +
                            "where i.isin = :isin\n" +
                            "and q.isin = i.id" +
                            " order by stmp",
                    params, new QouteMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> getIsins () {
        try {
            return jdbc.query("select * from ISINS " , (rs, rowNum) -> {
                return rs.getString("id") + "-" + rs.getString("isin");
            });
        } catch (DataAccessException e) {
            return null;
        }
    }
}
