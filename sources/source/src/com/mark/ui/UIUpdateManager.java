package com.mark.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UIUpdateManager {

	private Handler mHandler;
	public UIUpdateManager(Handler mHandler)
	{
		this.mHandler = mHandler;
	}
	
	public void sendMessageForUI(String text, int playerNo) {
		Message m = new Message();
		Bundle b = new Bundle();
		b.putString("StatusText", text);
		b.putInt("Player", playerNo);
		m.setData(b);
		mHandler.sendMessage(m);
//		mHandler.sendMessageAtFrontOfQueue(m);
		
	}
}
