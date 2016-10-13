package com.mark.players.ai;

import com.mark.board.Board;

public class AIEngineBase {
	
	Board board;
	int row;
	int column;
	
	public AIEngineBase(Board b)
	{
		   board = b;
		   row = board.getSizeY();
		   column = board.getSizeX();
	}

}
