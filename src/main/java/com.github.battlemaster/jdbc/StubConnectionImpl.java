package com.github.battlemaster.jdbc;

import com.github.battlemaster.jdbc.impl.StubDBImplementation;
import com.github.battlemaster.jdbc.stubs.StubConnection;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class StubConnectionImpl extends StubConnection {

    private StubDBImplementation implementation;

    public StubConnectionImpl(File url) throws SQLException {
        try {
            this.implementation = new StubDBImplementation(url);
        } catch (IOException e) {
            throw new SQLException("Cannot create JDBC connection", e);
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return new StubStatementsImpl(this, this.implementation);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return new StubStatementsImpl(sql, this, this.implementation);
    }

    @Override
    public void close() throws SQLException {
        this.implementation = null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.implementation == null;
    }
}
