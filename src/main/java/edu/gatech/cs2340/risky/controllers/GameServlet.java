package edu.gatech.cs2340.risky.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.Database;
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
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = getFinalAction(request);
        if (action.equalsIgnoreCase("start")) {
            startMatch(request, response);
        } else {
            if (!this.requireLobby(request, response)) {
                return;
            }
            
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/game.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    protected void startMatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        
        this.requireLobby(request, response);
        
        ModelDb<Player> playerDb = Database.getDb(Player.class);
        for (Player candidate : playerDb.query()) {
            if (candidate.playing == false) {
                candidate.playing = true;
                lobby.players.add(candidate.id);
            }
        }
        
        if (!lobby.isReadyToPlay()) {
            response.sendRedirect("/risky/lobby/");
            return;
        }
        
        lobby.turnOrder.shuffleOrder();// R2
        lobby.allocateArmies();// R3
        response.sendRedirect("/risky/game");
    }
    
    protected boolean requireLobby(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        
        if (lobby == null) {
            response.sendRedirect("/risky/lobby");
            return false;
        }
        
        return true;
    }

}