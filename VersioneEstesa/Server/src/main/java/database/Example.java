package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe gestisce una transazione letta dal Database.
 */
public class Example implements Comparable<Example> {
    private final List<Object> example = new ArrayList<>();

    /**
     * Aggiunge un oggetto alla lista di example.
     *
     * @param o Oggetto da aggiungere.
     */
    public void add(Object o) {
        example.add(o);
    }

    /**
     * Restituisce l'oggetto in posizione i nella lista di example.
     *
     * @param i Indice dell'oggetto nella lista di example.
     * @return Oggetto in posizione i.
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta l'istanza corrente della classe Example con un'altra istanza fornita come argomento.
     *
     * @param ex Oggetto da confrontare.
     * @return Se tutte le posizioni sono uguali restituisce 0, altrimenti restituisce il risultato del
     * confronto tra gli elementi diversi.
     */
    public int compareTo(Example ex) {

        int i = 0;
        for (Object o : ex.example) {
            if (!o.equals(this.example.get(i)))
                return ((Comparable) o).compareTo(example.get(i));
            i++;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale della classe Example.
     *
     * @return Stringa contenente la rappresentazione.
     */
    public String toString() {
        String str = "";
        for (Object o : example)
            str += o.toString() + " ";
        return str;
    }
}