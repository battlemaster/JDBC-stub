package com.github.battlemaster.jdbc;

import java.io.File;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class StubDriver implements Driver {

    private static final String URL_PREF = "jdbc:stub:";

    static {
        try {
            StubDriver driver = new StubDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating driver");
        }
    }


    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!this.isValidURL(url)) {
            // FROM JAVADOC
            //The driver should return "null" if it realizes it is the wrong kind of driver to connect to the given URL.
            return null;
        }

        String extractUrl = url.substring(URL_PREF.length());
        File cfgFile = new File(extractUrl);
        if (!cfgFile.exists()) {
            throw new SQLException("Requested URL does not exist");
        }

        return new StubConnectionImpl(cfgFile);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.isValidURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        //TODO?
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    private boolean isValidURL(String url) {
        return url.startsWith(URL_PREF);
    }
}
