package edu.gatech.cs2340.risky.controllers.api;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Player;

@WebServlet(urlPatterns = {
    "/api/lobby",
    "/api/lobby/*"
})
public class LobbyServlet extends ApiServlet {
    
    Lobby lobby;
    
    @Override
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) {
        lobby = this.<Lobby>getModel(request, Lobby.class);
        if (lobby == null) {
            this.lobby = new Lobby();
            
            ModelDb<Player> playerDb = this.<Player>getDb(request, Player.class);
            this.lobby.players.addAll(playerDb.query());// load the players
            playerDb.empty();
            
            this.setModel(request, this.lobby);
        }
        return true;
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        error(response, "Lobby must be created through /risky/lobby");
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        dispatch(response, lobby);
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Lobby givenLobby = (Lobby) getPayloadObject(request, Player.class);
        
        try {
            lobby.populateValidWith(givenLobby);
            dispatch(response, lobby);
        } catch (Exception e) {
            error(response, "Failed to update lobby", e);
        }
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.deleteModel(request, lobby);
        dispatch(response, new Object());
    }
    
}
