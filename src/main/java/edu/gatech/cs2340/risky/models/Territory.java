package edu.gatech.cs2340.risky.models;

import edu.gatech.cs2340.risky.Model;

public class Territory extends Model {
    int[][] vertexes;
    private static int instanceCount = 0;
    
    public Territory(int[] ... vertexes) {
        this.vertexes = new int[vertexes.length][];
        for (int i=0 ; i < vertexes.length ; i++) {
            this.vertexes[i] = vertexes[i];
        }
        this.id = instanceCount++;
    }
}