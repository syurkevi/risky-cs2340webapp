package edu.gatech.cs2340.risky.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players = new ArrayList<Player>();
    private TurnManager turnManager = new TurnManager();
    private int territories;
    private int playerNum=0;

    public Game() {

    }

    public int players() {
        return playerNum;
    }

    public Player getNextPlayer() {
        return turnManager.getNextPlayer();
    }

    public void setTerritoryNum(int territories){
        this.territories=territories;
    }

    public void addPlayer(Player p) {
        players.add(p);
        turnManager.addPlayer(p); 
        playerNum++;
    }

    public boolean hasPlayers(){
        return (players.size()>0);
    }

    public int numofTerritories() {
        return territories;
    }

    private int calculateArmies(int numPlayers) {
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

    public void allocateArmies() {
        
        for (Player player : players) {
            //player.setTotalArmies();
        }
    } 
    
    public String getPlayerOrder() {
        return turnManager.playerOrder();
    }

    public void calculateTurnOrder(){
        turnManager.shuffleOrder();
    }
}
