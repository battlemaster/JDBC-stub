package com.github.battlemaster.jdbc;

import com.github.battlemaster.jdbc.impl.StubDBImplementation;
import com.github.battlemaster.jdbc.stubs.StubPreparedStatement;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StubStatementsImpl extends StubPreparedStatement {

    private String sql;
    private ResultSet rs;
    private Connection connection;
    private StubDBImplementation impl;

    public StubStatementsImpl(String sql, Connection connection, StubDBImplementation impl) {
        this.sql = sql;
        this.connection = connection;
        this.impl = impl;
    }

    public StubStatementsImpl(Connection connection, StubDBImplementation impl) {
        this(null, connection, impl);
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return this.executeQuery(this.sql);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        CSVParser csvRecords = null;
        try {
            csvRecords = this.impl.requestCsvByQuery(sql);
            return (rs = new StubResultSetImpl(csvRecords, this));
        } catch (IOException e) {
            throw new SQLException("Cannot execute query", e);
        }
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        if (rs == null)
            throw new SQLException("Not available");
        return rs;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    @Override
    public void close() throws SQLException {
        if (rs != null)
            rs.close();
    }
}
