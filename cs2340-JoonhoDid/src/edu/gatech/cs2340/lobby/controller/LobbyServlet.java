package edu.gatech.cs2340.lobby.controller;

import edu.gatech.cs2340.lobby.model.GameLobby;
import java.io.IOException;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns={
        "/lobby", // GET
        "/lobby/create", // POST 
        "/lobby/update/*", // PUT
        "/lobby/delete/*" // DELETE
    })
public class LobbyServlet extends HttpServlet {
	
	
	TreeMap<Integer, GameLobby> gamelobbies = new TreeMap<>();
	

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.getParameter("operation");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		
	}	
	
    private int getId(HttpServletRequest request) {
        String uri = request.getPathInfo();
        String idStr = uri.substring(1, uri.length());// Strip off the leading slash, e.g. "/2" becomes "2"
        return Integer.parseInt(idStr);
    }
	

}

