package edu.gatech.cs2340.risky.database;

import java.io.Serializable;
import java.util.Map;

public interface ModelDb<T> extends Serializable  {

    public T get(Integer id);

    public Map<Integer, T> getAll();

    public Integer create(T model);

    public T update(Integer id, T model);

    public T delete(Integer id);
}
