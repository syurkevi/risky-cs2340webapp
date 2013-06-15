package edu.gatech.cs2340.game.model.order;

import java.util.*;

import edu.gatech.cs2340.player.model.Player;

public class TurnOrder {

	private static Random rand = new Random();
	
	private static List<Player> playerOrder;
	
	public static List<Player> randomize(TreeMap<Integer,Player> playerMap)
	{
		playerOrder = new ArrayList<>();
		for(Player p : playerMap.values())
			playerOrder.add(p);
		
		int swapPos;
		Player temp;
		for(int i = 0; i < playerMap.size(); i++)
		{
			swapPos = rand.nextInt(playerMap.size());
			
			/*
			 * temp = swapPos
			 * swapPos = playeri
			 * playeri = temp
			 */
			temp = playerOrder.get(swapPos);
			playerOrder.set(swapPos, playerOrder.get(i));
			playerOrder.set(i, temp);
		}
		return playerOrder;
	}

}
