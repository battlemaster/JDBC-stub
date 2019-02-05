package com.github.battlemaster.jdbc;

import com.github.battlemaster.jdbc.stubs.StubResultSet;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

/**
 * Currently implemented only STRING columns
 */
public class StubResultSetImpl extends StubResultSet {

    private CSVParser csvRecords;
    private Iterator<CSVRecord> csvIt;
    private StubRSMetaDataImpl metaData;
    private CSVRecord currentRecord;
    private Map<String, Integer> header;
    private Statement statement;

    public StubResultSetImpl(CSVParser csvRecords, Statement statement) {
        this.csvRecords = csvRecords;
        this.csvIt = this.csvRecords.iterator();
        this.header = this.csvRecords.getHeaderMap();
        this.metaData = new StubRSMetaDataImpl(this.header);
        this.statement = statement;
    }

    @Override
    public boolean next() throws SQLException {
        if (!csvIt.hasNext()) {
            this.currentRecord = null;
            return false;
        }

        this.currentRecord = csvIt.next();
        return true;
    }

    @Override
    public void close() throws SQLException {
        try {
            this.csvRecords.close();
        } catch (IOException e) {
            throw new SQLException("Error closing Result Set", e);
        }
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        if (!this.metaData.getHeaderViceVersa().containsKey(columnIndex - 1))
            throw new SQLException("Column with index " + columnIndex + " is unavailable");

        if (currentRecord == null)
            throw new SQLException("Record is unavailable.");

        return this.currentRecord.get(columnIndex - 1);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        //findColumn to avoid NPE in case column not exists
        return this.getString(this.findColumn(columnLabel));
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.metaData;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return this.getString(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return this.getString(columnLabel);
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        Integer headerInd = this.header.get(columnLabel);
        if (headerInd == null)
            throw new SQLException("No such column: " + columnLabel);
        return headerInd + 1;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.csvRecords.isClosed();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return this.statement;
    }
}
