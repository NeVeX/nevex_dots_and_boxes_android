package com.mark.activity;

import com.mark.R;
import com.mark.app.ApplicationSettings;
import com.mark.game.GameBoardSettings;
import com.mark.logging.MyLog;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	CheckBox animateCheckBox, debugModeCheckBox, seizureModeCheckBox;
	TextView minimumAiDelay;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        minimumAiDelay = (TextView) findViewById(R.id.aiEngineMinimumDelay);
        minimumAiDelay.setText(Long.toString(ApplicationSettings.CURRENT_AI_MOVE_DELAY));
//        minimumAiDelay.setOnFocusChangeListener(new OnFocusChangeListener() {
//			public void onFocusChange(View arg0, boolean arg1) {
//				if (arg1 )
//				{

//				}
//				
//			}
//		});
//        minimumAiDelay.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//				long value = -1;
//            	try {
//            		value = Long.parseLong(minimumAiDelay.getText().toString());
//            		if ( value >= ApplicationSettings.MINIMUM_AI_MOVE_DELAY && value <= ApplicationSettings.MAXIMUM_AI_MOVE_DELAY)
//                	{
//                		ApplicationSettings.CURRENT_AI_MOVE_DELAY = value;
//                		return;
//                	}
//            	}
//            	catch (Exception e) { 
//            		//do nothing 
//            	}
//            	minimumAiDelay.setText(Long.toString(ApplicationSettings.CURRENT_AI_MOVE_DELAY));
//            }
//            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
//            public void onTextChanged(CharSequence s, int start, int before, int count){
//
//            }
//        }); 
        
        animateCheckBox = (CheckBox) findViewById(R.id.checkBoxAnimate);
        debugModeCheckBox = (CheckBox) findViewById(R.id.checkBoxDebugMode);
        seizureModeCheckBox = (CheckBox) findViewById(R.id.checkBoxSeizureMode);
        animateCheckBox.setChecked(ApplicationSettings.ANIMATE);
        debugModeCheckBox.setChecked(ApplicationSettings.DEBUG_MODE);
        seizureModeCheckBox.setChecked(ApplicationSettings.SEIZURE_MODE);
//        TextView tv = (TextView) findViewById(R.id.settingsScreenVersionAppNumber);
//	    tv.setText(ApplicationSettings.APP_VERSION_STRING);
	}
	
	public void animateCheckboxClicked(View v)
	{
		ApplicationSettings.ANIMATE = animateCheckBox.isChecked();
	}
	
	public void debugModeCheckboxClicked(View v)
	{
		ApplicationSettings.DEBUG_MODE = debugModeCheckBox.isChecked();
	}
	
	public void positiveMinimumAiDelayButtonClick(View v)
	{
		long value = Long.parseLong(minimumAiDelay.getText().toString());
		value += ApplicationSettings.AI_MOVE_DELAY_INCREMENT;
		if ( value >= ApplicationSettings.MAXIMUM_AI_MOVE_DELAY)
		{
			value = ApplicationSettings.MAXIMUM_AI_MOVE_DELAY;
		}
		ApplicationSettings.CURRENT_AI_MOVE_DELAY = value;
		minimumAiDelay.setText(Long.toString(ApplicationSettings.CURRENT_AI_MOVE_DELAY));
	}
	
	public void negativeMinimumAiDelayButtonClick(View v)
	{
		long value = Long.parseLong(minimumAiDelay.getText().toString());
		value -= ApplicationSettings.AI_MOVE_DELAY_INCREMENT;
		if ( value <= ApplicationSettings.MINIMUM_AI_MOVE_DELAY)
		{
			value = ApplicationSettings.MINIMUM_AI_MOVE_DELAY;
		}
		ApplicationSettings.CURRENT_AI_MOVE_DELAY = value;
		minimumAiDelay.setText(Long.toString(ApplicationSettings.CURRENT_AI_MOVE_DELAY));
	}
	
	public void seizureModeCheckboxClicked(View v)
	{
		MyLog.D(this, "seizure mode Clicked");
		if (seizureModeCheckBox.isChecked())
		{
			ApplicationSettings.SEIZURE_MODE = true;
			ApplicationSettings.ANIMATE = true;
		}
		else
		{
			ApplicationSettings.SEIZURE_MODE = false;
			ApplicationSettings.ANIMATE = false;
		}
//		this.colorButtons();
	}
	
}
