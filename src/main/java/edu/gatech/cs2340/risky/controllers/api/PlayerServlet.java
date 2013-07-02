package edu.gatech.cs2340.risky.controllers.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.Player;

// POST / create
// GET / read
// PUT / update
// DELETE / delete

@WebServlet(urlPatterns = {
    "/api/player",
    "/api/player/*"
})
public class PlayerServlet extends ApiServlet {
    
    ModelDb<Player> playerDb;
    
    @Override
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) {
        playerDb = this.<Player>getDb(request, Player.class);
        return true;
    }
    
    protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {// R1
        Player player = (Player) getPayloadObject(request, Player.class);System.out.println("payload received and parsed");
        System.out.print("player payload: ");
        System.out.print((player == null) ? "" : "not ");
        System.out.println("null");
        Collection<Player> players = playerDb.query();System.out.println("db.query finished");
        if (players.size() >= 6) {System.out.println("too many players, so calling error");
            error(response, "Too many players");System.out.println("call to error() finished");
            return;
        }System.out.println("not too many players: " + players.size());
        
        System.out.println("got name " + player.name);
        
        for (Player opponent : players) {
            System.out.print("checking against " + opponent.name);
            if (player.name.equalsIgnoreCase(opponent.name)) {
                System.out.println(": same");
                error(response, "Invalid player: Cannot have same name");
                return;
            } else {
                System.out.println(": different");
            }
        }System.out.println("finished checking player names");
        
        playerDb.create(player);System.out.println("created player in db");
        dispatch(response, player);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Integer playerId = getId(request);
            dispatch(response, playerDb.get(playerId));
        } catch (Exception e) {
            dispatch(response, playerDb.query());
        }
    }
    
    protected synchronized void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer playerId = getId(request);
        Player givenPlayer = (Player) getPayloadObject(request, Player.class);
        Player player = playerDb.get(playerId);
        
        try {
            player.populateValidWith(givenPlayer);
            dispatch(response, player);
        } catch (Exception e) {
            error(response, "Failed to update player", e);
        }
    }
    
    protected synchronized void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int playerId = getId(request);
        Player p = this.playerDb.delete(playerId);
        System.out.println("Player id: " + playerId);
        System.out.println((p == null) ? "null player" : ("playerName: " + p.name));
        dispatch(response, p);
    }
    
}
