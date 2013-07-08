package edu.gatech.cs2340.risky.models;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.Model;
import edu.gatech.cs2340.risky.Util.Pair;
import edu.gatech.cs2340.risky.database.HashMapDbImpl;

public class Map extends Model {
    public ArrayList<Territory> territories;
    public HashMap<Object, TerritoryDeed> deeds;
    private ArrayList<Pair> adjacencies;
    
    public Map() {
        this.territories = new ArrayList<Territory>();
        this.deeds = new HashMap<Object, TerritoryDeed>();
    }
    
    public void addTerritory(Territory t, Object[] adjacencies) {
        territories.add(t);
        for (Object adjacent : adjacencies) {
            Pair p = new Pair(t.id, adjacent);
            if (!this.adjacencies.contains(p)) {
                this.adjacencies.add(p);
            }
        }
    }
    
    public static Map get(HttpServletRequest request) {
        Lobby lobby = Lobby.get(request);
        return Map.get(lobby.mapId);
    }
    
    public static Map get(Object mapId) {
        return Database.getModel(Map.class, mapId, new HashMapDbImpl<Map>());
    }
}
