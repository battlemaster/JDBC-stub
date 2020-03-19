# JDBC-stub

[![Build Status](https://travis-ci.com/battlemaster/JDBC-stub.svg?branch=master)](https://travis-ci.com/battlemaster/JDBC-stub)

 Usage:
 JDBC driver should be added as dependency in required project.
 Connection line is like:
 jdbc:stub:/path/to/file
 where path to file is local path to your config.
 Config is a text file with key value. Each pair starts with new line and separated by equals(=)

 Key is link to sql file and value is link to csv file with results.

 Please see example in tests.
