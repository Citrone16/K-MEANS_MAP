package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Questa classe gestisce lo schema di una tabella nel Database relazionale.
 */
public class TableSchema {
    DbAccess db;
    private final List<Column> tableSchema = new ArrayList<>();
    /**
     * Questa classe gestisce una generica colonna della tabella.
     */
    public static class Column {
        private final String name;
        private final String type;

        /**
         * Costruttore che assegna i valori passati come parametro agli attributi della classe.
         *
         * @param name Nome della colonna.
         * @param type Tipo della colonna.
         */
        Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Restituisce il nome della colonna.
         *
         * @return Nome della colonna.
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Verifica se il tipo della colonna è numerico.
         *
         * @return True se la colonna è numerica, False altrimenti.
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        /**
         * Restituisce una rappresentazione testuale dell'oggetto Column.
         *
         * @return Stringa che contiene la rappresentazione di Column.
         */
        public String toString() {
            return name + ":" + type;
        }
    }

    /**
     * Gestisce il mapping Java-SQL.
     *
     * @param db        Gestore della connessione al database.
     * @param tableName Nome della tabella nel database.
     * @throws SQLException Se viene lanciata un'eccezione SQL.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        this.db = db;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
        // http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        Connection con = db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {

            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(
                        new Column(res.getString("COLUMN_NAME"), mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));

        }
        res.close();
    }

    /**
     * Restituisce il numero di colonne della tabella.
     *
     * @return Numero di colonne della tabella.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna indicata dall'indice.
     *
     * @param index Indice della colonna da restituire.
     * @return Colonna indicata dall'indice.
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }

}
