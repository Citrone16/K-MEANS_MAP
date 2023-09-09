package data;

/**
 * Questa classe gestisce un item di tipo continuo.
 */
public class ContinuousItem extends Item {

    /**
     * Costruttore che iniziallizza i valori della classe.
     *
     * @param attribute Attributo dell'item.
     * @param value     Valore dell'item.
     */
    ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra due item di tipo continuo.
     *
     * @param a Oggetto rispetto al quale calcolare la distanza.
     * @return Distanza tra i due item.
     */
    @Override
    double distance(Object a) {
        double thisValue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) this.getValue());
        double aValue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) a);

        return Math.abs(thisValue - aValue);
    }
}
