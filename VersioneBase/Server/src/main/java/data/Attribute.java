package data;

import java.io.Serializable;

abstract class Attribute implements Serializable {
    private final String name;
    private final int index;

    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    String getName() {
        return name;
    }

    int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
