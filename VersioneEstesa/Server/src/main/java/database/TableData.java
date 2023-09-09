package database;

import database.TableSchema.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Questa classe gestisce le transazione della tabella.
 */
public class TableData {

    private final DbAccess db;

    /**
     * Costruttore che inizializza il gestore della connessione.
     *
     * @param db Gestore della connessione.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Restituisce lo schema della tabella e le sue tuple.
     *
     * @param table Nome della tabella.
     * @return Lista di transazioni della tabella.
     * @throws SQLException      Se viene sollevata un'eccezione SQL.
     * @throws EmptySetException Se il set è vuoto.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        TableSchema tableSchema = new TableSchema(db, table);
        List<Example> set = new ArrayList<>();
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT * FROM " + table);

        while (resultSet.next()) {
            Example ex = new Example();
            for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
                if (tableSchema.getColumn(i).isNumber()) {
                    ex.add(resultSet.getDouble(tableSchema.getColumn(i).getColumnName()));
                } else {
                    ex.add(resultSet.getString(tableSchema.getColumn(i).getColumnName()));
                }
            }
            set.add(ex);
        }
        if (set.isEmpty()) {
            throw new EmptySetException("Empty set.");
        }
        resultSet.close();
        statement.close();

        return set;
    }

    /**
     * Esegue una query SQL per estrarre i valori distinti ordinati di column e popola un insieme
     * da restituire.
     *
     * @param table  Nome della tabella.
     * @param column Nome della colonna.
     * @return Lista di valori distinti della colonna specificata.
     * @throws SQLException Se viene sollevata un'eccezione SQL.
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        TreeSet<Object> set = new TreeSet<>();
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement
                .executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table);

        if (column.isNumber()) {
            while (resultSet.next()) {
                set.add(resultSet.getDouble(column.getColumnName()));
            }
        } else {
            while (resultSet.next()) {
                set.add(resultSet.getString(column.getColumnName()));
            }
        }
        resultSet.close();
        statement.close();

        return set;
    }

    /**
     * Esegue una query SQL per estrarre il valore aggregato cercato nella colonna di nome column della
     * tabella di nome table.
     *
     * @param table     Nome della tabella.
     * @param column    Nome della colonna.
     * @param aggregate Operatore SQL di aggregazione(MIN, MAX).
     * @return Insieme dei valori distinti per l'attributo identificato  dal nome della colonna.
     * @throws SQLException     Se viene sollevata un'eccezione SQL.
     * @throws NoValueException Se viene sollevata un'eccezione NoValueException.
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
            throws SQLException, NoValueException {
        Statement statement = db.getConnection().createStatement();
        Object valueToReturn;

        String query = "SELECT " + aggregate.toString() + "(" + column.getColumnName() + ") AS "
                + column.getColumnName() + " FROM " + table;
        ResultSet resultSet = statement.executeQuery(query);
        if (!resultSet.next()) {
            throw new NoValueException("Error: result set empty.");
        }
        if (column.isNumber()) {
            valueToReturn = resultSet.getDouble(column.getColumnName());
        } else {
            valueToReturn = resultSet.getString(column.getColumnName());
        }

        resultSet.close();
        statement.close();

        return valueToReturn;
    }
}
