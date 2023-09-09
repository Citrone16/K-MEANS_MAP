package database;

import java.sql.*;

/**
 * Questa classe gestisce l'accesso al Database.
 */
public class DbAccess {
    String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    final String DBMS = "jdbc:mysql";
    final String SERVER = "localhost";
    final String DATABASE = "MapDB";
    final String PORT = "3306";
    final String USER_ID = "MapUser";
    final String PASSWORD = "map";
    Connection conn;

    /**
     * Inizializza una connessione al Database.
     *
     * @throws DatabaseConnectionException Se la connessione al Database non è riuscita.
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println("Error, class not found: " + e.getMessage());
        }
        try {
            conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE, USER_ID, PASSWORD);
            System.out.println("Connection successful.");
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /**
     * Restituisce il gestore della connessione al Database.
     *
     * @return Gestore della connessione.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione con il Database.
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error occurred while closing the connection:" + e.getMessage());
        }
    }
}
