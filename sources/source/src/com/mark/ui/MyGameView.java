package com.mark.ui;


import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

import quicktime.std.image.Matrix;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mark.activity.GameActivity;
import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.board.Board;
import com.mark.game.GameManager;
import com.mark.game.GamePlayers;
import com.mark.game.GameBoardSettings;
import com.mark.game.GameScores;
import com.mark.level.shapes.MyCircleShape;
import com.mark.level.shapes.MyShapeDrawable;
import com.mark.logging.MyLog;
import com.mark.players.PlayerMove;
import com.mark.R;


public class MyGameView extends SurfaceView implements SurfaceHolder.Callback {
    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;
    
    /** The thread that actually draws the animation */
    private DrawLevel drawLevelThread;
    private boolean gameThreadStarted = false;
	private TextView playerOneTextView;
	private TextView playerTwoTextView;
	private TextView playerThreeTextView;
	private TextView playerFourTextView;
	DecimalFormat df = new DecimalFormat("#.##");
	private GameActivity gameActivity;

	private boolean surfaceAlreadyCreated = false;

	private Context context;
	
    public static final int STATE_PAUSE = 1;
    public static final int STATE_RUNNING = 0;
	
	public boolean onTouchEvent(MotionEvent event) {
    	return drawLevelThread.onTouchEvent(event);
		
    }
    
	public MyGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
			this.context = context;
			gameActivity = (GameActivity) context;
	        // register our interest in hearing about changes to our surface
	        SurfaceHolder holder = getHolder();
	        holder.addCallback(this);
	        
	        // create thread only; it's started in surfaceCreated()
	        drawLevelThread = createSurfaceViewThread(context, holder, null);

	        setFocusable(true); // make sure we get key events
	        
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			ApplicationSettings.GAME_CANVAS_DISPLAY_WIDTH = display.getWidth();
			ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT = display.getHeight();
			
	}

	private DrawLevel createSurfaceViewThread(Context context,
			SurfaceHolder holder, GameManager gm) {
		return new DrawLevel(holder, context, 
				new UIUpdateManager(new  Handler() {
		        @Override
		        public void handleMessage(Message m) {
		            updateStatusOfGame(m.getData().getString("StatusText"), m.getData().getInt("Player"));
		        }
		    }), gm);
	}

    public DrawLevel getDrawLevelThread() {
        return drawLevelThread;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {

        if (!hasWindowFocus) 
    	{
        	drawLevelThread.setRunning(false);
        	return;
    	}
        else {
        	// check thread
        	if ( drawLevelThread.getState() == Thread.State.TERMINATED )
        	{
        		this.resumeGameInNewThread();
        	}
        }
       
		
    }
    
	private void resumeGameInNewThread() {
		
		DrawLevel resumedThread = createSurfaceViewThread(context, getHolder(), drawLevelThread.gameManager);
		resumedThread.restoreStateFromPreviousUIView(drawLevelThread);
		drawLevelThread = resumedThread;
//		drawLevelThread.setRunning(!ApplicationSettings.IS_BOARD_GAME_OVER);
		drawLevelThread.setRunning(true);
		drawLevelThread.start();
		
	}

	protected void shutdown()
	{
		gameActivity.finish();
		//android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}
	
	private void updateStatusOfGame(String text, int id)
	{
		if ( id == 666 )
		{
			ApplicationSettings.GAME_THREAD_RUNNING = false;
			// ai engine has failed
			new AlertDialog.Builder(this.context)
		    .setTitle("Balls...A Serious Error")
		    .setMessage(text)
		    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	shutdown();
		        }
		     })
		     .show();
			return;
		}
		else if (text.equals("undoButtonUpdate"))
		{
			if ( id == ApplicationSettings.ALLOWED_UNDO)
			{
				gameActivity.decideHowToColorUndoButton(true);
			}
			else if ( id == ApplicationSettings.NOT_ALLOWED_UNDO)
			{
				gameActivity.decideHowToColorUndoButton(false);
			}
		}
		else if ( text.equals("StartGame") )
		{
			gameActivity.gameStartUIChanges(id);
		}
		else if ( text.equals("EndGame") )
		{
			gameActivity.gameEndUIChanges();
		}
		else
		{
			
			View v = getRootView();
			TextView tv = (TextView) v.findViewById(R.id.statusGameText);
			if ( id == 0)
			{
				tv.setTextColor(Colors.PlayerOneColor);
			}
			else if ( id == 1 )
			{
				tv.setTextColor(Colors.PlayerTwoColor);
			}
			else if ( id == 2 )
			{
				tv.setTextColor(Colors.PlayerThreeColor);
			}
			else if ( id == 3 )
			{
				tv.setTextColor(Colors.PlayerFourColor);
			}
			
			tv.setText(text);
			MyLog.D(this, "status update: "+this.drawLevelThread.gameManager);
			
			try 
			{
				// mysterious fucking error thrown in here somewhere once in a blue moon at startup. good luck finding it
				playerOneTextView.setText(this.drawLevelThread.gameManager.getPlayerName(0)+" Score - "+GameScores.PlayerOne+debugText(0));
				playerOneTextView.setTextColor(Colors.PlayerOneColor);
				playerTwoTextView.setText(this.drawLevelThread.gameManager.getPlayerName(1)+" Score - "+GameScores.PlayerTwo+debugText(1));
				playerTwoTextView.setTextColor(Colors.PlayerTwoColor);
				playerThreeTextView.setText(this.drawLevelThread.gameManager.getPlayerName(2)+" Score - "+GameScores.PlayerThree+debugText(2));
				playerThreeTextView.setTextColor(Colors.PlayerThreeColor);
				playerFourTextView.setText(this.drawLevelThread.gameManager.getPlayerName(3)+" Score - "+GameScores.PlayerFour+debugText(3));
				playerFourTextView.setTextColor(Colors.PlayerFourColor);
			}
			catch(Exception e)
			{
				// bs.
			}
		}
	}

	private String debugText(int player) {
		if ( ApplicationSettings.DEBUG_MODE && player <= GamePlayers.NumberOfPlayers)
		{
			return " ("+df.format(PlayerMove.PLAYERTIMES[player])+"s)";
		}
		else return "";
		
	}

	public String getInitialStatusText(int playerNo) {
		return this.drawLevelThread.gameManager.getPlayerName(playerNo)+" Make Your Move.";
	}

	public void surfaceCreated(SurfaceHolder arg0) {

		if ( drawLevelThread.getState() == Thread.State.NEW)
		{
			View v = getRootView();
			RelativeLayout llBottom = (RelativeLayout) v.findViewById(R.id.GameBoardBottomRibbon);
			// Get the location of the top axis including the android widgets too
			int[] myloc = new int[2];
			getLocationOnScreen(myloc);
			GameBoardSettings.boardWindowYOffsetTop = myloc[1];
			GameBoardSettings.boardWindowYOffsetBottom = llBottom.getHeight();
			ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT -= ( GameBoardSettings.boardWindowYOffsetBottom + GameBoardSettings.boardWindowYOffsetTop);
			new GameBoardSettings(ApplicationSettings.GAME_CANVAS_DISPLAY_WIDTH, ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT);
	        // start the thread here so that we don't busy-wait in run()
	        // waiting for the surface to be created
			ApplicationSettings.IS_BOARD_GAME_OVER = false;
			drawLevelThread.setRunning(true);
			drawLevelThread.start();
		}
		else if ( drawLevelThread.getState() == Thread.State.TERMINATED)
		{
			this.resumeGameInNewThread();
		}
		
		
		this.storeViewsToObjects();
		
		
	}
	
	private void storeViewsToObjects()
	{
		View v = (View) getRootView();
		playerOneTextView = (TextView) v.findViewById(R.id.scoresPlayerOne);
		playerTwoTextView = (TextView) v.findViewById(R.id.scoresPlayerTwo);
		playerThreeTextView = (TextView) v.findViewById(R.id.scoresPlayerThree);
		playerFourTextView = (TextView) v.findViewById(R.id.scoresPlayerFour);
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;

        drawLevelThread.setRunning(false);
        while (retry) {
            try {
            	drawLevelThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        MyLog.D(this, "surface view thread shutdown");
		
	}
	
	public class DrawLevel extends Thread {
	    private final int TICKS_PER_SECOND = 25;
	    private final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
	    private final int MAX_FRAMESKIP = 5;
		//These two constants specify the minimum and maximum zoom
		private final float MIN_ZOOM = 1.0f;
		private final float MAX_ZOOM = 5f;
		//These constants specify the mode that we're in
		private final int NONE = 0;
		private final int DRAG = 1;
		private final int ZOOM = 2;
		
		
		
		
		
		private ScaleGestureDetector detector;
		private int currentMode;
		private float SCALE_FACTOR = 1.0f;
		private long touchDownTime;
		//These two variables keep track of the X and Y coordinate of the finger when it first
		//touches the screen
		private float startX = 0f;
		private float startY = 0f;
		//These two variables keep track of the amount we need to translate the canvas along the X
		//and the Y coordinate
		private float translateX = 0f;
		private float translateY = 0f;
		//These two variables keep track of the amount we translated the X and Y coordinates, the last time we
		//panned.
		private float previousTranslateX = 0f;
		private float previousTranslateY = 0f;    
//		private boolean dragged = false;
		private boolean canvasHigherThanDisplay = false;
		private boolean canvasWiderThanDisplay = false;
		private float[] touchedCoOrds = new float[4];
		private UIUpdateManager uiUpdateHandler;
		
		
		private GameManager gameManager;
		private SurfaceHolder mSurfaceHolder;
		

		public DrawLevel(SurfaceHolder surfaceHolder, Context context, UIUpdateManager uiHandler, GameManager gm)
		{
			mSurfaceHolder = surfaceHolder;
            uiUpdateHandler = uiHandler;
            mContext = context;
            detector = new ScaleGestureDetector(getContext(), new ScaleListener());
           
			this.gameManager = gm;
		}
		
		public void restoreStateFromPreviousUIView(DrawLevel oldLevel)
		{
			SCALE_FACTOR = oldLevel.SCALE_FACTOR;
//			detector = oldLevel.detector;
			currentMode = oldLevel.currentMode;
			
			touchDownTime = oldLevel.touchDownTime;
			startX = oldLevel.startX;
			startY = oldLevel.startY;
			//These two variables keep track of the amount we need to translate the canvas along the X
			//and the Y coordinate
			translateX = oldLevel.translateX;
			translateY = oldLevel.translateY;
			//These two variables keep track of the amount we translated the X and Y coordinates, the last time we
			//panned.
			previousTranslateX = oldLevel.previousTranslateX;
			previousTranslateY = oldLevel.previousTranslateY;    
//			private boolean dragged = false;
			canvasHigherThanDisplay = oldLevel.canvasHigherThanDisplay;
			canvasWiderThanDisplay = oldLevel.canvasWiderThanDisplay;
			touchedCoOrds = oldLevel.touchedCoOrds;
//			uiUpdateHandler = oldLevel.uiUpdateHandler;
//			this.drawGameLevelOnCanvas();
		}
		
        public void setRunning(boolean b) {
            ApplicationSettings.GAME_THREAD_RUNNING = b;
        }
        
//        public boolean isRunning()
//        {
//        	return ApplicationSettings.GAME_THREAD_RUNNING;
//        }


		public boolean onTouchEvent(MotionEvent event) {
			MyLog.D(this, "New touch event registered");
//			this.isTouching = true;
			
			
			MyLog.D(this, "GetX:"+event.getX()+", GetY:"+event.getY()+", GetRawX:"+event.getRawX()+", GetRawY:"+event.getRawY());
			detector.onTouchEvent(event);
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			
				case MotionEvent.ACTION_DOWN:
					
//					MyLog.D(this, "Action Down. Previous Mode is :"+mode+", new mode is :"+DRAG);
					touchDownTime = SystemClock.elapsedRealtime();
	
//					mode = DRAG;
					currentMode = NONE;
					MyLog.D(this, "Mode set to NONE");
					//We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
					//amount for each coordinates This works even when we are translating the first time because the initial
					//values for these two variables is zero.
					startX = event.getRawX() - previousTranslateX;
					startY = event.getRawY() - previousTranslateY;
	
					break;
	
				case MotionEvent.ACTION_MOVE:
					
					translateX = event.getRawX() - startX;
					translateY = event.getRawY() - startY;
	
					
					MyLog.D(this, "Action Move. Previous Mode is :"+currentMode+", new mode is :"+DRAG);
					MyLog.D(this, "new translate ["+translateX+", "+translateY+"] - Event ["+event.getRawX()+", "+event.getRawY()+"] - StartPositions["+startX+", "+startY+"]");
					//We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
					//This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
					double distance = Math.sqrt(Math.pow(event.getRawX() - (startX + previousTranslateX), 2) +
							Math.pow(event.getRawY() - (startY + previousTranslateY), 2)
							);
	
					if(distance > GameBoardSettings.TouchMoveDelta) {
						currentMode = DRAG;
						MyLog.D(this, "Mode set to DRAG");
					}       
					else 
					{
						currentMode = NONE;
						MyLog.D(this, "Mode set to NONE");
					}

					break;
	
				case MotionEvent.ACTION_POINTER_DOWN:
					
					MyLog.D(this, "Action Pointer Down. Previous Mode is :"+currentMode+", new mode is :"+ZOOM);
					currentMode = ZOOM;
					MyLog.D(this, "Mode set to ZOOM");
					break;
	
				case MotionEvent.ACTION_UP:
					
					MyLog.D(this, "Action Up. Previous Mode is :"+currentMode+", new mode is :"+NONE);
					MyLog.D(this, "Touch down time: "+(SystemClock.elapsedRealtime() - touchDownTime)+" ,mode is "+currentMode);
					if (currentMode == NONE && SystemClock.elapsedRealtime() - touchDownTime < GameBoardSettings.TouchDownTime/* <= 500*/ )
					{
						MyLog.D(this, "Action Pointer Down - Player about to make a move - Passing event co-ords to game");

						
						float myEventX = (event.getRawX()/SCALE_FACTOR)+(translateX*-1);
						float myEventY = (event.getRawY() / SCALE_FACTOR) + (translateY*-1) - (GameBoardSettings.boardWindowYOffsetTop / SCALE_FACTOR);
//						event.offsetLocation((translateX*-1), (translateY*-1));
//						event.setLocation(, );
						// draw where touched
						touchedCoOrds[0] = myEventX;
						touchedCoOrds[1] = myEventY;
						touchedCoOrds[2] = event.getRawX();
						touchedCoOrds[3] = event.getRawY();
//						gameManager.makeHumanMoveAsync(event);
						gameManager.makeHumanMoveAsync(new float[]{myEventX, myEventY});
						
//						this.shouldScrollToLastMove = true;
					}
//					dragged = false;
					//All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and
					//previousTranslate
					previousTranslateX = translateX;
					previousTranslateY = translateY;
					
					currentMode = NONE;
					MyLog.D(this, "Mode set to NONE");
					break;
	
				case MotionEvent.ACTION_POINTER_UP:
					
					MyLog.D(this, "Action Pointer Up. Previous Mode is :"+currentMode+", new mode is :"+DRAG);
					currentMode = DRAG;
					MyLog.D(this, "Mode set to DRAG");
					//This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
					//and previousTranslateY when the second finger goes up
					previousTranslateX = translateX;
					previousTranslateY = translateY;
					break;

			}
			
			return true;
		}


		@Override
        public void run() {
			
        	// check if game is over
			if ( !ApplicationSettings.IS_BOARD_GAME_OVER )
			{
	        	if ( gameManager == null)
	        	{
	        		gameManager = new GameManager(uiUpdateHandler);
	        		uiUpdateHandler.sendMessageForUI(getInitialStatusText(gameManager.playerToMakeMove), gameManager.playerToMakeMove);
	        	}
	        	
	        	this.uiUpdateHandler.sendMessageForUI("StartGame", determineScaleFactorRange());
	        	gameManager.determineNextPlayerAndMakeMove();
			}
        	int loops;
        	long next_game_tick = SystemClock.elapsedRealtime();
        	
            while (ApplicationSettings.GAME_THREAD_RUNNING) {
                loops = 0;
                while( SystemClock.elapsedRealtime() > next_game_tick && loops < MAX_FRAMESKIP) {
                	try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// o
						e.printStackTrace();
					}
                    next_game_tick += SKIP_TICKS;
                    loops++;
                }
        	
                drawGameLevelOnCanvas();
            }
            MyLog.D(this, "thread run() method returning.....shutting down");
            
            this.uiUpdateHandler.sendMessageForUI("EndGame", 0);
            
        }

		private void drawGameLevelOnCanvas() {
			Canvas c = null;
			try {
			    c = mSurfaceHolder.lockCanvas(null);
			    synchronized (mSurfaceHolder) {
			        onMyDraw(c);
			    }
			}
			catch(Exception e){
				MyLog.D(this, "Exception thrown from locking canvas on surfaceholder. "+e.toString());
			}
			finally {
			    // do this in a finally so that if an exception is thrown
			    // during the above, we don't leave the Surface in an
			    // inconsistent state
			    if (c != null) {
			        mSurfaceHolder.unlockCanvasAndPost(c);
			    }
			}
		}
		
		public void onMyDraw(Canvas canvas) {
			
			if ( canvas == null)
			{
				MyLog.D(this, "WARN: Canvas is NULL as parameter to onMyDraw. Returning from onMyDraw.");
				return;
			}
			
			canvas.drawColor(Colors.BLACK);
			canvas.save();
			doTranslationAndScaling(canvas);
			drawMyShapes(canvas);			
			if ( ApplicationSettings.DEBUG_MODE) { drawTouchedEvent(canvas); }
			canvas.restore();
		}

		
		private void drawTouchedEvent(Canvas c) {
			Paint p = new Paint();
			p.setColor(Colors.YELLOW);
			c.drawCircle(touchedCoOrds[0], touchedCoOrds[1], (GameBoardSettings.getCircleEndBounds()/2.1f)/SCALE_FACTOR, p);			
			Paint pa = new Paint();
			pa.setColor(Colors.RED);
			c.drawCircle(touchedCoOrds[2], touchedCoOrds[3], (GameBoardSettings.getCircleEndBounds()/2.0f)/SCALE_FACTOR, pa);	
			
//			c.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.triforce_icon),20, 20, null);
		}

		private void doTranslationAndScaling(Canvas canvas) {
			//We're going to scale the X and Y coordinates by the same amount
			canvas.scale(SCALE_FACTOR, SCALE_FACTOR);
//			MyLog.D(this, "Scaled Canvas by scale_factor: "+SCALE_FACTOR);
			//        If translateX times -1 is lesser than zero, let's set it to zero. This takes care of the left bound
			if ( this.canvasWiderThanDisplay )
			{
//				MyLog.D(this, "Canvas WIDER then display");
				if( (translateX * - 1) < 0) {
//					MyLog.D(this, "translateX set to 0");
					translateX = 0;
				}
				else if( ( (translateX * -1) > ( GameBoardSettings.scaledCanvasWidth - ApplicationSettings.GAME_CANVAS_DISPLAY_WIDTH) / SCALE_FACTOR )) {
					translateX = ((GameBoardSettings.scaledCanvasWidth - ApplicationSettings.GAME_CANVAS_DISPLAY_WIDTH) * -1) /SCALE_FACTOR;
//					MyLog.D(this, "translateX set to "+translateX);
				}
			}
			else 
			{
//				MyLog.D(this, "Canvas NOT WIDER then display");
				translateX = 0;
//				MyLog.D(this,  "Not Translating on X axis!");
			}
			
			
			if ( this.canvasHigherThanDisplay )
			{
				MyLog.D(this, "Canvas HIGHER then display");
				if( (translateY * - 1) < 0) {
//					MyLog.D(this, "translateY set to 0");
					translateY = 0;
				}
				else if( ((translateY * -1) > ( GameBoardSettings.scaledCanvasHeight - ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT) / SCALE_FACTOR)) {

					translateY =  ((GameBoardSettings.scaledCanvasHeight -  ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT) * -1) / SCALE_FACTOR;
//					MyLog.D(this, "translateY set to "+translateY);
				}
			}
			else 
			{
//				MyLog.D(this, "Canvas NOT HIGHER then display");
				translateY = 0;
//				MyLog.D(this, "Not Translating on Y axis!");
			}
			canvas.translate(translateX, translateY);
		}
		
		private void drawMyShapes(Canvas canvas) {
			if (ApplicationSettings.SEIZURE_MODE)
			{
				flipColorsBackground(canvas);
			}
			
			for (MyShapeDrawable d : gameManager.myShapeFactory.currentShapes)
			{
				if ( d.alreadySelected)
				{
					if (ApplicationSettings.SEIZURE_MODE)
					{
						flipColors(d);
					}
					else if ( gameManager.getWinningPlayer() > -1 )
					{
						d.getPaint().setColor(gameManager.getColorForPlayer(gameManager.getWinningPlayer()));
					}
					else if ( gameManager.isNextPlayerTurnAnAIEngine())
					{
						if ( d.getPaint().getColor() != Colors.CurrentSelectedShapeColor)
						{
							// color the board with the AI color
							d.getPaint().setColor(gameManager.getColorForPlayer(gameManager.playerToMakeMove));
						}
						
					}
					else
					{
						d.getPaint().setColor(d.originalColor);
					}
					d.draw(canvas);
				}
			}
			
			for ( MyShapeDrawable c: gameManager.myShapeFactory.fillerShapes )
			{
				if (ApplicationSettings.SEIZURE_MODE)
				{
					flipColors(c);
				}
				else if ( gameManager.getWinningPlayer() > -1 )
				{
					c.getPaint().setColor(gameManager.getColorForPlayer(gameManager.getWinningPlayer()));
				}
				else if ( gameManager.isNextPlayerTurnAnAIEngine())
				{
					// color the board with the AI color
					c.getPaint().setColor(gameManager.getColorForPlayer(gameManager.playerToMakeMove));
				}
				else
				{
					c.getPaint().setColor(c.originalColor);
				}
				c.draw(canvas);
			}
			
			for (MyCircleShape circle : gameManager.myShapeFactory.getCircleTokens())
			{
				if ( circle.isVisible() )
				{
					circle.draw(canvas);
					circle.colorCirlce();
					circle.animateCircle();
				}
			}
		}

		private void flipColors(ShapeDrawable d) {
			int r = new Random().nextInt(3);
			int[] colors = new int[]{Colors.RED, Colors.BLUE, Colors.YELLOW};
			d.getPaint().setColor(colors[r]);
			
		}
		
		private void flipColorsBackground(Canvas c)
		{
			int r = new Random().nextInt(3);
			int[] bgColors = new int[]{Colors.GREEN, Colors.CYAN, Colors.MAGENTA};
			c.drawColor(bgColors[r]);
			
		}

		private void modifyScaleFactor(float f) {
			SCALE_FACTOR *= f;
			SCALE_FACTOR = Math.max(MIN_ZOOM, Math.min(SCALE_FACTOR, MAX_ZOOM));
			setBoardDimensions();
		}
		
		private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				modifyScaleFactor(detector.getScaleFactor());
				gameActivity.colorZoomButtons(determineScaleFactorRange());
				return true;
			}
		}


		public void setBoardDimensions()
		{
//			
			float nWidth = Math.round( ApplicationSettings.GAME_CANVAS_DISPLAY_WIDTH);
			float nHeight = Math.round( ApplicationSettings.GAME_CANVAS_DISPLAY_HEIGHT);
			drawLevelThread.canvasWiderThanDisplay = drawLevelThread.canvasHigherThanDisplay = false;
			
			GameBoardSettings.scaledCanvasWidth = GameBoardSettings.myCanvasWidth * SCALE_FACTOR;
			GameBoardSettings.scaledCanvasHeight = GameBoardSettings.myCanvasHeight * SCALE_FACTOR;
			
//			BoardSettings.myCanvasHeight *= drawLevelThread.scaleFactor;
//			BoardSettings.myCanvasWidth *= drawLevelThread.scaleFactor;
			
			MyLog.D(this, "OnMeasure: Window Dimensions: "+nWidth+", "+nHeight);
			if ( nWidth < GameBoardSettings.scaledCanvasWidth)
			{
				drawLevelThread.canvasWiderThanDisplay = true;
				nWidth = GameBoardSettings.scaledCanvasWidth;
				MyLog.D(this, "OnMeasure: Window Width is smaller than canvas. New Width: "+nWidth);
			}
			if ( nHeight < GameBoardSettings.scaledCanvasHeight)
			{
				drawLevelThread.canvasHigherThanDisplay = true;
				nHeight = GameBoardSettings.scaledCanvasHeight;
				MyLog.D(this, "OnMeasure: Window Height is smaller than canvas. New Height: "+nHeight);
			}
			setMeasuredDimension(Math.round(nWidth)+1, Math.round(nHeight)+1); 	    
		}
		
		//Undo the last move
		public void UndoMove() {
			gameManager.UndoMove();
		}
		
		public int UserZoomControl(boolean zoomIn) {
			float scaleDiff = (MAX_ZOOM - MIN_ZOOM)/10;
			if ( zoomIn ) 
			{
				scaleDiff += 1;
			}
			else
			{
				scaleDiff = 1 - scaleDiff;
			}
			this.modifyScaleFactor(scaleDiff);
			
			return determineScaleFactorRange();
		}

		public int determineScaleFactorRange() {
			if ( SCALE_FACTOR >= MAX_ZOOM ) 
			{
				// return that this is at max level
				return 1;
			}
			if ( SCALE_FACTOR <= MIN_ZOOM)
			{
				return -1;
			}
			// return scale is within range
			return 0;
		}

//		public boolean CanPerformUndoMove() {
//			// TODO Auto-generated method stub
//			return gameManager.canUndoAnotherMove();
//		}

	}


}
