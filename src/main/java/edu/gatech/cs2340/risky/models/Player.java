package edu.gatech.cs2340.risky.models;

import java.awt.Color;
import java.util.HashMap;

import edu.gatech.cs2340.risky.Model;

public class Player extends Model {
    
    private static int[] colors = {Color.blue.getRGB(), Color.red.getRGB(), Color.green.getRGB(), Color.yellow.getRGB(), Color.black.getRGB(), Color.lightGray.getRGB()};
    private static int instanceCount = 0;
    
    public String name;
    public int armies;
    public int armiesAvailableThisTurn;
    public String color;// e.g. #00FF00
    public boolean playing;
    public HashMap<Object, TerritoryDeed> territories;
    public Object lobby;
    
    public Player() {
        this(null);
    }
    
    public Player(String name) {
        this(name, 0);
    }
    
    public Player(String name, int armies) {
        this.name = name;
        this.armies = armies;
        this.armiesAvailableThisTurn = this.armies;
        this.playing = false;
        this.id = instanceCount;
        this.color = String.format("#%06X", (0xFFFFFF & Player.colors[instanceCount%colors.length]));
        this.territories = new HashMap<Object, TerritoryDeed>();
        instanceCount++;
    }
    
    public void allocateArmies(int number) {
        this.armies += number;
        this.armiesAvailableThisTurn += number;
    }
    
    public int placeArmiesOnTerritory(int number, Object territoryId) {
        number = Math.min(number, this.armiesAvailableThisTurn);
        territories.get(territoryId).armies += number;
        return number;// number of armies actually attacked with
    }
    
    public BattleRecord attack(Object attackingTerritory, Object defendingTerritory, int attackingDie, int defendingDie) {
        Battle worldWarJava = new Battle(attackingTerritory, defendingTerritory, attackingDie, defendingDie);
        return worldWarJava.wage();
    }
    
    public void gainTerritory(Object territoryId, TerritoryDeed deed) {
        this.territories.put(territoryId, deed);
        // maybe return number of territories owned now?
        // or all the owned territories
    }
    
    public void fortifyTerritory(Object fromId, Object toId, int number) {
        // Territory from = Territories.get(from);
        // Territory to = Territories.get(to);
        // number = Math.min(from.armies-1, number);// require at least one army remain on Territory from
        // from.armies -= number;
        // to.armies += number;
        // return number;
    }
    
    public void setDead() {
        playing = false;
    }

    public boolean stillAlive() {
        return playing;
    }
    
    public void populateValidWith(Player p, boolean ignoreInvalidValues) {
        try {
            // should be a loop over matching the fields, but reflection is no fun
            this.name = p.name;
            this.armies = p.armies;
            this.playing = p.playing;
            
        } catch (Exception e) {
            if (!ignoreInvalidValues) {
                throw e;
            }
        }
    }
}
