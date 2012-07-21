package com.eduardogutierrezvalle.androidapps.pairsgame.utils;

import android.util.Log;

public class AppLogger {
	
	private static boolean enabled = true;
	private static String tag = "APPCHALLENGE";

	public static void setLogEnabled(boolean b) {
		enabled = b;
	}

	public static void d(String msg) {
		if (enabled && Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, msg);
		}
	}

	public static void i(String msg) {
		if (enabled && Log.isLoggable(tag, Log.INFO)) {
			Log.i(tag, msg);
		}
	}

	public static void e(String msg) {
		if (enabled && Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, msg);
		}
	}

	public static void e(String msg, Throwable t) {
		if (enabled && Log.isLoggable(tag, Log.ERROR)) {
			Log.e(tag, msg, t);
		}
	}
}
