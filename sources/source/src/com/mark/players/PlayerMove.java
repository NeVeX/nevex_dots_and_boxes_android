package com.mark.players;

import com.mark.app.ApplicationSettings;
import com.mark.board.Board;
import com.mark.board.Edge;
import com.mark.game.GameManager;
import com.mark.game.GamePlayers;
import com.mark.game.GameBoardSettings;
import com.mark.level.shapes.MyShapeDrawable;
import com.mark.logging.MyLog;
import com.mark.ui.MyGameView;

//import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class PlayerMove extends Thread {
	
		private GameManager gameM;
		private String player;
//		MotionEvent motionEvent;
		private float[] uiCoOrds;
		public static double[] PLAYERTIMES;
		private double currentTime;
		long startTime;
		
		public PlayerMove(GameManager gm, String player, float[] uiCoOrds)
		{
			MyLog.D(this, "Constructor");
			gameM = gm;
			this.uiCoOrds = uiCoOrds;
			this.player = player;
			currentTime = SystemClock.elapsedRealtime();
		}

		@Override
        public void run() {
			gameM.playerMoveComputing = true;
			MyLog.D(this, "BACKGROUND Method Entered");
			int gameStatus = 0;

			if (this.player.equals("Human"))
			{
				MyLog.D(this, "Getting Human to make next move");
				gameStatus = this.makePlayerMoveInUI(this.uiCoOrds);
				gameStatus = loopOverAITurn(gameStatus);
			}			
			else if ( this.player.equals("AI"))
			{
				gameStatus = loopOverAITurn(gameStatus);
			}
			gameM.playerMoveComputing = false;
			if ( gameStatus == 1 )
			{
				MyLog.D(this, "Game Status is 1 - Ending Game");
				gameM.gameOver();
			}
		}
		
		private int loopOverAITurn(int gameStatus) {
			
			if ( gameM.isNextPlayerTurnAnAIEngine() )
			{
				
				MyLog.D(this, "Getting AI to make next move");
				stateAIPlayerIsComputing();
				int status = 2;
				while ( status == 2 && canContinuePlaying())
				{
					stateAIPlayerIsComputing();
//					startTime = SystemClock.elapsedRealtime();
					putAIEngineToSleepIfNecessary(0);
					status = this.aiEngineToMakeOneMove();
					MyLog.D(this, "Forcing AI "+gameM.getPlayerName(gameM.playerToMakeMove)+" to make another move");
				}
				gameM.playerMoveComputing = false;
				return status;
			}
			return gameStatus;
		}

//		
//		private void changeToNextPlayerFromAI() {
//			putAIEngineToSleepIfNecessary(SystemClock.elapsedRealtime() - startTime);
//			gameM.changeToNextPlayer();
//		}
		
		private void putAIEngineToSleepIfNecessary(long elapsedTime) {
			// Pause for a moment, if necessary
			long pauseTime = ApplicationSettings.CURRENT_AI_MOVE_DELAY - elapsedTime;
			if ( pauseTime > 0 )
			{
				try {
					MyLog.D(this, "Sleeping AI thread for "+pauseTime+" ms....");
					Thread.sleep(pauseTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MyLog.D(this, "AI thread woken up after sleep");
			}
		}
		
		private void SendMessageToUI(String msg, int playerNo) {
			MyLog.D(this, "ProgressUpdate with text: "+msg);
			gameM.updateStatusGameTextFromGameManager(msg, playerNo);
		}
		


		public int makePlayerMoveInUI(float[] uiCoOrds) {

			int x = (int) uiCoOrds[0];
			int y = (int) uiCoOrds[1];
			
			MyLog.D(this, "Event Co-ords: "+x+", "+y);

			if ( !gameM.isNextPlayerTurnAnAIEngine() && gameM.b.getEdgesLeft() > 0 )
				for (MyShapeDrawable d : gameM.myShapeFactory.currentShapes)
				{
					if ( coOrdFitInsideShapeWithAllowedError(x, y, d) && !d.alreadySelected)
					{
						MyLog.D(this, "Found shape for Event Co-ords: "+x+", "+y);
						MyLog.D(this, "Shape with edge selected: "+d.myEdge.getCol()+", "+d.myEdge.getRow()+" ,"+d.myEdge.getEdge());

//						gameM.setLineColorForPlayer(d, gameM.playerToMakeMove);
						d.originalColor = gameM.getColorForPlayer(gameM.playerToMakeMove);
						d.getPaint().setColor(d.originalColor);
						
						gameM.updatePreviousAndCurrentShapes(d);
						
						if (gameM.b.acceptMove(d, d.myEdge, gameM.playerToMakeMove) )
						{
							if ( canContinuePlaying() )
							{
								gameM.changeToNextPlayer();
								if ( gameM.isNextPlayerTurnAnAIEngine() )
								{
//									this.stateAIPlayerIsComputing();
								}
								else 
								{
									// switch back to the human players
									this.stateNextPlayerIsToMove();
								}
							}
						}
						else
						{
							MyLog.D(this, gameM.getPlayerName(gameM.playerToMakeMove)+" gets to go again");
						}
						break;
					}
				}
			return determineReturnStatus();
		}

		private boolean coOrdFitInsideShapeWithAllowedError(int x, int y, MyShapeDrawable d) {	
			Rect r = new Rect( d.getBounds() );
//			int error = Math.round(GameSettings.AllowedTouchError / gameM.GetBoardScale());
			int error = GameBoardSettings.AllowedTouchError;
			if ( d.isHorizontal )
			{
				// add error to top and bottom
				r.top -= error;
				r.bottom += error;				
			}
			else
			{
				r.left -= error;
				r.right += error;
			}
			return r.contains(x, y);
		}

		private void stateAIPlayerIsComputing() {
			if ( ApplicationSettings.DEBUG_MODE)
			{
				PLAYERTIMES[gameM.playerToMakeMove] = (SystemClock.elapsedRealtime() - currentTime)/1000;
				currentTime = SystemClock.elapsedRealtime();
			}
			this.publishMessageToView(" Is Computing...");
		}

		private void stateNextPlayerIsToMove() {
			if ( ApplicationSettings.DEBUG_MODE)
			{
				PLAYERTIMES[gameM.getPreviousPlayerNumber()] = (SystemClock.elapsedRealtime() - currentTime)/1000;
				currentTime = SystemClock.elapsedRealtime();
			}
			this.publishMessageToView(" To Move");
		}
		
		private void publishMessageToView(String text)
		{
			MyLog.D(this, "Publishing to UI - "+text);
			this.SendMessageToUI(gameM.getPlayerName(gameM.playerToMakeMove) + text, gameM.playerToMakeMove);
			
		}

		public int aiEngineToMakeOneMove() {
			MyShapeDrawable d = null;
			Edge e = null;
			int playerNumber = gameM.playerToMakeMove;
			MyLog.D(this, gameM.getPlayerName(gameM.playerToMakeMove)+" is thinking");
			int tries = 0;
			while (tries < 5)
			{
				try 
				{
					tries++;
					MyLog.D(this, "AI ENGINE "+ gameM.players.allPlayers[playerNumber].getAIName()+" attempting to make move.");
					e = gameM.players.allPlayers[playerNumber].aiMakeMove();
					break;
				}
				catch ( Exception exception)
				{
					MyLog.D(this, "AI ENGINE "+ gameM.players.allPlayers[playerNumber].getAIName()+" FAILED. Retry number "+tries);
				}
			}
			
			if ( e == null && tries >= 5 )
			{
				gameM.updateStatusGameTextFromGameManager("AI Engine ["+gameM.players.allPlayers[playerNumber].getAIName()+"] has shutdown as it failed too many times to make a move. God Damn Bugs. Apologies.", 666);
				return 666;
			}
			
			d = gameM.findEdgeSelectedInShapes(e);
			MyLog.D(this, gameM.getPlayerName(gameM.playerToMakeMove)+" Engine picked: "+e.getCol()+", "+e.getRow()+", "+e.getEdge());
			if ( !gameM.b.acceptMove(d, e, gameM.playerToMakeMove) && gameM.b.getEdgesLeft() > 0 )
			{
				MyLog.D(this, "Board did not accept move OR AI can make another move: by  "+gameM.getPlayerName(gameM.playerToMakeMove)+". Player to move again");
//				stateAIPlayerIsComputing();
				return 2; //forceAIEngineMakeAnotherMove();
			}
			else 
			{
				MyLog.D(this, "Board accepted above move from "+gameM.getPlayerName(gameM.playerToMakeMove));
				MyLog.D(this, gameM.getPlayerName(gameM.playerToMakeMove)+" move over");
				
				if ( canContinuePlaying() )
				{
//					changeToNextPlayerFromAI();
					gameM.changeToNextPlayer();
					
					if ( gameM.isNextPlayerTurnAnAIEngine() )
					{
						return 2;//forceAIEngineMakeAnotherMove();
					}
					this.stateNextPlayerIsToMove();
				}
			}
			return determineReturnStatus();
		}

		private boolean canContinuePlaying() {
			return (determineReturnStatus() == 0 && !ApplicationSettings.IS_BOARD_GAME_OVER
					&& ApplicationSettings.GAME_THREAD_RUNNING);
		}

//		private void forceAIEngineMakeAnotherMove() {
//			if ( canContinuePlaying() )
//			{
//				MyLog.D(this, "Forcing AI "+gameM.getPlayerName(gameM.playerToMakeMove)+" to make another move");
//				this.stateNextPlayerIsToMove();
//				gameM.playerMoveComputing = false;
//				gameM.makeAIEngineMoveAsync();
//			}
//		}

		private int determineReturnStatus() {
			if ( gameM.b.getEdgesLeft() < 1 ) 
			{
				MyLog.D(this, "No Edges left. Returning 1 Status");
				return 1;
			}
			MyLog.D(this, "Edges still left. Returning 0 Status");
			return 0;
		}
}
