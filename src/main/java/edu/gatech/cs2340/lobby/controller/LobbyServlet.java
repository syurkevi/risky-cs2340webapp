package edu.gatech.cs2340.lobby.controller;

import java.io.IOException;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.lobby.model.Lobby;

@WebServlet(urlPatterns = { "/list", // GET
        "/create", // POST
        "/update/*", // PUT
        "/delete/*" // DELETE
})
public class LobbyServlet extends HttpServlet {

    Lobby lobby;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Handle the hidden HTML form field that simulates
        // HTTP PUT and DELETE methods.
        String operation = (String) request.getParameter("operation");
        // If form didn't contain an operation field and
        // we're in doPost(), the operation is POST
        if (null == operation) {
            operation = "POST";
        }
        
        if (operation.equalsIgnoreCase("PUT")) {
            doPut(request, response);
            
        } else if (operation.equalsIgnoreCase("DELETE")) {
            doDelete(request, response);
            
        } else {
            String title = request.getParameter("title");
            this.lobby = new Lobby(title);
            request.setAttribute("lobby", lobby);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Called when HTTP method is GET (e.g., from an <a href="...">...</a>
     * link).
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("lobby", lobby);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String title = (String) request.getParameter("title");
        int id = getId(request);
        //todos.put(id, new Lobby(title));
        request.setAttribute("lobby", lobby);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
        dispatcher.forward(request, response);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = getId(request);
        //todos.remove(id);
        request.setAttribute("lobby", lobby);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/lobby.jsp");
        dispatcher.forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String uri = request.getPathInfo();
        // Strip off the leading slash, e.g. "/2" becomes "2"
        String idStr = uri.substring(1, uri.length());
        return Integer.parseInt(idStr);
    }

}
