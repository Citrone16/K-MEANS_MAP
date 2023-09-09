package mining;

import data.Data;
import data.OutOfRangeSampleSize;

import java.io.*;

/**
 * Questa classe implementa l'algoritmo K-Means
 */
public class KMeansMiner {
    private final ClusterSet C;

    /**
     * Costruttore che inizializza l'attributo della classe con un insieme di k cluster.
     *
     * @param k Numero di cluster.
     * @throws OutOfRangeSampleSize Se il numero di cluster (k) è minore o uguale a 1.
     */
    public KMeansMiner(int k) throws OutOfRangeSampleSize {
        C = new ClusterSet(k);
    }

    /**
     * Costruttore che inizializza l'attributo della classe leggendo i dati serializzati da un file specificato.
     *
     * @param fileName Nome del file contenente i dati serializzati per inizializzare il ClusterSet.
     * @throws IOException            Se si verifica un errore durante la lettura del file.
     * @throws ClassNotFoundException Se la classe ClusterSet non viene trovata durante la deserializzazione.
     */
    public KMeansMiner(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
        C = (ClusterSet) objectInputStream.readObject();
        objectInputStream.close();
    }

    /**
     * Restituisce l'insieme di cluster utilizzato dal KMeansMiner.
     *
     * @return ClusterSet di KMeansMiner.
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo K-Means sull'oggetto Data fornito e raggruppa i dati in cluster.
     *
     * @param data Dataset.
     * @return Numero di iterazioni eseguite per raggiungere la convergenza dell'algoritmo.
     * @throws OutOfRangeSampleSize Se il numero di cluster specificato nel costruttore è maggiore
     *                              del numero di transazioni presenti nei dati.
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        // STEP 1
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            // STEP 2
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                // rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    // il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            // STEP 3
            C.updateCentroids(data);
        } while (changedCluster);
        return numberOfIterations;
    }

    /**
     * Salva i dati serializzati dell'oggetto ClusterSet in un file specificato.
     *
     * @param fileName Nome del file in cui salvare i dati serializzati.
     * @throws FileNotFoundException Se il file specificato non viene trovato.
     * @throws IOException           Se si verifica un errore durante la scrittura dei dati nel file.
     */
    public void salva(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        objectOutputStream.writeObject(C);
        objectOutputStream.close();
    }
}
