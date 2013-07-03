package edu.gatech.cs2340.risky.models;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonElement;

import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.Model;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.factories.MapFactory;

public class Lobby extends Model {
    
    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 6;
    
    public String title;
    public ArrayList<Object> players;
    public TurnOrder turnOrder;
    public Object mapId;
    
    public Lobby(Object id) {
        this(id, "Default Lobby");
    }
    
    public Lobby(Object id, String title) {
        this.id = id;
        this.title = title;
        this.turnOrder = new TurnOrder(this.id);
        this.mapId = MapFactory.get(0);
        this.players = new ArrayList<Object>();
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<Player>();
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        for (Object playerId : this.players) {
            players.add(playerDb.read(playerId));
        }
        return players;
    }
    
    public void addPlayer() {
        
    }

    public String getTitle() {
        return title;
    }
    
    public boolean isReadyToPlay() {
        return this.hasEnoughPlayers() && !this.hasTooManyPlayers() && this.mapId != null;
    }
    
    public boolean hasEnoughPlayers() {
        return this.players.size() > MIN_PLAYERS;
    }
    
    public boolean hasTooManyPlayers() {
        return this.players.size() > MAX_PLAYERS;
    }
    
    public void allocateArmies() {
        for (Player player : this.getPlayers()) {
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
    
    public static Lobby getInstance(Object id) {
        Lobby lobby = new Lobby(id);
        return lobby;
    }
    
}
