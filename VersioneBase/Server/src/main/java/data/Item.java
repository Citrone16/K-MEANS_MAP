package data;

import java.io.Serializable;
import java.util.Set;

public abstract class Item implements Serializable {
    private final Attribute attribute;
    private Object value;

    Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    Attribute getAttribute() {
        return attribute;
    }

    Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else {
            return "null";
        }
    }

    abstract double distance(Object a);

    public void update(Data data, Set<Integer> clusteredData) {
        value = data.computePrototype(clusteredData, attribute);
    }
}
