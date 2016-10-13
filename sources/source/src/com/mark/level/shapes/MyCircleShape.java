package com.mark.level.shapes;

import java.util.Random;

import com.mark.R;
import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.game.GamePlayers;
import com.mark.game.GameBoardSettings;
import com.mark.ui.MyGameView;

import android.graphics.BitmapFactory;
//import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class MyCircleShape extends ShapeDrawable {

	int minWidth, maxWidth, translateJump;
	int direction = -1;
	public Rect originalRect;
	public int playerOwner = -1;
//	public Drawable winnerToken;
	long previousWait = 0;
	
	private int boardLocCol;
	private int boardLocRow;
	public int originalColor;
	public MyCircleShape(OvalShape circleShape, Rect tokenRect, int col, int row) {
		super(circleShape);
		this.setBounds(tokenRect);
		this.setBoardLocation(col, row);
		this.setVisible(false, false);

//		winnerToken.setBoardLocation(col, row);
	}

	
	public void setBounds(Rect r)
	{
		super.setBounds(r);
		minWidth = 10;
		maxWidth = r.right - r.left;
		translateJump = (int) (((r.right - r.left) / 2) * 0.1);
	}
	
	
	public boolean animateCircle()
	{	
		if ( !ApplicationSettings.ANIMATE) 
		{ 
			return false;
		}
		Rect b = getBounds();
		int width = b.right - b.left;
		
		
		if ( width <= minWidth)
		{
			direction = 1;

		}
		else if ( width >= maxWidth )
		{
			direction = -1;
		}
		
//		
//		if ( SystemClock.elapsedRealtime() - previousWait > BoardSettings.circleAnimUpdateWait )
//		{
			// scale the bounds
			this.setBounds(
					b.left - ( translateJump * direction), 
					b.top - ( translateJump * direction), 
					b.right + ( translateJump * direction), 
					b.bottom + ( translateJump * direction)
					);
			
			
			
//			winnerToken.setBounds(this.getBounds());
			
			
			
//			previousWait = SystemClock.elapsedRealtime();

//		}
		
		return true;
		
	}
	
	public void colorCirlce()
	{
		if (ApplicationSettings.SEIZURE_MODE)
		{
			// flip the colors
			flipColors();
		}
		else
		{
			this.getPaint().setColor(this.originalColor);
		}
	}
	
	private void flipColors() {
		int r = new Random().nextInt(3);
		int[] colors = new int[]{Colors.RED, Colors.BLUE, Colors.YELLOW};
		this.getPaint().setColor(colors[r]);
		
	}


	public void setBoardLocation(int col, int row) {
		this.boardLocCol = col;
		this.boardLocRow = row;
		
	}
	
	public int getBoardLocationCol()
	{
		return this.boardLocCol;
		
	}
	public int getBoardLocationRow()
	{
		return this.boardLocRow;
		
	}


	public void MakePlayerOwner(int player, int colorForPlayer) {
		this.setVisible(true, false);
		this.playerOwner = player;
		this.originalColor = colorForPlayer;
		this.getPaint().setColor(colorForPlayer);
//		if ( player == 0)
//		{
//			winnerToken = new BitmapDrawable(MyGameView.getCONTEXT().getResources(), 
//					BitmapFactory.decodeResource(MyGameView.getCONTEXT().getResources(), R.drawable.triforce));
//		}
//		else if ( player == 1)
//		{
//			winnerToken = new BitmapDrawable(MyGameView.getCONTEXT().getResources(), 
//					BitmapFactory.decodeResource(MyGameView.getCONTEXT().getResources(), R.drawable.boob));
//			
//		}
//		winnerToken.setBounds(this.getBounds());
//		winnerToken.setVisible(true, false);
		
	}
}
