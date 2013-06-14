package edu.gatech.cs2340.risky.model;

public class Player {
    
    public String name;
    public int armies;

    public Player(String name, int armies) {
        this.name = name;
        this.armies = armies;
    }
    
    public Player(String name) {
        this(name, 0);
    }
}
