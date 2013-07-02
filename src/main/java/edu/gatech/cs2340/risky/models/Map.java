package edu.gatech.cs2340.risky.models;

import java.util.ArrayList;

import edu.gatech.cs2340.risky.Model;

public class Map extends Model {
    ArrayList<Territory> territories;
    
    public Map() {
        this.territories = new ArrayList<Territory>();
    }
    
    public void addTerritory(Territory t) {
        territories.add(t);
    }
    
}
