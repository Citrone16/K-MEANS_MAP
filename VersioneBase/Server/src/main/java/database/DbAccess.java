package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
    String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    final String DBMS = "jdbc:mysql";
    final String SERVER = "localhost";
    final String DATABASE = "MapDB";
    final String PORT = "3306";
    final String USER_ID = "MapUser";
    final String PASSWORD = "map";
    Connection conn;

    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE, USER_ID, PASSWORD);
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws DatabaseConnectionException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }
}
