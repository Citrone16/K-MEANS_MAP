package data;

import database.*;

import java.sql.SQLException;
import java.util.*;

public class Data {

    private List<Example> data;
    private final int numberOfExamples;
    private final List<Attribute> attributeSet;

    public Data(String table) throws DatabaseConnectionException {
        DbAccess db = new DbAccess();

        try {
            db.initConnection();
        } catch (DatabaseConnectionException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        TableData td = new TableData(db);
        try {
            data = td.getDistinctTransazioni(table);
        } catch (SQLException | EmptySetException e) {
            System.out.println("Error: " + e.getMessage());
        }

        TableSchema ts = null;
        try {
            ts = new TableSchema(db, table);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            data = td.getDistinctTransazioni(table);
        } catch (SQLException | EmptySetException e) {
            System.out.println("Error: " + e.getMessage());
        }

        attributeSet = new LinkedList<>();

        numberOfExamples = data.size();

        for (int i = 0; i < Objects.requireNonNull(ts).getNumberOfAttributes(); i++) {
            if (ts.getColumn(i).isNumber()) {
                try {
                    double min = (Double) td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MIN);
                    double max = (Double) td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MAX);
                    attributeSet.add(new ContinuousAttribute(ts.getColumn(i).getColumnName(), i, min, max));
                } catch (SQLException | NoValueException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                try {
                    Object[] tableResult = td.getDistinctColumnValues(table, ts.getColumn(i)).toArray();
                    int sizeOfDistinctTuple = tableResult.length;
                    String[] distinctValues = new String[sizeOfDistinctTuple];
                    System.arraycopy(tableResult, 0, distinctValues, 0, sizeOfDistinctTuple);
                    this.attributeSet.add(new DiscreteAttribute(ts.getColumn(i).getColumnName(), i, distinctValues));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        db.closeConnection();

    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    List<Attribute> getAttributeSchema() {
        return attributeSet;
    }

    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    Attribute getAttribute(int index) {
        return this.attributeSet.get(index);
    }

    @Override
    public String toString() {
        String value = "";

        for (Attribute v : getAttributeSchema()) {
            value += v.getName() + ",";
        }
        value += "\n";

        for (int i = 0; i < getNumberOfExamples(); i++) {
            value += (i + 1) + ":";
            for (int j = 0; j < getNumberOfAttributes(); j++) {
                value += getAttributeValue(i, j) + ",";
            }
            value += "\n";
        }
        return value;
    }

    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(getNumberOfAttributes());

        for (int i = 0; i < getNumberOfAttributes(); i++) {
            if (getAttribute(i) instanceof DiscreteAttribute) {
                tuple.add(new DiscreteItem((DiscreteAttribute) getAttribute(i), (String) data.get(index).get(getAttribute(i).getIndex())), getAttribute(i).getIndex());
            } else if (getAttribute(i) instanceof ContinuousAttribute) {
                tuple.add(new ContinuousItem(getAttribute(i), (Double) data.get(index).get(getAttribute(i).getIndex())), getAttribute(i).getIndex());
            }
        }
        return tuple;
    }

    public int[] sampling(int k) throws OutOfRangeSampleSize {
        if (k > this.numberOfExamples || k <= 0) {
            throw new OutOfRangeSampleSize("Number of clusters is greater than " + this.numberOfExamples + "or less than 0.", new UnsupportedOperationException());
        }
        int[] centroidIndexes = new int[k];
        //choose k random different centroids in data.
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i = 0; i < k; i++) {
            boolean found;
            int c;
            do {
                found = false;
                c = rand.nextInt(getNumberOfExamples());
                // verify that centroid[c] is not equal to a centroid already stored in CentroidIndexes
                for (int j = 0; j < i; j++)
                    if (compare(centroidIndexes[j], c)) {
                        found = true;
                        break;
                    }
            }
            while (found);
            centroidIndexes[i] = c;
        }
        return centroidIndexes;
    }

    private boolean compare(int i, int j) {
        return i == j || data.get(i).compareTo(data.get(j)) == 0;
    }

    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        if (attribute instanceof DiscreteAttribute)
            return computePrototype(idList, (DiscreteAttribute) attribute);
        else
            return computePrototype(idList, (ContinuousAttribute) attribute);
    }

    private String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        if (!attribute.iterator().hasNext()) {
            return null;
        }

        int maxOccurrence = 0;
        String str = null;

        for (String value : attribute) {
            int tempOccurrence = attribute.frequency(this, idList, value);
            if (tempOccurrence > maxOccurrence) {
                maxOccurrence = tempOccurrence;
                str = value;
            }
        }

        return str;
    }

    private double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
        double value = 0.0;

        for (Integer i : idList) {
            value += (Double) getAttributeValue(i, attribute.getIndex());
        }
        value /= idList.size();

        return value;
    }
}
