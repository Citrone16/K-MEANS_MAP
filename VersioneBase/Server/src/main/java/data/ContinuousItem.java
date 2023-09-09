package data;

public class ContinuousItem extends Item {
    ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    @Override
    double distance(Object a) {
        double thisValue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) this.getValue());
        double aValue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) a);

        return Math.abs(thisValue - aValue);
    }
}
