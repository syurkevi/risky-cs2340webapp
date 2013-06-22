package edu.gatech.cs2340.risky.model;
import java.util.HashMap;

public class Player {
    private String name;
    private int armies=0;
    private boolean playing = true;
    private HashMap<Integer,Integer> delegatedArmies;

    public Player(String name, int armies) {
        this.name = name;
        this.armies = armies;
    }
     
    public Player(String name) {
        this(name, 0);
    }
    
    public void setTotalArmies(int armies){
        this.armies=armies;
    }

    public void setDead() {
        playing = false;
    }

    public boolean stillAlive() {
        return playing;
    }
    
    public int armies(){
        return armies;
    }
    
    public String name(){
        return name;
    }
}
