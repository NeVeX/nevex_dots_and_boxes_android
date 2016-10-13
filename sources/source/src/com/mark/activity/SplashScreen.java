package com.mark.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.mark.R;
import com.mark.app.ApplicationSettings;
import com.mark.app.Colors;
import com.mark.game.GameBoardSettings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class SplashScreen extends Activity {
	Handler mhandler = new Handler();
	ViewAnimator mViewAnimator;
	Animation animationIn, animationOut;
	View currentViewShowing;
	boolean canStartApp = false;
	
	private Runnable myRunner = new Runnable() {
		public void run() {
			enableClickText();
		}
	};
	

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_screen);
        
        new Colors(getResources());
//        overridePendingTransition(enterAnim, exitAnim)
        // schedule the runner to update the text
//        mhandler.postDelayed(myRunner, 3000);
        
        currentViewShowing = getLayoutInflater().inflate(R.layout.marx_intro_splash_screen, null, false);
        currentViewShowing.setId(R.layout.marx_intro_splash_screen);
//      mViewAnimator = new ViewAnimator(this);
//      mViewAnimator.setAnimateFirstView(true);
		animationIn = new AlphaAnimation(0.0f, 1.0f);
		animationIn.setDuration(2000);
		animationIn.setAnimationListener(animationFadeInListener);
		animationOut = new AlphaAnimation(1.0f, 0.0f);
		animationOut.setDuration(2000);
		animationOut.setAnimationListener(animationFadeOutListener);
//      mViewAnimator.setInAnimation(animation);
        
		currentViewShowing.startAnimation(animationIn);
        setContentView(currentViewShowing);
//        this.displayVersionName();
//        Animation animation;
//        View nevexScreen = findViewById(R.layout.splash_screen);
//        // Set up the view animator
//        mViewAnimator = new ViewAnimator(this);
//        mViewAnimator.setAnimateFirstView(true);
//
//        animation = new AlphaAnimation(0.0f, 1.0f);
//        animation.setDuration(2000);
//        mViewAnimator.setInAnimation(animation);
//
//        animation = new AlphaAnimation(1.0f, 0.0f);
//        animation.setDuration(2000);
//        mViewAnimator.setOutAnimation(animation);
//
//        this.setContentView(mViewAnimator);
//
//        mViewAnimator.removeAllViews();
//        mViewAnimator.addView(nevexScreen);
        
       

	}
//
//	Here's the code to do a nice smooth fade between two Activities.. 
//	 Create a file called fadein.xml in res/anim
//
//	<?xml version="1.0" encoding="utf-8"?>
//	<alpha xmlns:android="http://schemas.android.com/apk/res/android"
//	   android:interpolator="@android:anim/accelerate_interpolator"
//	   android:fromAlpha="0.0" android:toAlpha="1.0" android:duration="2000" />
//
//
//	Create a file called fadeout.xml in res/anim
//
//	<?xml version="1.0" encoding="utf-8"?>
//
//	<alpha xmlns:android="http://schemas.android.com/apk/res/android"
//	   android:interpolator="@android:anim/accelerate_interpolator"
//	   android:fromAlpha="1.0" android:toAlpha="0.0" android:duration="2000" />
//
//
//	If you want to fade from Activity A to Activity B, put the following in the onCreate method for Activity B. Before setContentView works for me.
//
//
//
//	overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//	If the fades are too slow for you, change android:duration in the xml files above to something smaller.
	
	public void enableClickText() {
		
		TextView touchText = (TextView) findViewById(R.id.touchScreenSplashScreenText);
		touchText.setVisibility(View.VISIBLE);
		
	}

	public void splashScreenClick(View v)
	{
		if ( !canStartApp ) { return; }
		// stop my runner from updating
//		mhandler.removeCallbacks(myRunner);
		// start the main menu activity
		Intent myIntent = new Intent(v.getContext(), MainMenuActivity.class);
		startActivity(myIntent);
	}
	
	private void getAppVersionNumber() {
	    PackageInfo packageInfo;
	    try {
	        packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	        ApplicationSettings.APP_VERSION_STRING = "v " + packageInfo.versionName;
	    } catch (NameNotFoundException e) {
	        e.printStackTrace();
	    }
	    
//	    TextView tv = (TextView) findViewById(R.id.splashVersionAppNumber);
//	    tv.setText(ApplicationSettings.APP_VERSION_STRING);
	}
	
	private AnimationListener animationFadeInListener = new AnimationListener() {
		
		public void onAnimationStart(Animation animation) {
			// do nothing
			
		}
		
		public void onAnimationRepeat(Animation animation) {
			// do nothing
			
		}
		
		public void onAnimationEnd(Animation animation) {
			if ( currentViewShowing.getId() == R.layout.marx_intro_splash_screen)
			{
				// fade out
				currentViewShowing.startAnimation(animationOut);
			}
			else
			{
				//  show the help text
//				enableClickText();
				mhandler.postDelayed(myRunner, 2000);
				getAppVersionNumber();
				canStartApp = true;
			}
		}

	};
	
	private AnimationListener animationFadeOutListener = new AnimationListener() {
		
		public void onAnimationStart(Animation animation) {
			// do nothing
			
		}
		
		public void onAnimationRepeat(Animation animation) {
			// do nothing
			
		}
		
		public void onAnimationEnd(Animation animation) {
			// fade out
			currentViewShowing = getLayoutInflater().inflate(R.layout.nevex_intro_splash_screen, null, false);
			currentViewShowing.setId(R.layout.nevex_intro_splash_screen);
			currentViewShowing.startAnimation(animationIn);
			setContentView(currentViewShowing);
		}
	};
	
	
}
