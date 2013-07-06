package edu.gatech.cs2340.risky.controllers.api;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.models.Lobby;
import edu.gatech.cs2340.risky.models.Map;

@WebServlet(urlPatterns = {
    "/api/map"
})
public class MapServlet extends ApiServlet {
    
    public Object read(HttpServletRequest request) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request));
        Object map = null;
        if (lobby != null) {
            map = Database.getModel(Map.class, lobby.mapId);
        }
        return map;
    }
    
}
