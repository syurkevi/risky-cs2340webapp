package edu.gatech.cs2340.risky.database;

import java.util.ArrayList;

import edu.gatech.cs2340.risky.Model;

public class ArrayListDbImpl<T extends Model> implements ModelDb<T> {

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
        for (int i=0 ; i < values.size() ; i++) {
            if (values.get(i).id.equals(id)) {
                T temp = values.get(i);
                values.set(i, value);
                return temp;
            }
        }
        return null;
    }

    public T delete(Object id) {
        for (int i=0 ; i < values.size() ; i++) {
            if (values.get(i).id.equals(id)) {
                T item = values.get(i);
                values.remove(i);
                return item;
            }
        }
        return null;
    }
}
