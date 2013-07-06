package edu.gatech.cs2340.risky;

import java.util.UUID;

public class Model {
    
    public Object id;
    public Database db;
    
    public void populateValidWith(Model m) {
        
    }
    
    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }
    
    // wrote this initially to provide things like .toApi, or relationship mapping
    // but at the moment, those aren't needed
}
