package edu.gatech.cs2340.game.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.gatech.cs2340.player.model.Player;

@WebServlet(urlPatterns = {
		"/Game", // GET
		"/Game/create", //POST
		"/Game/update/", //PUT
		"/Game/delete/" //DELETE
	})
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
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
			
        }
	}
	
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	
	}
	
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {	
    	
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
    
    }
}
