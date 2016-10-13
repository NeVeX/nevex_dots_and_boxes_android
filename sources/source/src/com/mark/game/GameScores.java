package com.mark.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.Observable;
import java.util.TreeMap;

public class GameScores extends Observable {

	public static int PlayerOne = 0;
	public static int PlayerTwo = 0;
	public static int PlayerThree = 0;
	public static int PlayerFour = 0;
	
	public static void ResetScores() {
		PlayerOne = PlayerTwo = PlayerThree = PlayerFour = 0;
	}

	
	public static int DeterminePLayerWinner() {
		HashMap<Integer, Integer> winners = new HashMap<Integer, Integer>();
		List<Integer> mapKeys = new ArrayList<Integer>();
		mapKeys.add(GameScores.PlayerOne);
		mapKeys.add(GameScores.PlayerTwo);
		mapKeys.add(GameScores.PlayerThree);
		mapKeys.add(GameScores.PlayerFour);
		
		winners.put(mapKeys.get(0), 0);
		winners.put(mapKeys.get(1), 1);
		winners.put(mapKeys.get(2), 2);
		winners.put(mapKeys.get(3), 3);

		int winningIndex = 0;
		int winningScore = mapKeys.get(winningIndex);
		
		for ( int i = 1; i < mapKeys.size(); i++)
		{
			if ( mapKeys.get(i) > winningScore  )
			{
				 winningScore = mapKeys.get(i);
				 winningIndex = i;
			}
		}
		
		// check if we have a tie
		for ( int i = 1; i < mapKeys.size(); i++)
		{
			if ( mapKeys.get(i) == winningScore && i != winningIndex )
			{
				 return -1;
			}
		} 
		
		// we have a winner at this point, so return it
		return winners.get(winningScore);
	}


	private static LinkedHashMap<Integer, Integer> SortHashMapByValuesD(HashMap<Integer, Integer> passedMap) {
	    List<Integer> mapKeys = new ArrayList<Integer>(passedMap.keySet());
	    List<Integer> mapValues = new ArrayList<Integer>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);
	        
	    LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
	    
	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Integer val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();
	        
	        while (keyIt.hasNext()) {
	            Integer key = keyIt.next();
	            Integer comp1 = passedMap.get(key);
	            Integer comp2 = val;
	            
	            if (comp1.equals(comp2)){
	                passedMap.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put(key, val);
	                break;
	            }
	
	        }
	
	    }
	    return sortedMap;
	}

	public static void AddScoreForPlayer(int player) {
		if ( player == 0)
		{
			PlayerOne++;
		}
		else if ( player == 1 )
		{
			PlayerTwo++;
		}
		else if ( player == 2 )
		{
			PlayerThree++;
		}
		else if ( player == 3 )
		{
			PlayerFour++;
		}
	}
	
	public static void DeductScoreForPlayer(int playerOwner) {
		//deduct from the player score
		if ( playerOwner == 0 )
		{
			PlayerOne--;
		}
		else if ( playerOwner == 1)
		{
			PlayerTwo--;
		}
		else if ( playerOwner == 2)
		{
			PlayerThree--;
		}
		else if ( playerOwner == 3)
		{
			PlayerFour--;
		}
		
	}
	
}
