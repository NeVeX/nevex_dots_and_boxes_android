package com.mark.level.shapes;


import com.mark.R;
import com.mark.ui.MyGameView;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class BoxToken {
	

	public Rect tokenRect;
	public int player = -1;
	private MyCircleShape circle;
	
	public MyCircleShape getCircle() {
		return circle;
	}

	public BoxToken(Rect boxBounds, int i, int rows) {
		tokenRect = boxBounds;
		addCircleToken(i, rows);
	}

	private void addCircleToken(int col, int row)
	{
		this.circle = new MyCircleShape(new OvalShape(), tokenRect, col, row);
	}
	
}
