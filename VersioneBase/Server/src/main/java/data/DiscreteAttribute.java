package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class DiscreteAttribute extends Attribute implements Iterable<String> {
    private TreeSet<String> values;

    DiscreteAttribute(String name, int index, String[] values) {
        super(name, index);
        this.values = new TreeSet<>(Arrays.asList(values));
    }

    int getNumberOfDistinctValues() {
        return values.size();
    }

    int frequency(Data data, Set<Integer> idList, String v) {
        int count = 0;

        for (Integer i : idList) {
            if (data.getAttributeValue(i, getIndex()).equals(v)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}
