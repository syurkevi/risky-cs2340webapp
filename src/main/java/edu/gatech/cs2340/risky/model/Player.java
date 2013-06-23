package edu.gatech.cs2340.risky.model;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;

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
    
    public int totalArmies(){
        int totalArmies=0;
        ArrayList<Integer> armyCount=(ArrayList<Integer>)delegatedArmies.values();
        for(Integer army : armyCount){
            totalArmies+=army;
        }
        return totalArmies;
    }
    public int[] ownsTerritories(){
        HashSet<Integer> ownedTerritoriesSet=(HashSet<Integer>)delegatedArmies.keySet();
        Iterator<Integer> it=ownedTerritoriesSet.iterator();
        int[] ownedTerritories=new int[ownedTerritoriesSet.size()];
        for(int i=0;i<ownedTerritoriesSet.size();++i){
            ownedTerritories[i]=it.next();
        }
        return ownedTerritories;
    }

    public int armiesAtTerritory(int territory){
        return (delegatedArmies.get(territory)==null)?0:delegatedArmies.get(territory);
    }
    public void setArmiesPerTerritory(int territory, int armies){
        delegatedArmies.put(territory,armies);
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
