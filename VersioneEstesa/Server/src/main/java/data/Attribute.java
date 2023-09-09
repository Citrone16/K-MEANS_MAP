package data;

import java.io.Serializable;

/**
 * Questa classe astratta gestisce un attributo generico.
 */
abstract class Attribute implements Serializable {
    private final String name;
    private final int index;

    /**
     * Costruttore che inizializza gli attributi della classe.
     *
     * @param name  Nome dell'attributo.
     * @param index Indice dell'attributo.
     */
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome dell'attributo.
     *
     * @return Nome dell'attributo.
     */
    String getName() {
        return name;
    }

    /**
     * Restituisce l'indice dell'attributo.
     *
     * @return Indice dell'attributo.
     */
    int getIndex() {
        return index;
    }

    /**
     * Restituisce una stringa contenente il nome dell'attributo.
     *
     * @return Stringa contenente il nome dell'attributo
     */
    @Override
    public String toString() {
        return name;
    }
}
