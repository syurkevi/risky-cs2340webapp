package edu.gatech.cs2340.risky.models;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import edu.gatech.cs2340.risky.Database;

public class TurnOrder {
    
    private String[] states = {"setup", "placearmies", "play", "gameover"};

    private String lobbyId;
    public int playerIndex = 0;
    public int round = 0;
    
    public String state = "setup";
    public int action = 0;
    
    public TurnOrder(Object lobbyId) {
        this.lobbyId = lobbyId.toString();
    }
    
    public void shuffleOrder() {
        Lobby lobby = Lobby.get(this.lobbyId);
        if (lobby == null) return;
        Collections.shuffle(lobby.players);
    }
    
    public void nextState() {
        int i=0;
        for ( ; i < this.states.length ; i++) {// find current state
            if (this.state.equalsIgnoreCase(this.states[i])) {
                break;
            }
        }
        i++;// increment it
        if (i >= this.states.length) {
            // yikes
        }
        this.playerIndex = 0;
        this.action = 0;
        this.state = this.states[i];// set it
    }
    
    public int nextAction() throws Exception {
        Lobby lobby = Lobby.get(this.lobbyId);
        if (lobby == null) throw new Exception("No lobby");
        Map map = Map.get(lobby.mapId);
        this.action++;
        
        if (this.state.equals("setup")) {
            this.action = 0;
            if (map.deeds.keySet().size() == map.territories.size()) {// all territories occupied
                this.nextState();
            } else {
                this.nextTurn();
            }
            
        } else if (this.state.equals("placearmies")) {
            this.action = 0;
            int sum = 0;
            
            for (Player player : lobby.getPlayers()) {
                sum += player.armiesAvailableThisTurn;
            }
            
            if (sum > 0) {// armies still left to place
                this.nextTurn();
                
            } else {// no armies left
                this.nextState();
            }
            
        } else if (this.state.equals("play")) {
            int winner = lobby.getWinner();
            if (winner >= 0) {
                this.playerIndex = winner;
                this.state = this.states[this.states.length-1];
                this.action = 0;
            }
            this.action %= 4;
            
        }
        
        return this.action;
    }
    
    public int nextTurn() throws Exception {
        Lobby lobby = Lobby.get(this.lobbyId);
        if (lobby == null) throw new Exception("No lobby");
        this.playerIndex++;
        if (this.playerIndex >= lobby.players.size()) {
            this.round++;
            this.playerIndex %= lobby.players.size(); 
        }
        return this.playerIndex;
    }
    
    public static TurnOrder get(HttpServletRequest request) {
        Lobby lobby = Lobby.get(request);
        if (lobby == null) {
            return null;
        }
        return lobby.turnOrder;
    }

}
