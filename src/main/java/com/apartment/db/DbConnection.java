package com.apartment.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static Connection connection;

    static {
        DbProperties props = new DbProperties();
        try {
            Class.forName(props.getDriver());
            connection = DriverManager.getConnection(props.getUrl(), props.getUser(), props.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private DbConnection(){
    }

    public static Connection getConnection() {
        return connection;
    }
}
