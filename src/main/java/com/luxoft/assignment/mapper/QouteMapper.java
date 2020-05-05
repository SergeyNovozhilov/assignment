package com.luxoft.assignment.mapper;

import com.luxoft.assignment.model.Quote;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class QouteMapper implements RowMapper<Quote> {
    private static final String ISIN = "isin";
    private static final String BID = "bid";
    private static final String ASK = "ask";
    private static final String STMP = "stmp";

    @Override
    public Quote mapRow(ResultSet resultSet, int i) throws SQLException {
        String isin = resultSet.getString(ISIN);
        double bid = resultSet.getDouble(BID);
        double ask = resultSet.getDouble(ASK);
        Timestamp stmp = resultSet.getTimestamp(STMP);

        return new Quote(isin, bid, ask, stmp);
    }
}
