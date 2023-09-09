package mining;

import data.Data;
import data.Tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Questa classe modella un cluster di dati.
 */
public class Cluster implements Serializable {
    private final Tuple centroid;

    private final Set<Integer> clusteredData;

    /**
     * Costruttore che inizializza gli attributi della classe.
     *
     * @param centroid Valore del centroide da assegnare all'attributo della classe.
     */
    Cluster(Tuple centroid) {
        this.centroid = centroid;
        clusteredData = new HashSet<>();

    }

    /**
     * Resituisce il centroide del cluster.
     *
     * @return Centroide del cluster
     */
    Tuple getCentroid() {
        return centroid;
    }

    /**
     * Calcola il nuovo centroide del cluster basandosi sui dati forniti nell'oggetto Data
     *
     * @param data Dataset.
     */
    void computeCentroid(Data data) {
        for (int i = 0; i < centroid.getLength(); i++) {
            centroid.get(i).update(data, clusteredData);
        }
    }

    /**
     * Aggiunge una transizione al cluster
     *
     * @param id Identificatore della transizione da aggiungere al cluster.
     * @return True se la transazione è stata aggiunta al cluster, false se era già presente.
     */
    boolean addData(int id) {
        return clusteredData.add(id);
    }

    /**
     * Verifica se una transazione con l'identificatore specificato appartiene al cluster.
     *
     * @param id Identificatore della transazione da cercare nel cluster.
     * @return True se la transazione è presente nel cluster, false altrimenti.
     */
    boolean contain(int id) {
        return clusteredData.contains(id);
    }

    /**
     * Rimuove una transazione dal cluster se presente.
     *
     * @param id Identificatore della transazione da rimuovere dal cluster.
     */
    void removeTuple(int id) {
        clusteredData.remove(id);
    }

    /**
     * Restituisce una rappresentazione testuale del cluster con le informazioni sul centroide.
     *
     * @return Stringa contenente il centroide del cluster.
     */
    @Override
    public String toString() {
        String str = "Centroid=(";

        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")";

        return str;
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata del cluster.
     *
     * @param data Dataset.
     * @return Stringa contenente il centroide del cluster e le informazioni sulle transazioni del cluster.
     */
    public String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")\nExamples:\n";

        int[] intArray = Arrays.stream(clusteredData.toArray()).mapToInt(x -> (int) x).toArray();

        for (int i : intArray) {
            str += "[";
            for (int j = 0; j < data.getNumberOfAttributes(); j++)
                str += data.getAttributeValue(i, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(i)) + "\n";

        }
        str += "AvgDistance=" + getCentroid().avgDistance(data, intArray) + "\n";

        return str;
    }

}
