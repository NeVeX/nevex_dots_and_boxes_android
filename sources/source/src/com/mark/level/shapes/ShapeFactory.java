package com.mark.level.shapes;

import java.util.ArrayList;

//import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;

import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.board.Board;
import com.mark.board.Edge;
import com.mark.game.GameScores;
import com.mark.game.GameBoardSettings;
import com.mark.logging.MyLog;

public class ShapeFactory {

	public BoxToken[][] boxBoundaries;
	public MyShapeDrawable[][][] boardShapes;
	public ArrayList<MyShapeDrawable> currentShapes = new ArrayList<MyShapeDrawable>();
	private ArrayList<MyCircleShape> circleTokens = new ArrayList<MyCircleShape>();
	
	public ArrayList<MyCircleShape> getCircleTokens() {
			return circleTokens;
	}
//
//	public void setCircleTokens(ArrayList<MyCircleShape> circleTokens) {
//			this.circleTokens = circleTokens;
//	}
//	
//	public void addCircleToken(MyCircleShape c)
//	{
//		this.circleTokens.add(c);
//	}
	
	public void hideCircleTokenByCoOrds(int col, int row)
	{
//		int removeMeInt = -1;
		for (int i = 0; i < this.circleTokens.size(); i++)
		{
			MyCircleShape myC = this.circleTokens.get(i);
			if ( myC.getBoardLocationCol() == col
					&& myC.getBoardLocationRow() == row)
			{
//				removeMeInt = i;
				myC.setVisible(false, false);
				GameScores.DeductScoreForPlayer(myC.playerOwner);
			}
		}
//		if ( removeMeInt != -1 )
//		{
//			this.circleTokens.remove(removeMeInt);
//		}
	}

	

	public ArrayList<MyShapeDrawable> fillerShapes = new ArrayList<MyShapeDrawable>();
	private Board board;
	
	private int debugCirclesDrawn, debugLinesDrawn;
//	public ArrayList<Drawable> bitmaps = new ArrayList<Drawable>();
	
	public ShapeFactory(Board b)
	{
		this.board = b;
		debugCirclesDrawn = debugLinesDrawn = 0;
	}
	
	public void DrawBoard()
	{
		this.drawRowsAndColumns();
		this.drawGameBorder();
	}

	private void drawRowsAndColumns() {
		int prevX, prevY;
		prevX = prevY = determineStartCoOrds();

		int currentRow = 0;
		int currentCol = 0;
		
		boardShapes = new MyShapeDrawable[GameBoardSettings.boardColumns][GameBoardSettings.boardRows][4];
		boxBoundaries = new BoxToken[GameBoardSettings.boardColumns][GameBoardSettings.boardRows];

		for ( int rows = 0; rows < GameBoardSettings.boardRows; rows++)
		{
			createOneRowOfHorizontalShapes(prevX, prevY, currentRow, currentCol,rows, false);

			prevY = createOneRowOfVerticalShapes(prevX, prevY, currentRow, currentCol);
			prevX = determineStartCoOrds();
			
			currentCol = 0;
			// Check if we are in the last row
			if ( rows == GameBoardSettings.boardRows - 1)
			{
				createOneRowOfHorizontalShapes(prevX, prevY, currentRow, currentCol, rows, true);
			}
			currentRow++;
		}
		
	}

	private int createOneRowOfVerticalShapes(int x, int y, int currentRow, int currentCol) {

		for (int c = 0; c <= GameBoardSettings.boardColumns; c++)
		{
			x = this.shapeCreatorForColumns(GameBoardSettings.boardColumns, x, y, currentRow, currentCol, c);
			currentCol++;
		}
		return y + GameBoardSettings.lineWidth + GameBoardSettings.lineGap;
	}

	private void createOneRowOfHorizontalShapes(int x, int y, int currentRow, int currentCol, int rows, boolean lastRow) {
		for (int i = 0; i < GameBoardSettings.boardColumns; i++)
		{ 
			x = this.shapeCreatorForRows(x, y, currentRow, currentCol, rows, i, lastRow );
			currentCol++;
		}
		MyLog.D(this, "width of board from drawing: "+x+", width from calc "+GameBoardSettings.myCanvasWidth);
	}

	private int determineStartCoOrds() {
		return GameBoardSettings.boardBorderWidth + GameBoardSettings.borderGapToBoard;
	}


	private int shapeCreatorForColumns(int boardColumns, int x, int y, int currentRow, int currentCol, int c) {
		MyShapeDrawable newDrawableLine;
		Rect newRect = new Rect(
						x, 
						y + GameBoardSettings.lineGap, 
						x + GameBoardSettings.lineHeight, 
						y + GameBoardSettings.lineWidth + GameBoardSettings.lineGap
						);
		if ( c == boardColumns ) 
		{
			// last column is also the previous column
			newDrawableLine = this.createRectShape(newRect, currentCol - 1, currentRow, Edge.EAST);
			boardShapes[currentCol - 1][currentRow][Edge.EAST] = newDrawableLine;
		}
		else 
		{
			newDrawableLine = this.createRectShape(newRect, currentCol, currentRow, Edge.WEST);
			boardShapes[currentCol][currentRow][Edge.WEST] = newDrawableLine;
			if ( currentCol > 0 ) {
				boardShapes[currentCol - 1][currentRow][Edge.EAST] = newDrawableLine;
			}
		}
		newDrawableLine.isHorizontal = false;
		return newDrawableLine.getBounds().right + GameBoardSettings.lineWidth;
		
	}

	private int shapeCreatorForRows(int x, int y, int currentRow, int currentCol, int rows, int i, boolean isLastRow) {
		if ( currentCol == 0 )
		{
			// in first column, so draw the first circle
			this.createAndAddNewLayoutBoardCircle(x, y);
		}

		int rightSideOfLine = x + GameBoardSettings.lineWidth + GameBoardSettings.lineGap;
		Rect newRect = new Rect(
					x + GameBoardSettings.lineGap, 
					y, 
					rightSideOfLine, 
					y + GameBoardSettings.lineHeight
					);
		MyShapeDrawable newDrawableLine = this.createRectShape(newRect, currentCol, currentRow, Edge.NORTH);
		if (isLastRow)
		{
			MyLog.D(this, "Adding shapes to last row");
			newDrawableLine.setMyEdge(currentCol, currentRow, Edge.SOUTH);
			boardShapes[currentCol][currentRow][Edge.SOUTH] = newDrawableLine;
		}
		else
		{
			boardShapes[currentCol][currentRow][Edge.NORTH] = newDrawableLine;
			if ( currentRow > 0 ) {
				boardShapes[currentCol][currentRow - 1][Edge.SOUTH] = newDrawableLine;
			}
		}
		// draw the circle after the line
		this.createAndAddNewLayoutBoardCircle(rightSideOfLine, y);
		addGameBoxDefinitions(rows, i, newDrawableLine, isLastRow);
		return rightSideOfLine;
	}

	private void addGameBoxDefinitions(int rows, int i, MyShapeDrawable newDrawableLine, boolean isLastRow) {
		Rect boxBounds = new Rect();
		boxBounds.top = newDrawableLine.getBounds().bottom;
		boxBounds.left = newDrawableLine.getBounds().left;
		boxBounds.right = newDrawableLine.getBounds().right;
		boxBounds.bottom = boxBounds.top + GameBoardSettings.lineWidth;
		
		if ( isLastRow )
		{
			boxBounds.top = newDrawableLine.getBounds().top - GameBoardSettings.lineWidth;
			boxBounds.bottom = newDrawableLine.getBounds().top;
		}
		boxBounds.left += GameBoardSettings.CircleTokenBoxGap;
		boxBounds.right -= GameBoardSettings.CircleTokenBoxGap;
		boxBounds.top += GameBoardSettings.CircleTokenBoxGap;
		boxBounds.bottom -= GameBoardSettings.CircleTokenBoxGap;
		
		BoxToken bt = new BoxToken(boxBounds, i, rows);

		bt.tokenRect = boxBounds;
		this.boxBoundaries[i][rows] = bt;
		this.circleTokens.add(bt.getCircle());
//		this.bitmaps.add(bt.getCircle().winnerToken);
		MyLog.D(this, "["+i+"]["+rows+"] -- "+boxBounds.left+","+boxBounds.top+","+boxBounds.right+","+boxBounds.bottom);
	}
	
	private void createAndAddNewLayoutBoardCircle(int prevX, int prevY) {
		MyShapeDrawable circle = new MyShapeDrawable(new OvalShape());
		circle.setBounds(
				prevX + GameBoardSettings.getCircleStartBounds(), 
				prevY + GameBoardSettings.getCircleStartBounds(), 
				prevX + GameBoardSettings.getCircleEndBounds(), 
				prevY + GameBoardSettings.getCircleEndBounds() 
				);
		circle.originalColor = Colors.WHITE;
		circle.getPaint().setColor(circle.originalColor);
		
		fillerShapes.add(circle);
		debugCirclesDrawn++;
	}
	
	private void drawGameBorder() {
		MyShapeDrawable borderTop = new MyShapeDrawable(new RectShape());
		borderTop.setBounds(0, 0, (int)GameBoardSettings.myCanvasWidth, GameBoardSettings.boardBorderWidth);

		MyShapeDrawable borderBottom = new MyShapeDrawable(new RectShape());
		borderBottom.setBounds(0, (int)GameBoardSettings.myCanvasHeight - GameBoardSettings.boardBorderWidth, (int)GameBoardSettings.myCanvasWidth, (int)GameBoardSettings.myCanvasHeight);

		MyShapeDrawable borderLeft = new MyShapeDrawable(new RectShape());
		borderLeft.setBounds(0, 0, GameBoardSettings.boardBorderWidth, (int)GameBoardSettings.myCanvasHeight - GameBoardSettings.boardBorderWidth);

		MyShapeDrawable borderRight = new MyShapeDrawable(new RectShape());
		borderRight.setBounds((int)GameBoardSettings.myCanvasWidth - GameBoardSettings.boardBorderWidth, 0, (int)GameBoardSettings.myCanvasWidth , (int)GameBoardSettings.myCanvasHeight);

		borderTop.originalColor = Colors.GameBorderColor;
		borderBottom.originalColor = Colors.GameBorderColor;
		borderLeft.originalColor = Colors.GameBorderColor;
		borderRight.originalColor = Colors.GameBorderColor;
		
		borderTop.getPaint().setColor(borderTop.originalColor);
		borderBottom.getPaint().setColor(borderBottom.originalColor);
		borderLeft.getPaint().setColor(borderLeft.originalColor);
		borderRight.getPaint().setColor(borderRight.originalColor);
		

		
		fillerShapes.add(borderLeft);
		fillerShapes.add(borderTop);
		fillerShapes.add(borderRight);
		fillerShapes.add(borderBottom);
	}
	
	private MyShapeDrawable createRectShape(Rect bounds, int col, int row, int edge)
	{
		MyShapeDrawable drawableLine;
		drawableLine = new MyShapeDrawable(new RectShape());
		drawableLine.getPaint().setColor(Colors.TRANSPARENT);
		drawableLine.setEdge(row, col, edge, board);
		drawableLine.setBounds(bounds);
		MyLog.D(this, "adding new shape for : row "+drawableLine.myEdge.getRow()+", col "+drawableLine.myEdge.getCol()+" , edge "+drawableLine.myEdge.getEdge());
		currentShapes.add(drawableLine);
		debugLinesDrawn++;
		return drawableLine;
	}
	

	
}
