package com.mark.app;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import android.app.Application;
import android.content.res.Resources;
import android.graphics.Color;

import com.mark.R;

public class Colors {

	public static int CurrentSelectedShapeColor;
	public static  int GameBorderColor;
	public static  int BLACK;
	public static  int YELLOW;
	public static  int RED;
	public static  int BLUE;
	public static  int GREEN;
	public static  int CYAN;
	public static  int MAGENTA;
	public static  int WHITE;
	public static  int TRANSPARENT;
	public static int ButtonCannotBeClicked;
	public static int ButtonCanBeClicked;
//	
	public static int PlayerOneColor;
	public static int PlayerTwoColor;
	public static int PlayerThreeColor;
	public static int PlayerFourColor;
	
//	public static  int CURRENT_SHAPE_COLOR = 0XFFF0EB6E;
//	public static  int BORDER_COLOR = 0xFFCCC9D1;
//	public static int COLOR_RED = 0xFFC92031;
//	public static int COLOR_GREEN = 0xFF76AB55;
//	
//	public static int PlayerOneColor = 0xFFDB184F;
//	public static int PlayerTwoColor = 0xFF1CB03A;
//	public static int PlayerThreeColor = 0xFFFA8E57;
//	public static int PlayerFourColor = 0xFF66D1C3;
	
	public Colors(Resources resources)
	{
		CurrentSelectedShapeColor = resources.getColor(R.color.Gold);
		GameBorderColor = resources.getColor(R.color.DarkGray);
		BLACK = resources.getColor(R.color.Black);
		YELLOW = resources.getColor(R.color.Yellow);
		RED = resources.getColor(R.color.Red);
		BLUE = resources.getColor(R.color.Blue);
		GREEN = resources.getColor(R.color.Green);
		CYAN = resources.getColor(R.color.Cyan);
		MAGENTA = resources.getColor(R.color.Magenta);
		WHITE = resources.getColor(R.color.White);
		TRANSPARENT = Color.TRANSPARENT;
		ButtonCannotBeClicked = resources.getColor(R.color.DarkRed);
		ButtonCanBeClicked = resources.getColor(R.color.DarkGreen);
	//	
		PlayerOneColor = resources.getColor(R.color.LimeGreen);
		PlayerTwoColor = resources.getColor(R.color.CornflowerBlue);
		PlayerThreeColor = resources.getColor(R.color.Crimson);
		PlayerFourColor = resources.getColor(R.color.Orchid);
	}
}
