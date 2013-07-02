package edu.gatech.cs2340.risky.database;

import java.util.ArrayList;

import edu.gatech.cs2340.risky.Model;

public class ArrayListDbImpl<T extends Model> implements ModelDb<T> {

    private ArrayList<T> values = new ArrayList<T>();

    public synchronized T get(Object id) {
        synchronized(this) {
            for (T item : values) {
                if (item.id.equals(id)) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public synchronized ArrayList<T> query() {
        synchronized(this) {
            return values;
        }   
    }

    public synchronized Object create(T value) {
        synchronized(this) {// important for when multiple requests access the same db
            Integer newId = values.size();
            values.add(value);
            return newId;
        }
    }

    public synchronized T update(Object id, T value) {
        synchronized(this) {
            for (int i=0 ; i < values.size() ; i++) {
                if (values.get(i).id.equals(id)) {
                    T temp = values.get(i);
                    values.set(i, value);
                    return temp;
                }
            }
        }
        return null;
    }

    public synchronized T delete(Object id) {
        synchronized(this) {
            for (int i=0 ; i < values.size() ; i++) {
                if (values.get(i).id.equals(id)) {
                    T item = values.get(i);
                    values.remove(i);
                    return item;
                }
            }
        }
        return null;
    }
    
    public synchronized void empty() {
        this.values = new ArrayList<T>();
    }
}
