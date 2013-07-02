package edu.gatech.cs2340.risky.database;

import java.io.Serializable;
import java.util.Collection;

import edu.gatech.cs2340.risky.Model;

public interface ModelDb<T extends Model> extends Serializable  {

    public T get(Object id);

    public Collection<T> query();

    public Object create(T model);

    public T update(Object id, T model);

    public T delete(Object id);
    
    public void empty();
}
