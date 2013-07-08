package edu.gatech.cs2340.risky.models;

import javax.servlet.http.HttpServletRequest;

import edu.gatech.cs2340.risky.Model;

public class Territory extends Model {
    public Integer[][] vertexes;
    public Integer[] center;
    
    public Territory(int id, Integer[][] vertexes) {
        this(id, vertexes, null);
    }
    
    public Territory(int id, Integer[][] vertexes, Integer[] center) {
        this.id = id;
        this.vertexes = vertexes;
        this.center = center;
    }
    
    public static TerritoryDeed get(HttpServletRequest request, Object territoryId) {
        Map map = Map.get(request);
        if (map == null) {
            return null;
        }
        return map.deeds.get(territoryId);
    }
}