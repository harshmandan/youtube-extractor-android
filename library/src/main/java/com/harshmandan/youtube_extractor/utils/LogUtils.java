package com.harshmandan.youtube_extractor.utils;

import android.util.Log;

import com.github.kotvertolet.youtubejextractor.BuildConfig;

public class LogUtils
{
	public static void log(String x){
		if(BuildConfig.DEBUG) {
			Log.i("DEBUG", x);
		}
	}
}
