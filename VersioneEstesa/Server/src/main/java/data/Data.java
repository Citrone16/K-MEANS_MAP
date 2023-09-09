package data;

import database.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Questa classe gestisce la tabella su cui lavorare.
 */
public class Data {

    private final List<Example> data;
    private final int numberOfExamples;
    private final List<Attribute> attributeSet = new LinkedList<>();

    /**
     * Costruttore che inizializza il dataset con i dati presenti nel database.
     *
     * @param table Nome della tabella nel database.
     * @throws DatabaseConnectionException Se la connessione al database è fallita.
     * @throws SQLException Se vi è un errore SQL.
     * @throws EmptySetException Se il set è vuoto.
     * @throws NoValueException Se non ci sono risultati.
     */
    public Data(String table) throws DatabaseConnectionException, SQLException, EmptySetException, NoValueException {
        DbAccess db = new DbAccess();
        db.initConnection();

        try {
            TableData td = new TableData(db);
            data = td.getDistinctTransazioni(table);


            TableSchema ts = new TableSchema(db, table);

            numberOfExamples = data.size();

            for (int i = 0; i < Objects.requireNonNull(ts).getNumberOfAttributes(); i++) {
                if (ts.getColumn(i).isNumber()) {
                    double min = (Double) td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MIN);
                    double max = (Double) td.getAggregateColumnValue(table, ts.getColumn(i), QUERY_TYPE.MAX);
                    attributeSet.add(new ContinuousAttribute(ts.getColumn(i).getColumnName(), i, min, max));
                } else {
                    Object[] tableResult = td.getDistinctColumnValues(table, ts.getColumn(i)).toArray();
                    int sizeOfDistinctTuple = tableResult.length;
                    String[] distinctValues = new String[sizeOfDistinctTuple];
                    System.arraycopy(tableResult, 0, distinctValues, 0, sizeOfDistinctTuple);
                    this.attributeSet.add(new DiscreteAttribute(ts.getColumn(i).getColumnName(), i, distinctValues));
                }
            }
        } finally {
            db.closeConnection();
        }
    }

    /**
     * Restituisce il numero di esempi.
     *
     * @return Numero di esempi.
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero di attributi.
     *
     * @return Numero di attributi.
     */
    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    /**
     * Restituisce lo schema del dataset.
     *
     * @return Array di data.Attribute che rappresenta lo schema del dataset.
     */
    List<Attribute> getAttributeSchema() {
        return attributeSet;
    }

    /**
     * Restituisce il valore di un attributo (colonna) di un esempio (riga).
     *
     * @param exampleIndex   Indice della riga.
     * @param attributeIndex Indice della colonna.
     * @return Valore dell'attributo.
     */
    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Restituisce l'attributo della tabella presente in posizione index.
     *
     * @param index Indice dell'attributo da restituire.
     * @return Attributo presente in posizione index.
     */
    Attribute getAttribute(int index) {
        return this.attributeSet.get(index);
    }

    /**
     * Restituisce la rappresentazione in stringa del dataset.
     *
     * @return Stringa che contiene il dataset.
     */
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

    /**
     * Restituisce una tupla che contiene i valori degli attributi di un esempio.
     *
     * @param index Indice dell'esempio.
     * @return Tupla che contiene i valori degli attributi di un esempio.
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(getNumberOfAttributes());

        for (int i = 0; i < getNumberOfAttributes(); i++) {
            if (getAttribute(i) instanceof DiscreteAttribute) {
                tuple.add(new DiscreteItem((DiscreteAttribute) getAttribute(i),
                        (String) data.get(index).get(getAttribute(i).getIndex())), getAttribute(i).getIndex());
            } else if (getAttribute(i) instanceof ContinuousAttribute) {
                tuple.add(new ContinuousItem(getAttribute(i), (Double) data.get(index).get(getAttribute(i).getIndex())),
                        getAttribute(i).getIndex());
            }
        }
        return tuple;
    }

    /**
     * Esegue il passo 1 del K-Means scegliendo casualmente k centroidi.
     *
     * @param k Numero di centroidi da generare.
     * @return Array di k interi rappresentanti gli indici di riga delle tuple scelte come centroidi.
     * @throws OutOfRangeSampleSize Se k non è compreso tra 1 e il numero di transazioni.
     */
    public int[] sampling(int k) throws OutOfRangeSampleSize {
        if (k > this.numberOfExamples || k <= 0) {
            throw new OutOfRangeSampleSize(
                    "Number of clusters is greater than " + this.numberOfExamples + "or less than 0.",
                    new UnsupportedOperationException());
        }
        int[] centroidIndexes = new int[k];
        // choose k random different centroids in data.
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
            } while (found);
            centroidIndexes[i] = c;
        }
        return centroidIndexes;
    }

    /**
     * Verifica se due transazioni sono uguali.
     *
     * @param i Indice della prima transazione.
     * @param j Indice della seconda transazione.
     * @return True se le due transazioni sono uguali, false altrimenti.
     */
    private boolean compare(int i, int j) {
        return i == j || data.get(i).compareTo(data.get(j)) == 0;
    }

    /**
     * Calcola il centroide rispetto ad un attributo.
     *
     * @param idList    Set di indici.
     * @param attribute Attributo sul quale calcolare il centroide.
     * @return Valore del centroide.
     */
    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        if (attribute instanceof DiscreteAttribute)
            return computePrototype(idList, (DiscreteAttribute) attribute);
        else
            return computePrototype(idList, (ContinuousAttribute) attribute);
    }

    /**
     * Determina il valore più frequente per un DiscreteAttribute nel sottoinsieme di dati individuato da idList.
     *
     * @param idList    Set di indici.
     * @param attribute DiscreteAttribute sul quale calcolare il centroide.
     * @return Valore del centroide.
     */
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

    /**
     * Determina il valore pù frequente per un ContinuousAttribute nel sottoinsieme di dati individuato da idList.
     *
     * @param idList    Set di indici.
     * @param attribute ContinuousAttribute sul quale calcolare il centroide.
     * @return Valore del centroide.
     */
    private double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
        double value = 0.0;

        for (Integer i : idList) {
            value += (Double) getAttributeValue(i, attribute.getIndex());
        }
        value /= idList.size();

        return value;
    }
}
