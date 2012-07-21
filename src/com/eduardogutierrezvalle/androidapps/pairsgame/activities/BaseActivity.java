package com.eduardogutierrezvalle.androidapps.pairsgame.activities;

import android.app.Activity;
import android.os.Bundle;

import com.eduardogutierrezvalle.androidapps.pairsgame.application.MainApplication;

public class BaseActivity extends Activity {
	public MainApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MainApplication) getApplication();
	}
}
