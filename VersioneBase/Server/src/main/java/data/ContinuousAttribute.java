package data;

class ContinuousAttribute extends Attribute {
    private final double max;
    private final double min;

    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    double getScaledValue(double v) {
        return (v - min) / (max - min);
    }
}
