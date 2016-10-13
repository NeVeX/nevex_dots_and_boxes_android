package com.mark.game;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

//import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.board.Board;
import com.mark.board.Edge;
import com.mark.level.shapes.BoxToken;
import com.mark.level.shapes.MyCircleShape;
import com.mark.level.shapes.MyShapeDrawable;
import com.mark.level.shapes.ShapeFactory;
import com.mark.logging.MyLog;
import com.mark.players.PlayerMove;
import com.mark.ui.MyGameView;
import com.mark.ui.MyGameView.DrawLevel;
import com.mark.ui.UIUpdateManager;

public class GameManager {
	public GamePlayers players;
	
	private ArrayList<MyShapeDrawable> historicMoves = new ArrayList<MyShapeDrawable>();
	public ShapeFactory myShapeFactory;
	public int playerToMakeMove;
	public Board b;
	public boolean playerMoveComputing = false;
	private int winningPlayer = -1;
	private double gameTime;
	private UIUpdateManager uiUpdateMgr;
//	public DrawLevel dl;
//	private int firstPlayer;
//	private Handler mHandler;
	private void resetBoard() {
		GameScores.ResetScores();
		gameTime = SystemClock.elapsedRealtime();
		
	}
	
//	public float GetBoardScale()
//	{
//		return dl.SCALE_FACTOR;
//	}
	
	private void determineIfPlayersShouldReset() {
		if ( historicMoves.size() == 0 )
		{
			// reset the engines
			players = new GamePlayers(b);
		}
	}
	
	public GameManager(UIUpdateManager uiUpdateHandler)
	{
		uiUpdateMgr = uiUpdateHandler;
		this.resetBoard();
		b = new Board(this, GameBoardSettings.boardColumns, GameBoardSettings.boardRows);
		myShapeFactory = new ShapeFactory(b);
		myShapeFactory.DrawBoard();
		determineIfPlayersShouldReset();
		
		PlayerMove.PLAYERTIMES = new double[GamePlayers.NumberOfPlayers];
		this.playerToMakeMove = new Random().nextInt(GamePlayers.NumberOfPlayers);
		b.setScoreSize(GamePlayers.NumberOfPlayers);
//		determineNextPlayerAndMakeMove();
//		ApplicationSettings.IS_BOARD_GAME_OVER = false;
	}
	
	public int getWinningPlayer()
	{
		return winningPlayer;
	}
	
	public void gameOver() {
		String debugInfo = "";
		determineIfUndoAllowed();
		if ( ApplicationSettings.DEBUG_MODE)
		{
			DecimalFormat df = new DecimalFormat("#.##");
			debugInfo = " (" +df.format(((SystemClock.elapsedRealtime() - this.gameTime)/1000)/60) + "min)";
		}
		winningPlayer = GameScores.DeterminePLayerWinner();
		if ( winningPlayer >= 0 )
		{
			updateStatusGameTextFromGameManager(getPlayerName(winningPlayer)+" Won The Game"+debugInfo, winningPlayer);
		}
		else
		{
			updateStatusGameTextFromGameManager("The Game Was A Tie "+debugInfo, 0);
		}
		
//		try
//		{
//			// hack job! Sleep thread to let cycles finish drawing the end state
//			Thread.sleep(500);
//		}
//		catch(Exception e)
//		{
//			// do nothing...game is over..
//		}
		
//		ApplicationSettings.GAME_THREAD_RUNNING = false;
		ApplicationSettings.IS_BOARD_GAME_OVER = true;

	}

	public void determineNextPlayerAndMakeMove() {
		this.updateStatusGameTextFromGameManager(this.getPlayerName(this.playerToMakeMove) + " To Move", this.playerToMakeMove);
		if ( this.isNextPlayerTurnAnAIEngine())
		{
			this.makeAIEngineMoveAsync();
		}
		else 
		{
			// we wait on human input
		}
	}
	
	private void makePlayerMoveAsync(String name, float[] uiCoOrds)
	{
		if ( !this.playerMoveComputing && !ApplicationSettings.IS_BOARD_GAME_OVER )
		{
			new PlayerMove(this, name, uiCoOrds).start();
		}
	}
	
	public void makeAIEngineMoveAsync() {
		this.makePlayerMoveAsync("AI", null);
	}
	
	public void makeHumanMoveAsync(float[] uiCoOrds)
	{
		this.makePlayerMoveAsync("Human", uiCoOrds);
		
	}



	
	public boolean isNextPlayerTurnAnAIEngine() {
		return this.players.allPlayers[this.playerToMakeMove] != null;
	}

	public int getColorForPlayer(int player) {
		if ( player == 0 ) 
		{
			return Colors.PlayerOneColor;
		}
		else if ( player == 1 )
		{
			return Colors.PlayerTwoColor;
		}
		else if ( player == 2 )
		{
			return Colors.PlayerThreeColor;
		}
		else 
		{
			return Colors.PlayerFourColor;
		}
//		shape.getPaint().setColor(shape.originalColor);
	}

	public String getPlayerName(int player) {
		String returnString = "Player";
		
		if ( player == 0 ) { returnString += " One"; }
		else if ( player == 1 ) { returnString += " Two"; }
		else if ( player == 2 ) { returnString += " Three"; }
		else if ( player == 3 ) { returnString += " Four"; }
		
		returnString += " : ";
		if ( this.players.allPlayers[player] == null )
		{
			returnString += "Human";
		}
		else
		{
			returnString += this.players.allPlayers[player].getAIName();
		}
		returnString += " :";
		return returnString;
	}
	
	
	
	public void changeToNextPlayer() {
		if ( b.isFull() ) { return; }
		this.playerToMakeMove++;
		if ( this.playerToMakeMove >= this.players.allPlayers.length)
		{
			this.playerToMakeMove = 0;
		}
		
	}
	
	public int getPreviousPlayerNumber() {
//		if ( b.isEmpty() ) { return; }
		int prevPlayer = this.playerToMakeMove - 1;
		if ( prevPlayer < 0)
		{
			prevPlayer = this.players.allPlayers.length - 1;
		}
		return prevPlayer;
	}
	
	private void changeToPreviousPlayer() {
//		if ( b.isEmpty() ) { return; }
		this.playerToMakeMove--;
		if ( this.playerToMakeMove < 0)
		{
			this.playerToMakeMove = this.players.allPlayers.length - 1;
		}
	}

//	private void changeToFirstPlayer()
//	{
//		this.playerToMakeMove = this.firstPlayer;
//	}

	// only AI engines call this
	public MyShapeDrawable findEdgeSelectedInShapes(Edge e) {
		MyLog.D(this, "Getting drawable from following location in array: Col: "+e.getCol()+", Row: "+e.getRow()+", Edge: "+e.getEdge());
		MyShapeDrawable selectedDrawableEdge = myShapeFactory.boardShapes[e.getCol()][e.getRow()][e.getEdge()];
		MyLog.D(this, "Drawable gotten is : "+selectedDrawableEdge);
		updatePreviousAndCurrentShapes(selectedDrawableEdge);
		return selectedDrawableEdge;
	}



	private void updateVisualsToCurrentSelectedEdge(MyShapeDrawable selectedDrawableEdge) {
//		updatePreviousAndCurrentShapes(selectedDrawableEdge);
		
		selectedDrawableEdge.alreadySelected = true;
		if ( b.getEdgesLeft() > 1 )
		{
			selectedDrawableEdge.originalColor = Colors.CurrentSelectedShapeColor;
		}
		else
		{
			selectedDrawableEdge.originalColor = this.getColorForPlayer(selectedDrawableEdge.playerOwner);
		}
		selectedDrawableEdge.getPaint().setColor(selectedDrawableEdge.originalColor);
	}
	
	public void updatePreviousAndCurrentShapes(MyShapeDrawable pShape) {
		int historyLength = this.historicMoves.size();
		if ( historyLength > 0) 
		{
			MyShapeDrawable prevSelecetedShape = this.historicMoves.get(historyLength-1);
			prevSelecetedShape.originalColor = this.getColorForPlayer(prevSelecetedShape.playerOwner);
			prevSelecetedShape.getPaint().setColor(prevSelecetedShape.originalColor);
		}
		pShape.playerOwner = this.playerToMakeMove;
		updateVisualsToCurrentSelectedEdge(pShape);
		this.addTooHistoricMoves(pShape);
	}
	
	private void addTooHistoricMoves(MyShapeDrawable pShape)
	{
		this.historicMoves.add(pShape);
		this.determineIfUndoAllowed();
	}
	

	private void determineIfUndoAllowed() {
		if ( players.isHumanPlaying )
		{
			int undoCode = ApplicationSettings.NOT_ALLOWED_UNDO;
			if ( this.historicMoves.size() > 0 && !this.b.isFull())
			{
				undoCode = ApplicationSettings.ALLOWED_UNDO;
			}
			this.uiUpdateMgr.sendMessageForUI("undoButtonUpdate", undoCode);
		}
		
	}

	private MyShapeDrawable removeHistoricMoveAtElement(int i) {
		
		MyShapeDrawable ms = this.historicMoves.remove(i);
		this.determineIfUndoAllowed();
		return ms;
	}
	
	
	public void showTokenInBoxForWinningPlayer(int col, int row, int player)
	{
		BoxToken circleToken = this.myShapeFactory.boxBoundaries[col][row];
		MyCircleShape myC = circleToken.getCircle();
		myC.MakePlayerOwner(player, this.getColorForPlayer(player));

	}

	public void UndoMove() {
		// we can only perform this move on Human turn
		if ( !isNextPlayerTurnAnAIEngine() && this.historicMoves.size() > 0 && !this.playerMoveComputing)
		{
//			this.updateStatusGameTextFromGameManager("Performing UNDO Move", this.playerToMakeMove);
			// dont allow any more moves on this board while doing undo
			this.playerMoveComputing = true;
			int undoPLayer = this.playerToMakeMove; // this human player
			while ( true ) // we will break once the current player is changed or the history size is empty
			{
				
				// remove the last one
				MyShapeDrawable removeMe = this.removeHistoricMoveAtElement(this.historicMoves.size()-1);
				removeMe.alreadySelected = false;
				boolean[] completedBoxes = this.b.unSelectEdge(removeMe.myEdge.getCol(), removeMe.myEdge.getRow(), removeMe.myEdge.getEdge());
				if ( this.historicMoves.size() > 0 )
				{
					MyShapeDrawable myS = this.historicMoves.get(this.historicMoves.size()-1);
					// previous shape is made current
					this.updateVisualsToCurrentSelectedEdge(myS);
					// need to check if we must remove a box token
					checkAndRemoveBoxTokenForUndo(removeMe, completedBoxes);
					if ( undoPLayer == this.playerToMakeMove )
					{
						// player is back to his undo move, so break
						break;
					}
				}
				else 
				{
					// no more historic moves to act on
					this.changeToPreviousPlayer(); // change to first player
					break;
				}
			}
			this.playerMoveComputing = false; // remove control of the board
			// decide if to reset the ai engines
			determineIfPlayersShouldReset();
			// finally make player move
			this.determineNextPlayerAndMakeMove();
		
		}
	}




	private boolean checkAndRemoveBoxTokenForUndo(MyShapeDrawable myS, boolean[] completedBoxes) {
		boolean playerShouldRevert = true;
		
		for ( int i = 0; i < completedBoxes.length; i++)
		{
			if (completedBoxes[i] )
			{
				if ( i == 0)
				{
					this.myShapeFactory.hideCircleTokenByCoOrds(myS.myEdge.getCol(), myS.myEdge.getRow());
					
				}
				else if ( i == 1)
				{
					this.myShapeFactory.hideCircleTokenByCoOrds(myS.neighbourEdge.getCol(), myS.neighbourEdge.getRow());
				}

				playerShouldRevert = false;
			}
		}
		
		if ( playerShouldRevert )
		{
			this.changeToPreviousPlayer();
		}
		
		return playerShouldRevert;
		
	}

	public void updateStatusGameTextFromGameManager(String string, int playerNo) {
		
		uiUpdateMgr.sendMessageForUI(string, playerNo);
	}

//	public boolean canUndoAnotherMove() {
//		if ( this.players.isHumanPlaying)
//		{
//			// figure out if can undo move
//			return (this.historicMoves.size() > 0);
//		}
//		else 
//		{
//			return false;
//		}
//	}
	
}
