package mining;

import data.Data;
import data.OutOfRangeSampleSize;

import java.io.*;

public class KMeansMiner {
    private final ClusterSet C;

    public KMeansMiner(int k) throws OutOfRangeSampleSize {
        C = new ClusterSet(k);
    }

    public KMeansMiner(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
        C = (ClusterSet) objectInputStream.readObject();
        objectInputStream.close();
    }

    public ClusterSet getC() {
        return C;
    }

    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            //STEP 2
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                //rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //STEP 3
            C.updateCentroids(data);
        }
        while (changedCluster);
        return numberOfIterations;
    }

    public void salva(String fileName) throws FileNotFoundException, IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
        objectOutputStream.writeObject(C);
        objectOutputStream.close();
    }
}
