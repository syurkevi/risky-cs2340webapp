package edu.gatech.cs2340.risky.controllers.api;

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
import edu.gatech.cs2340.risky.models.*;
import java.io.PrintWriter;
import com.google.gson.Gson;

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
        Player player = (Player) getPayloadObject(Player.class);
        System.out.println(new Gson().toJson(player));
        //int playerId = playerDb.create(player);
        //dispatch(playerId);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Integer playerId = getId(request);
            dispatch(playerDb.get(playerId));
        } catch (Exception e) {
            dispatch(playerDb.getAll());
        }
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Integer playerId = getId(request);
        Player player = playerDb.get(playerId);
        
        String name = (String) request.getParameter("name");
        Integer armies = Integer.parseInt(request.getParameter("armies"));
        Boolean isAlive = new Boolean(request.getParameter("is_alive"));
        
        if (name != null) {
            player.name = name;
        } else {
            dispatch(new Exception("Player requires name"));
        }
        if (armies != null) {
            player.armies = armies;
        }
        if (isAlive != null) {
            player.playing = isAlive;
        }
        
        dispatch(player);
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int playerId = getId(request);
            Player p = this.playerDb.delete(playerId);
            dispatch(p);
        } catch (Exception e) {
            dispatch(e);
        }
    }
    
}
