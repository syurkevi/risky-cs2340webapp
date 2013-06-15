package edu.gatech.cs2340.risky.model;

import java.util.ArrayList;

public class Lobby {
    
    private String name;
    public ArrayList<Player> players = new ArrayList<Player>();
    
    public Lobby(String name) {
        this.name = name;
    }

    public ArrayList<Player> getPlayers(){
        return players; 
    }

    public String getName(){
        return name;
    } 
}
