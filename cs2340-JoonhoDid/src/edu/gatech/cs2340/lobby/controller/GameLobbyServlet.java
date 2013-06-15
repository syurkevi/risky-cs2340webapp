package edu.gatech.cs2340.lobby.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.lobby.model.GameLobby;
import edu.gatech.cs2340.player.model.Player;


@SuppressWarnings("serial")
@WebServlet(urlPatterns = {
		"/GameLobbyList", // GET
		"/GameLobbyList/create", //POST
		"/GameLobbyList/update/", //PUT
		"/GameLobbyList/delete/" //DELETE
	})
public class GameLobbyServlet extends HttpServlet {
	
	GameLobby gameLobby = new GameLobby();
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String operation = (String) request.getParameter("operation");
        if (null == operation) 
        {
            operation = "POST";
        }
        
        if (operation.equalsIgnoreCase("PUT")) 
        {
            doPut(request, response);   
        } 
        else if (operation.equalsIgnoreCase("DELETE")) 
        {
            doDelete(request, response);
        } 
		else 
        {
			String name = request.getParameter("newPlayer");
			if(name != "")
			{
				Player player = new Player(name);
				gameLobby.addPlayer(player);
			}
	        request.setAttribute("gameLobby", this.gameLobby);
	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GameLobbyList.jsp");
	        dispatcher.forward(request, response);
			
        }
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.setAttribute("gameLobby", gameLobby);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GameLobbyList.jsp");
		dispatcher.forward(request,response);
	}
	
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
    	String name = (String) request.getParameter("name");
    	Player player = new Player(name);
    	gameLobby.addPlayer(player);
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GameLobbyList.jsp");
    	dispatcher.forward(request, response);
    }
   
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
    	int playerId = Integer.parseInt(request.getParameter("removed"));
    	gameLobby.removePlayer(playerId);
    	request.setAttribute("gameLobby", gameLobby);
    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GameLobbyList.jsp");
    	dispatcher.forward(request,response);
    }
    
}
