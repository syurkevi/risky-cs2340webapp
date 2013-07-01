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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        playerDb = this.<Player>getDb(request, Player.class);
        super.service(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Player player = (Player) getPayloadObject(Player.class);System.out.println("payload received and parsed");
        System.out.print("player payload: ");
        System.out.print((player == null) ? "" : "not ");
        System.out.println("null");
        Collection<Player> players = playerDb.query();System.out.println("db.query finished");
        if (players.size() >= 6) {System.out.println("too many players, so calling error");
            error("Too many players");System.out.println("call to error() finished");
            return;
        }System.out.println("not too many players: " + players.size());
        
        System.out.println("got name " + player.name);
        
        for (Player opponent : players) {
            System.out.print("checking against " + opponent.name);
            if (player.name.equalsIgnoreCase(opponent.name)) {
                System.out.println(": same");
                error("Invalid player: Cannot have same name");
                return;
            } else {
                System.out.println(": different");
            }
        }System.out.println("finished checking player names");
        
        playerDb.create(player);System.out.println("created player in db");
        dispatch(player);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Integer playerId = getId(request);
            dispatch(playerDb.get(playerId));
        } catch (Exception e) {
            dispatch(playerDb.query());
        }
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer playerId = getId(request);
        Player givenPlayer = (Player) getPayloadObject(Player.class);
        Player player = playerDb.get(playerId);
        
        try {
            player.populateValidWith(givenPlayer);
            dispatch(player);
        } catch (Exception e) {
            error("Failed to update player", e);
        }
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int playerId = getId(request);
        Player p = this.playerDb.delete(playerId);
        System.out.println("Player id: " + playerId);
        System.out.println((p == null) ? "null player" : ("playerName: " + p.name));
        dispatch(p);
    }
    
}
