package com.mark.activity;

import com.mark.R;
import com.mark.app.ApplicationSettings;
import com.mark.game.GameBoardSettings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        Spinner playerOne = (Spinner) findViewById(R.id.spinnerPlayerOne);
        ArrayAdapter adapterOne = ArrayAdapter.createFromResource(
                this, R.array.players, android.R.layout.simple_spinner_item);
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerOne.setAdapter(adapterOne);
        
        Spinner playerTwo = (Spinner) findViewById(R.id.spinnerPlayerTwo);
        ArrayAdapter adapterTwo = ArrayAdapter.createFromResource(
                this, R.array.players, android.R.layout.simple_spinner_item);
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerTwo.setAdapter(adapterTwo);
        playerTwo.setSelection(1);
        
        
        Spinner playerThree = (Spinner) findViewById(R.id.spinnerPlayerThree);
        ArrayAdapter adapterThree = ArrayAdapter.createFromResource(
                this, R.array.players, android.R.layout.simple_spinner_item);
        adapterThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerThree.setAdapter(adapterThree);
        
        
        Spinner playerFour = (Spinner) findViewById(R.id.spinnerPlayerFour);
        ArrayAdapter adapterFour = ArrayAdapter.createFromResource(
                this, R.array.players, android.R.layout.simple_spinner_item);
        adapterFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerFour.setAdapter(adapterFour);
        
        
        Spinner rowSpinner = (Spinner) findViewById(R.id.spinnerRows);
        ArrayAdapter rowAdapter = ArrayAdapter.createFromResource(
                this, R.array.boardSizes, android.R.layout.simple_spinner_item);
        rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowSpinner.setAdapter(rowAdapter);
        
        Spinner colSpinner = (Spinner) findViewById(R.id.spinnerColumns);
        ArrayAdapter colAdapter = ArrayAdapter.createFromResource(
                this, R.array.boardSizes, android.R.layout.simple_spinner_item);
        colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colSpinner.setAdapter(colAdapter);
        
        
        TextView t = (TextView) findViewById(R.id.menuVersionAppNumber);
        t.setText(ApplicationSettings.APP_VERSION_STRING);
        
        
        defineThePlayerSelectionOptions();

       
    }
    
    private void defineThePlayerSelectionOptions() {

    	LinearLayout playerThreeLL = (LinearLayout) findViewById(R.id.playerThreeSelectionBox);
    	LinearLayout playerFourLL = (LinearLayout) findViewById(R.id.playerFourSelectionBox);
    	CheckBox playerThreeCb = (CheckBox) findViewById(R.id.checkboxPlayerThree);
    	CheckBox playerFourCb = (CheckBox) findViewById(R.id.checkboxPlayerFour);
    	switch(ApplicationSettings.NUMBER_OF_PLAYERS)
    	{
	    	case 3:
//	    		playerThreeLL.setEnabled(true);
	    		playerFourCb.setEnabled(true);
	    		playerFourCb.setChecked(false);
//	    		playerFourLL.setEnabled(false);
	    		this.enableDisableView(playerThreeLL, true);
	    		this.enableDisableView(playerFourLL, false);
	    		break;
	    	case 4:
//	    		playerThreeLL.setEnabled(true);
//	    		playerFourLL.setEnabled(true);
	    		this.enableDisableView(playerThreeLL, true);
	    		this.enableDisableView(playerFourLL, true);
	    		break;
	    	default:
//	    		playerThreeLL.setEnabled(false);
	    		this.enableDisableView(playerThreeLL, false);
	    		this.enableDisableView(playerFourLL, false);
	    		playerFourCb.setEnabled(false);
	    		playerFourCb.setChecked(false);
	    		playerThreeCb.setChecked(false);
//	    		playerFourLL.setEnabled(false);
	    		break;
    	}
    	
	}
    
    private void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);

        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

	public void settingsButtonClicked(View v)
    {
    	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(i);
    }
    
    public void playerFourCheckboxClicked(View v)
    {
    	// allow four players to play
    	CheckBox playerFourCb = (CheckBox) findViewById(R.id.checkboxPlayerFour);
    	if (playerFourCb.isChecked()) { ApplicationSettings.NUMBER_OF_PLAYERS = 4; }
    	else { ApplicationSettings.NUMBER_OF_PLAYERS = 3;}
    	defineThePlayerSelectionOptions();
    }
    
    public void playerThreeCheckboxClicked(View v)
    {
    	// allow three players to play
    	CheckBox playerThreeCb = (CheckBox) findViewById(R.id.checkboxPlayerThree);
    	if (playerThreeCb.isChecked()) { ApplicationSettings.NUMBER_OF_PLAYERS = 3; }
    	else { ApplicationSettings.NUMBER_OF_PLAYERS = 2;}
    	defineThePlayerSelectionOptions();
    }

	public void startNewGame(View v)
    {
    	Spinner playerOne = (Spinner) findViewById(R.id.spinnerPlayerOne);
    	String p1 = (String) playerOne.getSelectedItem();
    	
    	Spinner playerTwo = (Spinner) findViewById(R.id.spinnerPlayerTwo);
    	String p2 = (String) playerTwo.getSelectedItem();
    	
    	String p3, p4;
    	p3 = p4 = "None";
    	if (ApplicationSettings.NUMBER_OF_PLAYERS >= 3 )
    	{
	    	Spinner playerThree = (Spinner) findViewById(R.id.spinnerPlayerThree);
	    	p3 = (String) playerThree.getSelectedItem();
    	}
    	
    	if ( ApplicationSettings.NUMBER_OF_PLAYERS == 4)
    	{
	    	Spinner playerFour = (Spinner) findViewById(R.id.spinnerPlayerFour);
	    	p4 = (String) playerFour.getSelectedItem();
    	}
    	
    	Spinner rowSpinner = (Spinner) findViewById(R.id.spinnerRows);
    	Spinner colSpinner = (Spinner) findViewById(R.id.spinnerColumns);
    	
        GameBoardSettings.boardRows = Integer.parseInt((String) rowSpinner.getSelectedItem());
        GameBoardSettings.boardColumns = Integer.parseInt((String) colSpinner.getSelectedItem());
    	
    	Intent myIntent = new Intent(v.getContext(), GameActivity.class);
    	
    	myIntent.putExtra("P1", p1);
    	myIntent.putExtra("P2", p2);
    	myIntent.putExtra("P3", p3);
    	myIntent.putExtra("P4", p4);
    	startActivity(myIntent);
    	
    }
	
}

