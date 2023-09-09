package database;

import database.TableSchema.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TableData {

    DbAccess db;


    public TableData(DbAccess db) {
        this.db = db;
    }

    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        TableSchema tableSchema = new TableSchema(db, table);
        List<Example> list = new LinkedList<>();
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
            list.add(ex);
        }
        if (list.isEmpty()) {
            throw new EmptySetException("Empty set.");
        }
        resultSet.close();
        statement.close();

        return list;
    }


    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        TreeSet<Object> set = new TreeSet<>();
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table);

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

    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
        Statement statement = db.getConnection().createStatement();
        Object valueToReturn;

        String query = "SELECT " + aggregate.toString() + "(" + column.getColumnName() + ") AS " + column.getColumnName() + " FROM " + table + ";";
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
