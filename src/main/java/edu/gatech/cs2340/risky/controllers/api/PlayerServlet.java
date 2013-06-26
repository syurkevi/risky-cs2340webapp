package edu.gatech.cs2340.risky.controller.api;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.gatech.cs2340.risky.ApiServlet;
import edu.gatech.cs2340.risky.database.HashMapDbImpl;
import edu.gatech.cs2340.risky.database.ModelDb;
import edu.gatech.cs2340.risky.model.Lobby;
import edu.gatech.cs2340.risky.model.Player;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {
    "/player/create", // POST
    "/player/read/*", // GET
    "/player/update/*", // PUT
    "/player/delete/*" // DELETE
})
public class PlayerServlet extends ApiServlet {
    
    ModelDb<Player> playerDb;
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        playerDb = this.<Player>getDb(request, Player.class);
        super.service(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = (String) request.getParameter("name");
        Player player = new Player(name);
        int playerId = playerDb.create(player);
        request.setAttribute("model", playerId);
        dispatch(request, response);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer playerId = getId(request);
        request.setAttribute("model", playerDb.get(playerId));
        dispatch(request, response);
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer playerId = getId(request);
        Player player = playerDb.get(playerId);
        
        String name = (String) request.getParameter("name");
        Integer armies = Integer.parseInt(request.getParameter("armies"));
        Boolean isAlive = new Boolean(request.getParameter("is_alive"));
        
        if (name != null) {
            player.name = name;
        }
        if (armies != null) {
            player.armies = armies;
        }
        if (isAlive != null) {
            player.playing = isAlive;
        }
        
        request.setAttribute("model", player);
        dispatch(request, response);
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int playerId = getId(request);
            Player p = this.playerDb.delete(playerId);
            request.setAttribute("model", p);
        } catch (Exception e) {
            request.setAttribute("model", e);
        }
        dispatch(request, response);
    }
    
}
