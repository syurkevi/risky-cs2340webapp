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
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Player;

@WebServlet(urlPatterns = {
    "/api/turnOrder",
    "/api/turnOrder/*"
})
public class TurnOrderServlet extends ApiServlet {
    
    @Override
    protected void read(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        if (lobby == null) {
            error(response, "No lobby to use");
            return;
        }
        
        dispatch(response, lobby.turnOrder);
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
    
}
