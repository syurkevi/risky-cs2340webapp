package edu.gatech.cs2340.risky.models;

import java.util.Collections;

import edu.gatech.cs2340.risky.Database;

public class TurnOrder {

    private Object lobbyId;
    private int playerIndex = 0;
    private int round = 0;
    
    public TurnOrder(Object lobbyId) {
        this.lobbyId = lobbyId;
    }
    
    public void shuffleOrder() {
        Lobby lobby = Database.getModel(Lobby.class, this.lobbyId);
        if (lobby == null) return;
        Collections.shuffle(lobby.players);
    }

    public int getRound() {
        return round;
    }
    
    public Player getPlayer() {
        Lobby lobby = Database.getModel(Lobby.class, this.lobbyId);
        if (lobby == null) return null;
        return Database.getModel(Player.class, lobby.players.get(this.playerIndex));
    }
    
    public int nextTurn() {
        Lobby lobby = Database.getModel(Lobby.class, this.lobbyId);
        if (lobby == null) return -1;
        this.playerIndex++;
        if (this.playerIndex >= lobby.players.size()) {
            this.round++;
            this.playerIndex %= lobby.players.size(); 
        }
        return this.playerIndex;
    }

}
