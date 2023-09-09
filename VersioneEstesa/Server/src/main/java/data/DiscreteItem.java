package data;

/**
 * Questa classe gestisce un item di tipo discreto.
 */
public class DiscreteItem extends Item {
	/**
	 * Costruttore che inizializza i valori della classe.
	 * @param attribute Attributo dell'item.
	 * @param value Valore dell'item.
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}

	/**
	 * Restituisce la distanza tra due item.
	 * Assume valore 1 se il valore dell'item è diverso dal valore dell'oggetto passato come parametro.
	 * Assume valore 0 altrimenti.
	 * @param a Oggetto rispetto al quale calcolare la distanza.
	 * @return Distanza degli item.
	 */
	double distance(Object a) {
		return getValue().equals(a) ? 0 : 1;
	}

	/**
	 * Confronta l'oggetto corrente con l'oggetto fornito come parametro per determinare se sono uguali.
	 * @param obj Oggetto con cui eseguire il confronto.
	 * @return True se gli oggetti confrontati sono uguali, false altrimenti.
	 */
	@Override
	public boolean equals(Object obj) {
		return (this.getValue().equals(((Item) obj).getValue()));
	}
}
