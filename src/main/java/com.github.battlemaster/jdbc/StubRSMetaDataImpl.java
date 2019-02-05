package com.github.battlemaster.jdbc;

import com.github.battlemaster.jdbc.stubs.StubResultSetMetaData;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;


/**
 * Currently implemented only STRING columns
 */
public class StubRSMetaDataImpl extends StubResultSetMetaData {

    private Map<String, Integer> header;
    private Map<Integer, String> headerViceVersa;

    public StubRSMetaDataImpl(Map<String, Integer> header) {
        this.header = header;
        this.headerViceVersa = new HashMap<>();
        this.header.forEach((s, integer) -> this.headerViceVersa.put(integer, s));
    }

    public Map<String, Integer> getHeader() {
        return header;
    }

    public Map<Integer, String> getHeaderViceVersa() {
        return headerViceVersa;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return header.size();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        return this.headerViceVersa.get(column - 1);
    }

    private void checkCol(int column) throws SQLException {
        if (!this.headerViceVersa.containsKey(column - 1))
            throw new SQLException("Wrong column id: " + column);
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        this.checkCol(column);
        return Types.VARCHAR;
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        this.checkCol(column);
        //TODO change to other one?
        return "VARCHAR";
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        this.checkCol(column);
        return String.class.getName();
    }
}
