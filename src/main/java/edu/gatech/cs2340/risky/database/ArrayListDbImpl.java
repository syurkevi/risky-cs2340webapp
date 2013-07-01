package edu.gatech.cs2340.risky.database;

import java.util.ArrayList;

public class ArrayListDbImpl<T> implements ModelDb<T> {

    private ArrayList<T> values = new ArrayList<T>();

    public T get(Object id) {
        for (T item : values) {
            if (item.id.equals(id)) {
                return item;
            }
        }
        return null;
    }
    
    public ArrayList<T> query() {
        return values;
    }

    public Object create(T value) {
        Integer newId = values.size();
        values.add(value);
        return newId;
    }

    public T update(Object id, T value) {
        return values.set(id, value);
    }

    public T delete(Object id) {
        int i=0;
        for ( ; i < values.size() ; i++) {
            if (values.get(i).id.equals(id)) {
                break;
            }
        }
        T item = values.get(i);
        values.remove(i);
        return item;
    }
}
