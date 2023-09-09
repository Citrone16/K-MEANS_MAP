package database;

/**
 * Questa classe gestisce un'eccezione di tipo EmptySetException.
 */
public class EmptySetException extends Exception {
    /**
     * Costruttore della classe.
     *
     * @param msg Messaggio visualizzato.
     */
    public EmptySetException(String msg) {
        super(msg);
    }
}
