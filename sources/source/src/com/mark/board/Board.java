package com.mark.board;

import java.util.Observable;

import com.mark.game.GameManager;
import com.mark.game.GamePlayers;
import com.mark.game.GameScores;
import com.mark.level.shapes.MyShapeDrawable;
import com.mark.logging.MyLog;

public class Board extends Observable {
    private Box box[];
    private int noOfColums, noOfRows, edgesLeft;
    private int[] score;
    private GameManager gameManager;
	private int emptyBoardEdges;

    public int getEdgesLeft() {
        return edgesLeft;
    }

    public void setScoreSize(int size)
    {
    	score = new int[GamePlayers.NumberOfPlayers]; 
    }

    public Board(GameManager dl, int noOfColumns, int noOfRows) {
    	this.gameManager = dl;
    	score = new int[GamePlayers.NumberOfPlayers];
    	setSize(noOfColumns, noOfRows);
    }
    // returns false if player must go again, true otherwise 
    public boolean acceptMove(MyShapeDrawable shape, Edge edge, int player) {
		if ( !selectEdge(edge.getCol(), edge.getRow(), edge.getEdge()) ) 
		{
			MyLog.D(this, "could not accept move as the edge is already selected.");
		    return false;   // illegal move, player has to go again
		}
		
		// Now we know that an edge was added.
		setChanged();
		edgesLeft--;
		boolean ret = true; // Assume no box completed as default. 
		Box b = getBox(edge.getCol(), edge.getRow());
		if ( b.isComplete() ) {
		    b.setOwner(player);
		    score[player]++;
		    
		    this.gameManager.showTokenInBoxForWinningPlayer(edge.getCol(), edge.getRow(), player);
		    
		    GameScores.AddScoreForPlayer(player);
		    
		    
		    ret = false;
		    MyLog.D(this, "accepted move. player will go again since box is completed.");
		}
		b = getNeighbour(edge.getCol(), edge.getRow(), edge.getEdge());
		if ( b != null && b.isComplete() ) {
	        b.setOwner(player);
		    score[player]++;
		    int[] coOrds = this.getNeighbourCoOrds(edge.getCol(), edge.getRow(), edge.getEdge());
		    this.gameManager.showTokenInBoxForWinningPlayer(coOrds[0], coOrds[1], player);
		    
		    GameScores.AddScoreForPlayer(player);
		    
            ret = false;
            MyLog.D(this, "accepted move. player will go again since neighbour box is completed.");
		}
		notifyObservers();
		MyLog.D(this,  "Move is returning: "+ret);
		return ret;
    }
    // remote methods needed by players and views
    public boolean isFull()   {
    	return (edgesLeft == 0); 
    }
    
    public int[] getScore()   
    {
    	return score;
    }
    
    public int getOwner(int col, int row)   {
    	return getBox(col, row).getOwner();
    }
    
    public int getSizeX() { return noOfColums; }
    public int getSizeY() { return noOfRows; }
    
    public boolean isSelected(int col, int row, int side) {
    	return getBox(col, row).isPresent(side);
    }
    public String toString() {
		String ret = "";
		Box b;
		for (int row = 0; row < noOfRows; row++) {
		    for (int col = 0; col < noOfColums; col++) {
			b = getBox(col, row);
			ret += (b.isPresent(Edge.NORTH)) ? " -" : " .";
		    }
		    ret += "\n";
		    for (int col = 0; col < noOfColums; col++) {
			b = getBox(col, row);
			ret += (b.isPresent(Edge.WEST)) ? "|" : ".";
			ret += (b.isComplete()) ? b.getOwner() : " "; 
		    }
		    ret += (getBox(noOfColums - 1, row).isPresent(Edge.EAST)) ? "|" : ".";
		    ret += "\n";
		}
		for (int col = 0; col < noOfColums; col++) {
		    ret += (getBox(col, noOfRows - 1).isPresent(Edge.SOUTH)) ? " -" : " .";
		}
		ret += "\n";
		return ret;
    }
    // private helper methods
    private void setSize(int noOfColumns, int noOfRows) {
		this.noOfColums = noOfColumns;
		this.noOfRows = noOfRows;
		box = new Box[noOfColums * noOfRows];
//		Log.d("nevex", "set size of boxes on board to "+box.length);
		emptyBoardEdges = edgesLeft = noOfColums * (noOfRows + 1) + noOfRows * (noOfColums + 1);
		init();
    }
    
    public boolean[] unSelectEdge(int col, int row, int side) {
		Box myBox = getBox(col,row);
		boolean iAmCompleted = myBox.isComplete();
		if ( !myBox.unSelectEdge(side))
		{
			edgesLeft++;
		}
		
		Box neighbourBox = getNeighbour(col, row, side);
		boolean neighbourComplete = false;
		if ( neighbourBox != null ) {
			neighbourComplete = neighbourBox.isComplete();
			if ( neighbourBox.unSelectEdge(Edges.oppositeSide(side)))
			{
				edgesLeft++;
			}
			
			
		}
		
		return new boolean[]{iAmCompleted,neighbourComplete} ;
    }
    
    private boolean selectEdge(int col, int row, int side) {
		if ( !getBox(col, row).selectEdge(side) ) {
		    return false;
		}
		Box b = getNeighbour(col, row, side);
		
		if ( b != null ) {
		    if ( ! (b.selectEdge(Edges.oppositeSide(side))) )
			System.err.println("There is a problem with overlapping edges");
		}
		return true;
    }
    
    private void init() {
		for (int i = 0; i < box.length; i++) 
		    box[i] = new Box();
    }
    
    private Box getBox(int x, int y) {
//    	Log.d("nevex", "getting box for "+x+", "+y+". Returning box: "+(y * noOfColums + x));
    	return box[y * noOfColums + x];
    }
	     
    private boolean isLegal(int x, int y) {
    	return (x >= 0 && x < noOfColums && y >= 0 && y < noOfRows);
    }
    
    public int[] getNeighbourCoOrds(int x, int y, int side) {
		int edge;
		if (side == Edge.NORTH) 
		    y -= 1;
		else if (side == Edge.SOUTH) 
		    y += 1;
		else if (side == Edge.EAST) 
		    x += 1;
		else if (side == Edge.WEST) 
		    x -= 1;
		if (!isLegal(x,y)) {
			return null;
		}
		return new int[]{x, y};
    }
    
    public Box getNeighbour(int x, int y, int side) {
		int edge;
		if (side == Edge.NORTH) 
		    y -= 1;
		else if (side == Edge.SOUTH) 
		    y += 1;
		else if (side == Edge.EAST) 
		    x += 1;
		else if (side == Edge.WEST) 
		    x -= 1;
		if (!isLegal(x,y)) {
			return null;
		}
		return getBox(x, y);
    }
    
    public int[] getNeighbourEdgeAndCoOrds(int x, int y, int side) {
		if (side == Edge.NORTH) 
		    y -= 1;
		else if (side == Edge.SOUTH) 
		    y += 1;
		else if (side == Edge.EAST) 
		    x += 1;
		else if (side == Edge.WEST) 
		    x -= 1;
		if (!isLegal(x,y)) {
			return null;
		}
		MyLog.D(this, "returning neighbour box: "+x+", "+y+", "+Edges.oppositeSide(side));
		return new int[]{x, y, Edges.oppositeSide(side)};
    }



	public boolean isEmpty() {
		return emptyBoardEdges == edgesLeft;
	}
}
	    

