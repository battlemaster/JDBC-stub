package com.github.battlemaster.jdbc;


import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.sql.*;

public class SimpleTest {

    @Test
    public void test1() throws ClassNotFoundException, SQLException {
        Class.forName("com.github.battlemaster.jdbc.StubDriver");
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            URL resource = SimpleTest.class.getClassLoader().getResource("config.txt");
            if (resource == null)
                Assert.fail("Not available resource");
            String file = resource.getFile();
            connection = DriverManager.getConnection("jdbc:stub:" + file);
            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from ABC");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colCount = metaData.getColumnCount();
            Assert.assertEquals(colCount, 3);
            if (colCount == 3) {
                Assert.assertEquals(metaData.getColumnName(1), "abc");
                Assert.assertEquals(metaData.getColumnName(2), "BBB");
                Assert.assertEquals(metaData.getColumnName(3), "CCC");
            }
            int rowNum = 1;

            Assert.assertTrue(resultSet.next());
            Assert.assertEquals(resultSet.getString(1), "1");
            Assert.assertEquals(resultSet.getString(2), "2");
            Assert.assertEquals(resultSet.getString(3), "3");
            Assert.assertEquals(resultSet.getString(metaData.getColumnName(1)), "1");
            Assert.assertEquals(resultSet.getString(metaData.getColumnName(2)), "2");
            Assert.assertEquals(resultSet.getString(metaData.getColumnName(3)), "3");


            while (resultSet.next()) {
                ++rowNum;
            }

            Assert.assertEquals(rowNum, 3);
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
        }
    }

}
