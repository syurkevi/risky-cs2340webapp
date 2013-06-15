package edu.gatech.cs2340.player.model;

public class Player 
{
	private final int INITARMYSIZE = 10;
	
	private static int playerCount;
	private int playerId;
	private String playerName;
	private int armySize;
	
	public Player(String name)
	{
		playerName = name;
		playerId = playerCount++%1000;
		armySize = INITARMYSIZE;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public int getPlayerId()
	{
		return playerId;
	}
	
	public int getArmySize()
	{
		return armySize;
	}
	
	public void setArmySize(int size)
	{
		armySize = size;
	}
}
