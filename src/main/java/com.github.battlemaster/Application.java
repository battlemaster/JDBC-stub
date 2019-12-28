package com.github.battlemaster;

import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        if (args.length < 2) {
            System.out.println("Use args: /path/to/cfg sqlfile");
            System.exit(1);
        }

        Class.forName("com.github.battlemaster.jdbc.StubDriver");
        ResultSet resultSet = null;
        Connection connection = null;
        try {

            connection = DriverManager.getConnection("jdbc:stub:" + args[0]);
            Statement statement = connection.createStatement();

            String sql = new String(Files.readAllBytes(new File(args[1]).toPath()));

            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount;
            System.out.println("Column count: " + (colCount = metaData.getColumnCount()));
            for (int i = 1; i <= colCount; ++i) {
                System.out.printf("Col %s Name %s Type %s", i, metaData.getColumnName(i), metaData.getColumnTypeName(i));
                System.out.println();
            }

            System.out.println();
            System.out.println();
            int rowNum = 1;

            while (resultSet.next()) {
                System.out.println("Row " + rowNum++);
                StringBuilder sb0 = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < colCount; ++i) {
                    sb0.append(resultSet.getString(i + 1)).append("\t");
                    sb1.append(resultSet.getString(metaData.getColumnName(i + 1))).append("\t");
                }
                System.out.println(sb0);
                System.out.println(sb1);
            }


        } finally {
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
        }
    }
}
