package com.mark.game;

import java.util.ArrayList;

import com.mark.activity.GameActivity;
import com.mark.board.Board;
import com.mark.players.ai.AIEngineInterface;
import com.mark.players.ai.Viper;
import com.mark.players.ai.Random;
import com.mark.players.ai.NeVeX;

public class GamePlayers {

	public AIEngineInterface[] allPlayers;
	public static int NumberOfPlayers;
//	public static int PlayerOneColor = Color.RED;
//	public static int PlayerTwoColor = Color.BLUE;
//	public static int PlayerThreeColor = Color.GREEN;
//	public static int PlayerFourColor = Color.MAGENTA;
	
	boolean isAIPlayerPlaying = false;
	boolean isHumanPlaying = false;
//	public AIEngine playerTwo;
	
	
//	public static BitmapDrawable PlayerOneWinnerTokenBitmap = new BitmapDrawable(MyGameView.getCONTEXT().getResources(), 
//			BitmapFactory.decodeResource(MyGameView.getCONTEXT().getResources(), R.drawable.player_1));
//	public static BitmapDrawable PlayerTwoWinnerTokenBitmap = new BitmapDrawable(MyGameView.getCONTEXT().getResources(), 
//			BitmapFactory.decodeResource(MyGameView.getCONTEXT().getResources(), R.drawable.player_2));
	private GamePlayers() { }
	
	public GamePlayers(Board b) {
	
		ArrayList<String> realPlayers = DetermineRealPlayers();
		NumberOfPlayers = realPlayers.size();
		allPlayers = new AIEngineInterface[NumberOfPlayers];
//		this.players = new Players();
		
		for (int i = 0; i < realPlayers.size(); i++)
		{
			String player = realPlayers.get(i);
	    	if ( player.contains(Viper.NAME))
			{
	    		isAIPlayerPlaying = true;
	    		allPlayers[i] = new Viper(b);
			}
	    	else if ( player.contains(NeVeX.NAME))
			{
	    		isAIPlayerPlaying = true;
	    		allPlayers[i] = new NeVeX(b);
			}
	    	else if ( player.contains(Random.NAME))
			{	
	    		isAIPlayerPlaying = true;
	    		allPlayers[i] = new Random(b);
			}
	    	else 
	    	{
	    		isHumanPlaying = true;
	    		allPlayers[i] = null;
	    	}
		}
	}

	public static ArrayList<String> DetermineRealPlayers() {
		ArrayList<String> playersToReturn = new ArrayList<String>();
		
		for (String s : GameActivity.players)
		{
			if ( !s.equals("None") )
			{
				playersToReturn.add(s);
			}
		}
		return playersToReturn;
	}

	
	
}
