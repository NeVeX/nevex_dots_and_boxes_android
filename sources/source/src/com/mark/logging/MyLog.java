package com.mark.logging;

import android.util.Log;

public class MyLog {

	public static void D(Object obj, String logText)
	{
		Log.d(obj.getClass().getSimpleName(), logText);
	}
}
