package edu.gatech.cs2340.risky.controllers.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Player;

@WebServlet(urlPatterns = {
    "/api/lobby",
    "/api/lobby/*"
})
public class LobbyServlet extends ApiServlet {
    
    @Override
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        if (lobby == null) {
            error(response, "Made new lobby");
            return false;
        }
        return true;
    }
    
    protected void create(HttpServletRequest request, HttpServletResponse response) {
        error(response, "Lobby must be created through /risky/lobby");
    }
    
    protected void read(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        dispatch(response, lobby);
    }
    
    protected void update(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        Lobby givenLobby = (Lobby) getPayloadObject(request, Player.class);
        
        try {
            lobby.populateValidWith(givenLobby);
            dispatch(response, lobby);
        } catch (Exception e) {
            error(response, "Failed to update lobby", e);
            return;
        }
    }
    
    protected void delete(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        dispatch(response, Database.delete(lobby));
    }
    
}
