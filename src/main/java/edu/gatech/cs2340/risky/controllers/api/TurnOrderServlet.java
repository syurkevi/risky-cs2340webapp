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
    public Object read(HttpServletRequest request) throws Exception {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        if (lobby == null) {
            throw new Exception("No lobby to use");
        }
        
        return lobby.turnOrder;
    }
    
    public synchronized Object update(HttpServletRequest request) throws Exception {
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        
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
    
}
