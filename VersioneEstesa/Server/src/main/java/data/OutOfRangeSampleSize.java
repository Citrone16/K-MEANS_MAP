package data;

/**
 * Questa classe gestisce l'eccezione che viene lanciata quando il numero di cluster inserito dall'utente è maggiore
 * del numero di tuple del dataset.
 */
public class OutOfRangeSampleSize extends Exception {
    /**
     * Costruttore della classe.
     *
     * @param msg Messaggio di errore visualizzato.
     * @param e   Evento che genera l'eccezione.
     */
    public OutOfRangeSampleSize(String msg, Throwable e) {
        super(msg, e);
    }
}
