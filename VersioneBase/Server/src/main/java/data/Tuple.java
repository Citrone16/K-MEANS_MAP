package data;

import java.io.Serializable;

public class Tuple implements Serializable {
    private final Item[] tuple;

    Tuple(int size) {
        tuple = new Item[size];
    }

    public int getLength() {
        return tuple.length;
    }

    public Item get(int i) {
        return tuple[i];
    }

    public void add(Item c, int i) {
        tuple[i] = c;
    }

    public double getDistance(Tuple obj) {
        double distance = 0.0;

        for (int i = 0; i < tuple.length; i++) {
            distance += get(i).distance(obj.get(i).getValue());
        }
        return distance;
    }

    public double avgDistance(Data data, int[] clusteredData) {
        double p, sumD = 0.0;

        for (int i = 0; i < clusteredData.length; i++) {
            double d = getDistance(data.getItemSet(clusteredData[i]));
            sumD += d;
        }
        p = sumD / clusteredData.length;
        return p;
    }
}
