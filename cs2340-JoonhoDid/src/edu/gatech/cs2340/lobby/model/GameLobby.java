package edu.gatech.cs2340.lobby.model;

import java.util.*;
import edu.gatech.cs2340.player.model.Player;

public class GameLobby 
{
	private final int LOBBYCAP = 6;
	
	private static int gameLobbyNum;
	
	private int gamelobbyId;
	private int numPlayers;
	private TreeMap<Integer, Player> players = new TreeMap<>();
	
	public GameLobby()
	{
		gamelobbyId = gameLobbyNum++%1000;
		numPlayers = 0;
	}

	public void addPlayer(Player player)
	{
		if(numPlayers < LOBBYCAP)
			players.put(player.getPlayerId(), player);
		numPlayers++;
	}
	
	public void removePlayer(int id)
	{
		players.remove(id);
		numPlayers--;
	}
	
	public Player getPlayer(int id)
	{
		return players.get(id);
	}
	
	public boolean canStart()
	{
		return (numPlayers >= 3 && numPlayers <=6);
	}
	
	public TreeMap<Integer, Player> getPlayers()
	{
		return players;
	}
	
	public int getNumPlayers()
	{
		return numPlayers;
	}
}
