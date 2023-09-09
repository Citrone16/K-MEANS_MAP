package data;

import java.io.Serializable;

/**
 * Questa classe gestisce una tupla.
 */
public class Tuple implements Serializable {
    private final Item[] tuple;

    /**
     * Costruttore che inizializza il valore della classe.
     *
     * @param size Dimensione della tupla.
     */
    Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * Restituisce la dimensione della tupla.
     *
     * @return Dimensione della tupla.
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'item in posizione i.
     *
     * @param i Indice dell'item da restituire.
     * @return Item in posizione i.
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * Aggiunge un item alla tupla.
     *
     * @param c Item da aggiungere.
     * @param i Indice in cui viene aggiunto l'item.
     */
    public void add(Item c, int i) {
        tuple[i] = c;
    }

    /**
     * Calcola la distanza tra due tuple.
     *
     * @param obj Tupla rispetto alla quale calcolare la distanza.
     * @return Distanza tra le due tuple.
     */
    public double getDistance(Tuple obj) {
        double distance = 0.0;

        for (int i = 0; i < tuple.length; i++) {
            distance += get(i).distance(obj.get(i).getValue());
        }
        return distance;
    }

    /**
     * Restituisce la media delle distanze tra la tupla corrente ed un insieme di tuple.
     *
     * @param data          Dataset che contiene tutte le tuple.
     * @param clusteredData Insieme delle tuple rispetto alle quali calcolare la distanza.
     * @return Media delle distanze.
     */
    public double avgDistance(Data data, int[] clusteredData) {
        double p, sumD = 0.0;

        for (int clusteredDatum : clusteredData) {
            double d = getDistance(data.getItemSet(clusteredDatum));
            sumD += d;
        }
        p = sumD / clusteredData.length;
        return p;
    }
}
