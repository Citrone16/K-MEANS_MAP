package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Questa classe gestisce un attributo discreto.
 */
class DiscreteAttribute extends Attribute implements Iterable<String> {
    private final TreeSet<String> values;

    /**
     * Costruttore che inizializza i valori della classe.
     *
     * @param name   Nome dell'attributo.
     * @param index  Indice dell'attributo.
     * @param values Valori dell'attributo.
     */
    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new TreeSet<>(Arrays.asList(values));
    }

    /**
     * Restituisce il numero di occorrenenze di un valore in un insieme di esempi dell'attributo.
     *
     * @param data   Dataset sul quale contare le occorrenze.
     * @param idList Insieme di esempi.
     * @param v      Valore considerato.
     * @return Numero di occorrenze.
     */
    int frequency(Data data, Set<Integer> idList, String v) {
        int count = 0;

        for (Integer i : idList) {
            if (data.getAttributeValue(i, getIndex()).equals(v)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Restituisce l'iterator che permette di iterare sui valori dell'attributo.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}
