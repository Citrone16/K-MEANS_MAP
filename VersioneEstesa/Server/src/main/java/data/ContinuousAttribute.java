package data;

/**
 * Questa classe gestisce un attributo continuo.
 */
class ContinuousAttribute extends Attribute {
    private final double max;
    private final double min;

    /**
     * Costruttore che inizializza gli attributi della classe.
     *
     * @param name  Nome dell'attributo.
     * @param index Indice dell'attributo.
     * @param min   Estremo inferiore dell'intervallo.
     * @param max   Estremo superiore dell'intervallo.
     */
    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Calcola e restituisce il valore normalizzato del parametro passato in input.
     * La normalizzazione ha come codominio l'intervallo [0,1].
     *
     * @param v Valore dell'attributo da normalizzare.
     * @return Valore normalizzato.
     */
    double getScaledValue(double v) {
        return (v - min) / (max - min);
    }
}
