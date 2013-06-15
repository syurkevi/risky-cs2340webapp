package edu.gatech.cs2340.game.model;

import java.util.*;

import edu.gatech.cs2340.game.model.order.TurnOrder;
import edu.gatech.cs2340.lobby.model.GameLobby;
import edu.gatech.cs2340.player.model.Player;

public class Game {
	private GameLobby lobby;
	private List<Player> turnOrder;
	private static int gameTurnNum;
	
	public Game(GameLobby lobby)
	{
		this.lobby = lobby;
		turnOrder = TurnOrder.randomize(lobby.getPlayers());
		gameTurnNum = 0;
	}
}
