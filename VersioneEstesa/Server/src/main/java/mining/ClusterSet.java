package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

import java.io.Serializable;

/**
 * Questa classe gestisce un insieme di cluster.
 */
public class ClusterSet implements Serializable {
    private final Cluster[] C;
    private int i = 0;

    /**
     * Costruisce un nuovo insieme di cluster con il numero specificato di cluster.
     *
     * @param k Numero di cluster da creare.
     * @throws OutOfRangeSampleSize Se il numero di cluster (k) è minore o uguale a 1.
     */
    ClusterSet(int k) throws OutOfRangeSampleSize {
        if (k <= 1) {
            throw new OutOfRangeSampleSize("Number of cluster must be greater than 1.",
                    new UnsupportedOperationException());
        }
        C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster all'insieme di cluster.
     *
     * @param c Oggetto Cluster da aggiungere all'insieme.
     */
    void add(Cluster c) {
        C[i] = c;
        i++;
    }

    /**
     * Restituisce l'oggetto Cluster nella posizione specificata.
     *
     * @param i l'indice dell'oggetto Cluster da restituire.
     * @return l'oggetto Cluster nella posizione specificata.
     */
    Cluster get(int i) {
        return C[i];
    }

    /**
     * Inizializza i centroidi dei cluster.
     *
     * @param data Dataset
     * @throws OutOfRangeSampleSize Se il numero di cluster (k) specificato nel costruttore è maggiore del numero di transazioni presenti nei dati.
     */
    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int[] centroidIndexes = data.sampling(C.length);

        for (int i : centroidIndexes) {
            Tuple centroidI = data.getItemSet(i);
            add(new Cluster(centroidI));
        }
    }

    /**
     * Restituisce il cluster più vicino alla tupla specificata.
     *
     * @param tuple Tupla di cui trovare il cluster più vicino.
     * @return Cluster più vicino alla tupla specificata.
     */
    Cluster nearestCluster(Tuple tuple) {
        Cluster nearest = null;
        double distance = Double.MAX_VALUE;
		for (Cluster cluster : C) {
			if (tuple.getDistance(cluster.getCentroid()) < distance) {
				nearest = cluster;
				distance = tuple.getDistance(cluster.getCentroid());
			}
		}
        return nearest;
    }

    /**
     * Restituisce il cluster corrente a cui appartiene la transazione con l'identificatore specificato.
     *
     * @param id Identificatore della transazione.
     * @return Cluster corrente a cui appartiene la transazione con l'identificatore specificato.
     * Restituisce null se la transazione non appartiene a nessun cluster.
     */
    Cluster currentCluster(int id) {
		for (Cluster cluster : C) {
			if (cluster.contain(id)) {
				return cluster;
			}
		}
        return null;
    }

    /**
     * Aggiorna i centroidi di tutti i cluster nell'insieme di cluster basandosi sul dataset fornito.
     *
     * @param data Dataset.
     */
    void updateCentroids(Data data) {
		for (Cluster cluster : C) {
			cluster.computeCentroid(data);
		}
    }

    /**
     * Restituisce una rappresentazione testuale dell'insieme di cluster con le informazioni sul centroide.
     *
     * @return Stringa contenente l'indice di ciascun cluster e le informazioni sul centroide del cluster.
     */
    @Override
    public String toString() {
        String str = "";

        for (int i = 0; i < C.length; i++) {
            str += i + ":" + get(i).toString() + "\n";
        }
        return str;
    }

    /**
     * Restituisce una rappresentazione testuale dettagliata dell'insieme di cluster con le informazioni
     * sul centroide e sulle transazioni.
     *
     * @param data Dataset.
     * @return una stringa contenente l'indice di ciascun cluster, le informazioni sul centroide del cluster
     * e le informazioni sulle transazioni appartenenti a ciascun cluster.
     */
    public String toString(Data data) {
        String str = "";

        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + get(i).toString(data) + "\n";
            }
        }
        return str;
    }
}
