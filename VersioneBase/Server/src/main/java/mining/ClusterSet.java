package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

import java.io.Serializable;

public class ClusterSet implements Serializable {
    private final Cluster[] C;
    private int i = 0;

    ClusterSet(int k) throws OutOfRangeSampleSize {
        if (k <= 1) {
            throw new OutOfRangeSampleSize("Number of cluster must be greater than 1.", new UnsupportedOperationException());
        }
        C = new Cluster[k];
    }

    void add(Cluster c) {
        C[i] = c;
        i++;
    }

    Cluster get(int i) {
        return C[i];
    }

    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int centroidIndexes[] = data.sampling(C.length);

        for (int i : centroidIndexes) {
            Tuple centroidI = data.getItemSet(i);
            add(new Cluster(centroidI));
        }
    }

    Cluster nearestCluster(Tuple tuple) {
        Cluster nearest = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < C.length; i++) {
            if (tuple.getDistance(C[i].getCentroid()) < distance) {
                nearest = C[i];
                distance = tuple.getDistance(C[i].getCentroid());
            }
        }
        return nearest;
    }

    Cluster currentCluster(int id) {
        for (int i = 0; i < C.length; i++) {
            if (C[i].contain(id)) {
                return C[i];
            }
        }
        return null;
    }

    void updateCentroids(Data data) {
        for (int i = 0; i < C.length; i++) {
            C[i].computeCentroid(data);
        }
    }

    @Override
    public String toString() {
        String str = "";

        for (int i = 0; i < C.length; i++) {
            str += i + ":" + C[i].toString() + "\n";
        }
        return str;
    }

    public String toString(Data data) {
        String str = "";

        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ":" + C[i].toString(data) + "\n";
            }
        }
        return str;
    }
}
