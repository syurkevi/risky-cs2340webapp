package edu.gatech.cs2340.risky.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.gatech.cs2340.risky.model.Lobby;
import edu.gatech.cs2340.risky.model.Player;
import edu.gatech.cs2340.risky.RiskyServlet;

@WebServlet(urlPatterns = {
    "/lobby", // GET
    "/lobby/" // GET
})
public class LobbyServlet extends RiskyServlet {

    Lobby lobby = null;

    /**
     * Called when HTTP method is GET (e.g., from an <a href="...">...</a>
     * link).
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (lobby == null) {
            lobby = new Lobby("Risky Lobby");
        }
        request.setAttribute("lobby", lobby);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
        dispatcher.forward(request, response);
    }

}
