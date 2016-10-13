package com.mark.level.shapes;

import com.mark.board.Board;
import com.mark.board.Edge;
import com.mark.logging.MyLog;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

public class MyShapeDrawable extends ShapeDrawable {
	
	public MyShapeDrawable(RectShape rectShape) {
		super(rectShape);

	}

	public Edge myEdge, neighbourEdge;
	public boolean alreadySelected = false;
	public int playerOwner;
	public boolean isHorizontal = true;
	public int originalColor;
	
	public void setEdge(int row, int col, int edge, Board b)
	{
		this.setMyEdge(col, row, edge);
		int[] box = b.getNeighbourEdgeAndCoOrds(col, row, edge);
		
		MyLog.D(this, "Edge for shape: "+myEdge.getCol()+", "+myEdge.getRow()+", "+myEdge.getEdge());
		if ( box != null ) 
		{
			neighbourEdge = new Edge(box[0], box[1], box[2]);
			MyLog.D(this, "Edge for shape: "+neighbourEdge.getCol()+", "+neighbourEdge.getRow()+", "+neighbourEdge.getEdge());
		}
		
		
	}
	
	public void setMyEdge(int currentCol, int currentRow, int edge) {
		myEdge = new Edge(currentCol, currentRow, edge);
		
	}	
	
	public Edge[] getEdges()
	{
		return null;
		
	}


}
