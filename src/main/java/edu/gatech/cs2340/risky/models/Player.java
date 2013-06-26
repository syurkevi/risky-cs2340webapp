package edu.gatech.cs2340.risky.model;

import edu.gatech.cs2340.risky.Model;

public class Player extends Model {
    
    public String name;
    public int armies;
    public boolean playing = true;

    public Player(String name, int armies) {
        this.name = name;
        this.armies = armies;
    }
    
    public Player(String name) {
        this(name, 0);
    }
    
    public void setDead() {
        playing = false;
    }

    public boolean stillAlive() {
        return playing;
    }
}
