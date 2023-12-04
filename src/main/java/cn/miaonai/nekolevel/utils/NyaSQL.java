package cn.miaonai.nekolevel.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NyaSQL{
    private static Connection connection;
    public NyaSQL(String host, int port, String database, String username, String password) {
        connectToDatabase(host, port, database, username, password);
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Log.warning("Error closing the database connection!");
        }
    }

    private void connectToDatabase(String host, int port, String database, String username, String password) {
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            Log.warning("Failed to connect to the database!");
        }
    }
}