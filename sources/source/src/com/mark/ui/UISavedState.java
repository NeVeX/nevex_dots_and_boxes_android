package com.mark.ui;

import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;

import com.mark.game.GameManager;

public class UISavedState {
    final int TICKS_PER_SECOND = 25;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;

	long touchDownTime;
	//These two constants specify the minimum and maximum zoom
	float MIN_ZOOM = 1.0f;
	float MAX_ZOOM = 5f;
	float SCALE_FACTOR = 1.0f;
	ScaleGestureDetector detector;
	//These constants specify the mode that we're in
	int NONE = 0;
	int DRAG = 1;
	int ZOOM = 2;
	int mode;
	//These two variables keep track of the X and Y coordinate of the finger when it first
	//touches the screen
	float startX = 0f;
	float startY = 0f;
	//These two variables keep track of the amount we need to translate the canvas along the X
	//and the Y coordinate
	float translateX = 0f;
	float translateY = 0f;
	//These two variables keep track of the amount we translated the X and Y coordinates, the last time we
	//panned.
	float previousTranslateX = 0f;
	float previousTranslateY = 0f;    
//	private boolean dragged = false;


	

	private boolean canvasHigherThanDisplay = false;
	/** Indicate whether the surface has been created & is ready to draw */
//    private boolean mRun = false;
	private boolean canvasWiderThanDisplay = false;
//	private boolean isTouching = false;
	private float[] touchedCoOrds = new float[4];
	private UIUpdateManager uiUpdateHandler;
}
