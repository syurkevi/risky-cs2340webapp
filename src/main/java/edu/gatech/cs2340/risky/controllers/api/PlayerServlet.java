package edu.gatech.cs2340.risky.controllers.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.database.ArrayListDbImpl;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.Player;

@WebServlet(urlPatterns = {
    "/api/player",
    "/api/player/*"
})
public class PlayerServlet extends ApiServlet {
    
    @Override
    protected synchronized void create(HttpServletRequest request, HttpServletResponse response) {// R1
        ModelDb<Player> playerDb = Database.getDb(Player.class, new ArrayListDbImpl<Player>());
        Player player = (Player) getPayloadObject(request, Player.class);
        
        Collection<Player> players = playerDb.query();
        if (players.size() >= 6) {
            error(response, "Too many players");
            return;
        }
        for (Player opponent : players) {
            if (player.name.equalsIgnoreCase(opponent.name)) {
                error(response, "Invalid player: Cannot have same name");
                return;
            }
        }
        
        players.add(player);
        dispatch(response, player);
    }
    
    @Override
    protected void read(HttpServletRequest request, HttpServletResponse response) {
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        
        try {
            Integer playerId = getId(request);
            dispatch(response, playerDb.read(playerId));
        } catch (Exception e) {
            Collection<Player> results = playerDb.query();
            results = this.filterResults(results, (String) request.getParameter("filter"));
            dispatch(response, results);
        }
    }
    
    protected synchronized Collection<Player> filterResults(Collection<Player> players, String filter) {
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
        }
        
        return filteredResult;
    }
    
    protected synchronized void update(HttpServletRequest request, HttpServletResponse response) {
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        
        Integer playerId = getId(request);
        Player givenPlayer = (Player) getPayloadObject(request, Player.class);
        Player player = playerDb.read(playerId);
        
        try {
            player.populateValidWith(givenPlayer);
            dispatch(response, player);
            return;
        } catch (Exception e) {
            error(response, "Failed to update player", e);
            return;
        }
    }
    
    protected synchronized void delete(HttpServletRequest request, HttpServletResponse response) {
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        int playerId = getId(request);
        Player p = playerDb.delete(playerId);
        dispatch(response, p);
    }
    
}
