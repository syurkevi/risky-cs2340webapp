package edu.gatech.cs2340.risky;

import java.util.HashMap;
import java.util.Map;

import edu.gatech.cs2340.risky.database.ArrayListDbImpl;
import edu.gatech.cs2340.risky.database.ModelDb;

public class Database {
    
    protected static HashMap<String, ModelDb> databases = new HashMap<String, ModelDb>();
    
    public static <T extends Model> ModelDb<T> getDb(Class c) {
        return Database.getDb(c, null);
    }
    
    public static <T extends Model> ModelDb<T> getDb(Class c, ModelDb<T> defaultDatabase) {
        ModelDb database = Database.databases.get(c.getName());
        if (database == null && defaultDatabase != null) {
            database = defaultDatabase.getInstance();
            Database.setDb(c, database);
        }
        return database;
    }
    
    public static <T extends Model> void setDb(Class c, ModelDb<T> database) {
        Database.databases.put(c.getName(), database);
    }
    
    public static <T extends Model> T getModel(Class c, Object id) {
        return Database.getModel(c, id, null);
    }
    
    public static <T extends Model> T getModel(Class c, Object id, ModelDb<T> defaultDatabase) {
        // leave an option for a null model so the controller is responsible to create one as it pleases
        ModelDb<T> database = getDb(c, defaultDatabase);
        if (database == null) {
            return null;// or throw NoSuchDatabaseException()?
        }
        return database.read(id);
    }
    
    public static <T extends Model> boolean setModel(T model) {
        ModelDb<T> database = getDb(model.getClass());
        if (database == null) {
            return false;// or throw NoSuchDatabaseException()?
        }
        database.update(model);
        return true;
    }
    
    public static <T extends Model> boolean delete(T model) {
        ModelDb<T> database = getDb(model.getClass());
        if (database == null) {
            return false;// or throw NoSuchDatabaseException()?
        }
        database.delete(model);
        return true;
    }
    
}
