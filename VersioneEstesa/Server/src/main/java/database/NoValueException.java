package database;

/**
 * Questa classe gestisce un'eccezione di tipo NoValueException.
 */
public class NoValueException extends Exception {
    /**
     * Costruttore della classe.
     *
     * @param msg Messaggio visualizzato.
     */
    public NoValueException(String msg) {
        super(msg);
    }
}
