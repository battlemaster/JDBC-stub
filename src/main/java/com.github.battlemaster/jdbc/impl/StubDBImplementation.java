package com.github.battlemaster.jdbc.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubDBImplementation {

    /**
     * Config.
     * Key = Loaded SQL Query
     * Value = File to referenced csv with data
     */
    private Map<String, File> config;
    private static final char CFG_SEPARATOR = '=';

    public StubDBImplementation(File configFileName) throws IOException {
        this.readConfig(configFileName);
    }

    private void readConfig(File configFile) throws IOException {
        List<String> lines = Files.readAllLines(configFile.toPath());
        this.config = new HashMap<>(lines.size());

        File parentDir = configFile.getParentFile();

        for (String line : lines) {
            int ind;
            if (line.isEmpty() || (ind = line.indexOf(CFG_SEPARATOR)) < 0)
                continue;

            File sqlQueryFile = this.getFile(line.substring(0, ind), parentDir);
            File csvQueryFile = this.getFile(line.substring(ind + 1), parentDir);

            String sqlQuery = new String(Files.readAllBytes(sqlQueryFile.toPath()));
            this.config.put(sqlQuery, csvQueryFile);
        }
    }

    public CSVParser requestCsvByQuery(String requestedQuery) throws IOException {
        File dataFile = this.config.get(requestedQuery);
        if (dataFile == null)
            return null;
        Reader in = new FileReader(dataFile);
        return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
    }

    private File getFile(String str, File parentDir) {
        File tmp = new File(str);
        return tmp.isAbsolute() ? tmp : new File(parentDir, str);
    }

}
