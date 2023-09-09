package data;

public class DiscreteItem extends Item {
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    double distance(Object a) {
        return getValue().equals(a) ? 0 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.getValue().equals(((Item) obj).getValue()));
    }
}
