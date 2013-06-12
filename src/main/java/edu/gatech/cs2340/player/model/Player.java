package edu.gatech.cs2340.player.model;

public class Player {
    private static int count;
    private int id;
    private String name;
    
    public Player(String name) {
        this.count++;
        this.id = this.count;
        this.name = name;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
}
