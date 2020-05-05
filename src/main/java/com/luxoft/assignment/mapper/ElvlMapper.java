package com.luxoft.assignment.mapper;
import com.luxoft.assignment.model.ElvlModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ElvlMapper implements RowMapper<ElvlModel> {
    private static final String ISIN = "isin";
    private static final String ELVL = "elvl";

    @Override
    public ElvlModel mapRow(ResultSet resultSet, int i) throws SQLException {
        String isin = resultSet.getString(ISIN);
        double elvl = resultSet.getDouble(ELVL);

        return new ElvlModel(isin, elvl);
    }
}
