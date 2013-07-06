package edu.gatech.cs2340.risky.controllers.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.api.annotations.ApiParams;
import edu.gatech.cs2340.risky.database.ArrayListDbImpl;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.Battle;
import edu.gatech.cs2340.risky.models.BattleRecord;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Player;

@WebServlet(urlPatterns = {
    "/api/player",
    "/api/player/*"
})
public class PlayerServlet extends ApiServlet {
    
    public synchronized Object create(HttpServletRequest request) throws Exception {// R1
        ModelDb<Player> playerDb = Database.getDb(Player.class, new ArrayListDbImpl<Player>());
        Player player = (Player) getPayloadObject(request, Player.class);
        
        Collection<Player> players = playerDb.query();
        if (players.size() >= 6) {
            throw new Exception("Too many players");
        }
        for (Player opponent : players) {
            if (player.name.equalsIgnoreCase(opponent.name)) {
                throw new Exception("Invalid player: Cannot have same name");
            }
        }
        
        players.add(player);
        return player;
    }
    
    public Object read(HttpServletRequest request) {
        ModelDb<Player> playerDb = Database.getDb(Player.class, new ArrayListDbImpl<Player>());
        
        try {
            Integer playerId = getId(request);
            return playerDb.read(playerId);
            
        } catch (Exception e) {
            Collection<Player> results = playerDb.query();
            results = this.filterResults(results, (String) request.getParameter("filter"), (String) request.getParameter("arg"));
            return results;
        }
    }
    
    protected synchronized Collection<Player> filterResults(Collection<Player> players, String filter, String arg) {
        if (filter == null) {
            return players;
        }
        ArrayList<Player> filteredResult = new ArrayList<Player>();
        if ("isNotPlaying".equals(filter)) {
            for (Player player : players) {
                if (player.playing == false) {
                    filteredResult.add(player);
                }
            }
        } else if ("inLobby".equals(filter)) {
            Lobby lobby = Database.getModel(Lobby.class, arg);
            if (lobby != null) {
               filteredResult = lobby.getPlayers();
            }
        }
        
        return filteredResult;
    }
    
    public synchronized Object update(HttpServletRequest request) throws Exception {
        ModelDb<Player> playerDb = Database.getDb(Player.class, new ArrayListDbImpl<Player>());
        
        Integer playerId = getId(request);
        Player givenPlayer = (Player) getPayloadObject(request, Player.class);
        Player player = playerDb.read(playerId);
        
        try {
            player.populateValidWith(givenPlayer);
            return player;
            
        } catch (Exception e) {
            throw new Exception("Failed to update player");
        }
    }
    
    public synchronized Object delete(HttpServletRequest request) {
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        int playerId = getId(request);
        Player p = playerDb.delete(playerId);
        return p;
    }
    
    // example of what the /api/{model}/do/ calls will work like
    @ApiParams({"defendingPlayer", "attackingDie", "defendingDie"})
    public Object attack(Object defendingPlayer, int attackingDie, int defendingDie) {
        Battle worldWarJava = new Battle();
        BattleRecord results = worldWarJava.wage();
        return results;
    }

    @ApiParams({"one", "two"})
    public Object[] nothing(HttpServletRequest request, Object one, Object two) throws Exception {
        Object[] proof = new Object[3];
        proof[0] = 0;
        proof[1] = one;
        proof[2] = two;
        return proof;
    }
    
}
