package edu.gatech.cs2340.risky.controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.risky.Database;
import edu.gatech.cs2340.risky.RiskyServlet;
import edu.gatech.cs2340.risky.database.HashMapDbImpl;
import edu.gatech.cs2340.risky.models.Lobby;

@WebServlet(urlPatterns = {
    "/lobby", // GET
    "/lobby/", // GET
    "/lobby/*" // GET
})
public class LobbyServlet extends RiskyServlet {
    
    @Override
    protected boolean preDo(HttpServletRequest request, HttpServletResponse response) {
        Lobby lobby = Database.getModel(Lobby.class, this.getSessionId(request), new HashMapDbImpl<Lobby>());
        if (lobby == null) {
            lobby = new Lobby(this.getSessionId(request));
            Database.setModel(lobby);
        }
        return true;
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
        dispatcher.forward(request, response);
    }

}