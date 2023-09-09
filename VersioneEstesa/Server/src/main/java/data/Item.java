package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Questa classe astratta gestisce un item generico.
 */
public abstract class Item implements Serializable {
    private final Attribute attribute;
    private Object value;

    /**
     * Costruttore che inizializza i valori della classe.
     *
     * @param attribute Attrubuto dell'item
     * @param value     Valore dell'item
     */
    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce l'attributo dell'item.
     *
     * @return Attributo dell'item.
     */
    Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore dell'item.
     *
     * @return Valore dell'item.
     */
    Object getValue() {
        return value;
    }

    /**
     * Restituisce una stringa contenente la rappresentazione dell'item.
     *
     * @return Rappresentazione dell'item in stringa.
     */
    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else {
            return "null";
        }
    }

    /**
     * Calcola la distanza tra due item.
     *
     * @param a Oggetto rispetto al quale calcolare la distanza.
     * @return Distanza tra i due item.
     */
    abstract double distance(Object a);

    /**
     * Aggiorna il valore dell'item.
     *
     * @param data          Dataset.
     * @param clusteredData Insieme degli indici delle transazione appartenenti al cluster in considerazione.
     */
    public void update(Data data, Set<Integer> clusteredData) {
        value = data.computePrototype(clusteredData, attribute);
    }
}
