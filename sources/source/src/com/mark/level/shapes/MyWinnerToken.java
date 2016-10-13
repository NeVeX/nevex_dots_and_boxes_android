//package com.mark.level.shapes;
//
//import com.mark.game.GameSettings;
//
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//public class MyWinnerToken extends BitmapDrawable {
//
//	int minWidth, maxWidth, translateJump;
//	int direction = -1;
//	public Rect originalRect;
//	public int playerOwner = -1;
//	
//	long previousWait = 0;
//	
//	private int boardLocCol;
//	private int boardLocRow;
//	public int originalColor;
//	public MyWinnerToken() {
//		super();
//		
//		
//	}
//
//	
//	public void setBounds(Rect r)
//	{
//		super.setBounds(r);
//		minWidth = 10;
//		maxWidth = r.right - r.left;
//		translateJump = (int) (((r.right - r.left) / 2) * 0.1);
//	}
//	
//	
//	public boolean animateCircle()
//	{
//		if ( !GameSettings.animate) 
//		{ 
//			return false;
//		}
//		Rect b = getBounds();
//		int width = b.right - b.left;
//		
//		
//		if ( width <= minWidth)
//		{
//			direction = 1;
//
//		}
//		else if ( width >= maxWidth )
//		{
//			direction = -1;
//		}
//		
////		
////		if ( SystemClock.elapsedRealtime() - previousWait > BoardSettings.circleAnimUpdateWait )
////		{
//			// scale the bounds
//			this.setBounds(
//					b.left - ( translateJump * direction), 
//					b.top - ( translateJump * direction), 
//					b.right + ( translateJump * direction), 
//					b.bottom + ( translateJump * direction)
//					);
////			previousWait = SystemClock.elapsedRealtime();
////			if (GameSettings.SEIZURE_MODE)
////			{
////				// flip the colors
////				flipColors();
////			}
////			else
////			{
////				this.getPaint().setColor(this.originalColor);
////			}
////		}
//		
//		return true;
//		
//	}
//
////	private void flipTheColors() {
////		Paint c = this.getPaint();
////		if ( c.getColor() == Color.BLUE)
////		{
////			c.setColor(Color.RED);
////		}
////		else if ( c.getColor() == Color.RED)
////		{
////			c.setColor(Color.BLUE);
////		}
////	}
//	
//	private void flipColors() {
////		int r = new Random().nextInt(3);
////		int[] colors = new int[]{Color.RED, Color.BLUE, Color.YELLOW};
////		Paint p = d.getPaint();
////		if ( p.getColor() == Color.RED)
////		{
////			c.drawColor(Color.YELLOW);
////			p.setColor(Color.BLUE);
////		}
////		else if ( p.getColor() == Color.BLUE)
////		{
////			c.drawColor(Color.RED);
////			p.setColor(Color.YELLOW);
////		}
////		else if (p.getColor() == Color.YELLOW)
////		{
////			c.drawColor(Color.BLUE);
////			p.setColor(Color.RED);
////		}
////		this.getPaint().setColor(colors[r]);
//		
//	}
//
//
//	public void setBoardLocation(int col, int row) {
//		this.boardLocCol = col;
//		this.boardLocRow = row;
//		
//	}
//	
//	public int getBoardLocationCol()
//	{
//		return this.boardLocCol;
//		
//	}
//	public int getBoardLocationRow()
//	{
//		return this.boardLocRow;
//		
//	}
//
//
//	@Override
//	public void draw(Canvas canvas) {
//		
//		
//	}
//
//
//	@Override
//	public int getOpacity() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//
//	@Override
//	public void setAlpha(int alpha) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void setColorFilter(ColorFilter cf) {
//		// TODO Auto-generated method stub
//		
//	}
//}
