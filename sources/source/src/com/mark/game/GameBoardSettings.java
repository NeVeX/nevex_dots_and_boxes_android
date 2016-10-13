package com.mark.game;

public class GameBoardSettings {
	
	
//	public static long circleAnimUpdateWait = 35;
	public static int lineWidth = 100;
	public static int lineHeight = 20;
	public static int boardBorderWidth = 10;
	public static int borderGapToBoard = 20;
	public static int lineGap = 20;
	public static int CircleTokenBoxGap = 10;
	private static int circleStartBounds = 2;
	private static int circleEndBounds = 18;
	public static int boardRows, boardColumns;
	public static float myCanvasWidth, myCanvasHeight;
	public static int boardWindowYOffsetTop;
	public static int boardWindowYOffsetBottom;
	public static long TouchDownTime = 500;
	public static int AllowedTouchError = 20;
	public static double TouchMoveDelta = 15;

	public static float scaledCanvasWidth, scaledCanvasHeight;
	
	public static int getCircleStartBounds() {
		return circleStartBounds;
	}
	

	public static int getCircleEndBounds() {
		return circleEndBounds;
	}


	
	public GameBoardSettings(float phoneDisplayWidth, float phoneDisplayHeight)
	{
//		circleAnimUpdateWait = 35;

		
		
		calculateDimensions();
		

		
		if ( phoneDisplayWidth < myCanvasWidth || phoneDisplayHeight < myCanvasHeight)
		{
			float diffW = myCanvasWidth / phoneDisplayWidth;
			float diffH = myCanvasHeight / phoneDisplayHeight;
			
			if ( diffH <= diffW )
			{
				// scale by the width difference
				scaleShapeDimensions(diffW, false);
			}
			else 
			{
				// scale by the height differnce
				scaleShapeDimensions(diffH, false);
			}
			calculateDimensions();
		}
		else if (phoneDisplayWidth > myCanvasWidth || phoneDisplayHeight > myCanvasHeight)
		{
			float diffW = phoneDisplayWidth / myCanvasWidth;
			float diffH = phoneDisplayHeight / myCanvasHeight;
			
			if ( diffH <= diffW )
			{
				// scale by the height difference
				scaleShapeDimensions(diffH, true);
			}
			else 
			{
				// scale by the width differnce
				scaleShapeDimensions(diffW, true);
			}
			calculateDimensions();
		}
		scaledCanvasWidth = myCanvasWidth;
		scaledCanvasHeight = myCanvasHeight;
	}
	
	private void scaleShapeDimensions(float scale, boolean shouldScaleUp) {
		lineWidth = myScale(lineWidth, scale, shouldScaleUp, 30);
		lineHeight = myScale(lineHeight, scale, shouldScaleUp, 3);
		boardBorderWidth = myScale(boardBorderWidth, scale, shouldScaleUp, 5);
		borderGapToBoard = myScale(borderGapToBoard, scale, shouldScaleUp, 5);
		lineGap = myScale(lineGap, scale, shouldScaleUp, 5); 
		circleStartBounds = myScale(circleStartBounds, scale, shouldScaleUp, 2);
		circleEndBounds = myScale(circleEndBounds, scale, shouldScaleUp, 4);
		CircleTokenBoxGap = myScale(CircleTokenBoxGap, scale, shouldScaleUp, 1);
		AllowedTouchError = myScale(AllowedTouchError, scale, shouldScaleUp, 0);
	}


	private int myScale(int m, float scale, boolean shouldScaleUp, int minimumSize) {
		if ( shouldScaleUp) {
			m = Math.round(m * scale);
		}
		else
		{
			m = Math.round(m / scale);
		}
		
		if ( m <= 1 )
		{
			m = 1;
		}
		return m;
	}


	private void calculateDimensions() {
		myCanvasWidth = (GameBoardSettings.boardColumns * GameBoardSettings.lineWidth) 
				+ ( (GameBoardSettings.boardColumns + 1) * GameBoardSettings.lineHeight)
				+ (GameBoardSettings.boardBorderWidth * 2) 
				+ ( borderGapToBoard * 2 );
		
		myCanvasHeight = (GameBoardSettings.boardRows * GameBoardSettings.lineWidth) 
				+ ( (GameBoardSettings.boardRows + 1) * GameBoardSettings.lineHeight) 
				+ ( GameBoardSettings.boardBorderWidth * 2) 
				+ ( borderGapToBoard * 2 );
	}
	
}
