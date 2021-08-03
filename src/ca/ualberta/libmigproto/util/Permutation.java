package ca.ualberta.libmigproto.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Permutation<T> implements Iterator<List<T>> {
    private final List<T> items;
    private Integer[] current;
    private boolean done = false;

    public Permutation(List<T> items, int stringSize) {
        this.items = items;
        this.current = new Integer[stringSize];
        for (int i = 0; i < current.length; i++) {
            current[i] = 0;
        }
    }

    public boolean hasNext() {
        return !done;
    }

    public List<T> next() {
        if (done) {
            throw new RuntimeException("no more items");
        }
        var next = Arrays.stream(current).map(i -> items.get(i)).toList();
        increase(0);
        return next;
    }

    public void increase(int index) {
        if (current[index] == items.size() - 1) {
            if (index == current.length - 1) {
                done = true;
                return;
            }
            current[index] = 0;
            for (int i = 0; i <= index; i++) {
                current[i] = 0;
            }
            increase(index + 1);
        } else {
            current[index]++;
        }
    }
}
