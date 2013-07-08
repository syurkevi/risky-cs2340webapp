package edu.gatech.cs2340.risky.models;

import javax.servlet.http.HttpServletRequest;

import edu.gatech.cs2340.risky.Model;

public class Territory extends Model {
    public Integer[][] vertexes;
    public int[] center;
    private static int instanceCount = 0;
    
    public Territory(Integer[][] vertexes) {
        this.id = instanceCount++;
        this.vertexes = vertexes;
    }
    
    public Territory(Integer[][] vertexes, int[] center) {
        this.id = instanceCount++;
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