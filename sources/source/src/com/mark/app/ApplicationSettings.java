package com.mark.app;

public class ApplicationSettings {
	
	
	
	public static final int NOT_ALLOWED_UNDO = 1000;
	public static final int ALLOWED_UNDO = 1001;
	public static boolean DEBUG_MODE = false;
	public static boolean SEIZURE_MODE = false;
	public static boolean ANIMATE = false;
	public static boolean ALLOW_BOARD_MOVEMENT = false;
	public static String APP_VERSION_STRING = "** Dev Build **";
	public static float GAME_CANVAS_DISPLAY_WIDTH;
	public static float GAME_CANVAS_DISPLAY_HEIGHT;
	public static boolean GAME_THREAD_RUNNING = false;
	public static boolean IS_BOARD_GAME_OVER = false;
	public static int NUMBER_OF_PLAYERS = 2;
	public static long MINIMUM_AI_MOVE_DELAY = 0;
	public static long CURRENT_AI_MOVE_DELAY = 1000;
	public static long MAXIMUM_AI_MOVE_DELAY = 5000;
	public static long AI_MOVE_DELAY_INCREMENT = 100;

}
