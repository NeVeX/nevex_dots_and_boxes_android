package com.mark.activity;

import android.app.Activity;
import android.content.Intent;
//import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mark.R;
import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.board.Board;
import com.mark.game.GameManager;
import com.mark.game.GameBoardSettings;
import com.mark.game.GamePlayers;
import com.mark.game.GameScores;
import com.mark.logging.MyLog;
import com.mark.players.ai.Viper;
import com.mark.players.ai.NeVeX;
import com.mark.ui.MyGameView;

public class GameActivity extends Activity {
	
	public static String[] players = new String[4];
	private MyGameView drawLevelView;
//	private Button animateButton;
	private Button undoButton;
//	private Button seizureButton;
	private Button zoomPositiveButton;
	private Button zoomNegativeButton;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MyLog.D(this, "Inflating the menu button options Menu.");
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.control_menu, menu);
	    return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		MyLog.D(this, "Menu Popup option selected.");
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.popupSettingsItem:
	        	MyLog.D(this, "Menu Popup option selected is Settings.");
	    		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
	    		startActivity(i);
	    		MyLog.D(this, "Launched Settings Activity.");
	            return true;
	         default:
	         	return true;
	    }
	}
	
	public void UndoButtonClicked(View v)
	{
		
		MyLog.D(this, "undoButton Clicked");
		if (!ApplicationSettings.IS_BOARD_GAME_OVER ) 
		{
			drawLevelView.getDrawLevelThread().UndoMove();
		}

	}

	public void decideHowToColorUndoButton(boolean shouldColor) {
		if (shouldColor && !ApplicationSettings.IS_BOARD_GAME_OVER) { undoButton.setBackgroundColor(Colors.ButtonCanBeClicked); }
		else {  undoButton.setBackgroundColor(Colors.ButtonCannotBeClicked); }
	}
	
	
	
	public void ZoomPositiveClick(View v)
	{
		if (ApplicationSettings.GAME_THREAD_RUNNING ) 
		{
			this.colorZoomButtons(this.drawLevelView.getDrawLevelThread().UserZoomControl(true));
		}
	}
	
	public void gameEndUIChanges() {
		zoomNegativeButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
		zoomPositiveButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
		undoButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
		
	}
	
	public void colorZoomButtons(int userZoomControl) {
		if ( userZoomControl == 0 )
		{
			zoomNegativeButton.setBackgroundColor(Colors.ButtonCanBeClicked);
			zoomPositiveButton.setBackgroundColor(Colors.ButtonCanBeClicked);
		}
		else if ( userZoomControl == -1 )
		{
			// negative zoom maxed
			zoomNegativeButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
			zoomPositiveButton.setBackgroundColor(Colors.ButtonCanBeClicked);
			
		}
		else if ( userZoomControl == 1)
		{
			//positive zoom maxed
			zoomPositiveButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
			zoomNegativeButton.setBackgroundColor(Colors.ButtonCanBeClicked);
		}
		
	}

	public void ZoomNegativeClick(View v)
	{
		if (ApplicationSettings.GAME_THREAD_RUNNING ) 
		{
			this.colorZoomButtons(this.drawLevelView.getDrawLevelThread().UserZoomControl(false));
		}
	}
	

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String p1 = b.getString("P1");
        String p2 = b.getString("P2");
        String p3 = b.getString("P3");
        String p4 = b.getString("P4");
        if ( b != null ) 
        {
        	players[0] = p1;
        	players[1] = p2;
        	players[2] = p3;
        	players[3] = p4;
        	
        	if ( GamePlayers.DetermineRealPlayers().size() < 2)
        	{
        		// we need at least two players
        		return;
        	}
        	setContentView(R.layout.game_parent);
        	this.createViewObjects();
        }
        
    }

	private void createViewObjects() {
		undoButton = (Button) findViewById(R.id.UndoButton);
		drawLevelView = (MyGameView) findViewById(R.id.DrawLevelID);
		
		zoomPositiveButton = (Button) findViewById(R.id.ZoomPositiveButton);
		zoomNegativeButton = (Button) findViewById(R.id.ZoomNegativeButton);
		zoomPositiveButton.setBackgroundColor(Colors.ButtonCanBeClicked);
		zoomNegativeButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
		undoButton.setBackgroundColor(Colors.ButtonCannotBeClicked);
	}

	public void gameStartUIChanges(int zoomLevel) {
		this.colorZoomButtons(zoomLevel);
	}

    
}