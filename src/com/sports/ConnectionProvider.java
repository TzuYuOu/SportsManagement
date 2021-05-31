package com.sports;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider {
    private static String serverName = "localhost";
    private static String username = "root";
    private static String dbname = "tySports";
    private static Integer portNumber = 3306;
    private static String password = "";

    public static Connection getConnection(){
        Connection cnx = null;
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setServerName(serverName);
        dataSource.setUser(username);
        dataSource.setDatabaseName(dbname);
        dataSource.setPortNumber(portNumber);
        dataSource.setPassword(password);

        try {
            cnx = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return cnx;

    }
}
