package edu.gatech.cs2340.risky.database;

import java.util.Map;
import java.util.HashMap;

public class HashMapDbImpl<T> implements ModelDb<T> {

    private Map<Integer, T> values = new HashMap<Integer, T>();

    public T get(Integer id) {
        return values.get(id);
    }
    
    public Map<Integer, T> getAll() {
        return values;
    }

    public Integer create(T value) {
        Integer newId = values.size();
        values.put(newId, value);
        return newId;
    }

    public T update(Integer id, T value) {
        return values.put(id, value);
    }

    public T delete(Integer id) {
        return values.remove(id);
    }
}
