package edu.gatech.cs2340.risky.models;

import java.util.ArrayList;
import java.util.Collections;

import edu.gatech.cs2340.risky.Model;

public class Lobby extends Model {

    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 6;
    
    public String title;
    public ArrayList<Player> players = new ArrayList<Player>();
    
    public Lobby() {
        this("Default Lobby");
    }
    
    public Lobby(String title) {
        this.title = title;
    }

    public ArrayList<Player> getPlayers() {
        return players; 
    }

    public String getTitle() {
        return title;
    }
    
    public boolean hasEnoughPlayers() {
        return this.players.size() > MIN_PLAYERS;
    }
    
    public boolean hasTooManyPlayers() {
        return this.players.size() > MAX_PLAYERS;
    }
    
    public void randomizeTurnOrder() {
        Collections.shuffle(this.players);
    }
    
    public void allocateArmies() {
        for (Player player : this.players) {
            player.armies = this.calculateArmies(this.players.size());
        }
    }
    
    public int calculateArmies(int numPlayers) {
        switch (numPlayers) {
        case 3:
            return 35;
        case 4:
            return 30;
        case 5:
            return 25;
        case 6:
            return 20;
        }
        return 0;
    }
    
    public void populateValidWith(Lobby l) {
        this.title = l.title;
    }
}
