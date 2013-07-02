package edu.gatech.cs2340.risky.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.RiskyServlet;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Map;
import edu.gatech.cs2340.risky.models.Player;
import edu.gatech.cs2340.risky.models.factories.MapFactory;

@WebServlet(urlPatterns = {
    "/game", // GET
    "/game/", // GET
    "/game/start" // GET
})
public class GameServlet extends RiskyServlet {
    
    Lobby lobby;
    Map map;
    
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        lobby = this.<Lobby>getModel(request, Lobby.class);
        if (lobby == null) {
            this.lobby = new Lobby();
            
            ModelDb<Player> playerDb = this.<Player>getDb(request, Player.class);
            Collection<Player> players = playerDb.query();
            if (players.size() < lobby.MIN_PLAYERS || players.size() > lobby.MAX_PLAYERS) {
                response.sendRedirect("/risky/lobby/");
                return false;
            }
            this.lobby.players.addAll(playerDb.query());// load the players
            playerDb.empty();
            
            this.setModel(request, this.lobby);
        }
        map = this.<Map>getModel(request, Map.class);
        if (map == null) {
            map = MapFactory.get(0);
            this.setModel(request, this.map);
        }
        return true;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = getAction(request);
        
        if (action.equalsIgnoreCase("start")) {
            startMatch(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/game.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    protected void startMatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.lobby = new Lobby();
        
        ModelDb<Player> playerDb = this.<Player>getDb(request, Player.class);
        this.lobby.players.addAll(playerDb.query());// load the players
        playerDb.empty();
        
        this.setModel(request, this.lobby);
        
        lobby.randomizeTurnOrder();// R2
        lobby.allocateArmies();// R3
        response.sendRedirect("/risky/game");
    }

}