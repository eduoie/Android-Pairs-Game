package com.eduardogutierrezvalle.androidapps.pairsgame.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.application.MainApplication;
import com.eduardogutierrezvalle.androidapps.pairsgame.data.Constants;

public class OptionsActivity extends BaseActivity implements OnCheckedChangeListener {

	AlertDialog alertDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.options);
		
		Button btnSelectImages = (Button) findViewById(R.id.buttonSelectImages);
		Button btnPlayerMode = (Button) findViewById(R.id.buttonPlayerMode);
		Button btnBoardSize = (Button) findViewById(R.id.buttonBoardSize);

		CheckBox checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
		checkBoxSound.setChecked(MainApplication.soundEnabled);

		checkBoxSound.setOnCheckedChangeListener(this);

		btnSelectImages.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(application,
						SelectImagesActivity.class));
			}
		});

		btnPlayerMode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
				final Editor editPrefs = prefs.edit();
				int checkedItem = -1;
				
				if (application.twoPlayers == false) {
					checkedItem = 0;
				} else if (application.twoPlayers) {
					if (application.computerPlayer) {
						checkedItem = 2;
					}
					else {
						checkedItem = 1;
					}
				}
				
				alertDialog = new AlertDialog.Builder(OptionsActivity.this)
						.setSingleChoiceItems(
								new CharSequence[] {getString(R.string.one_player),
										getString(R.string.two_players), getString(R.string.player_vs_computer) },
								checkedItem, new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										switch (whichButton) {
										case 0:
											application.twoPlayers = false;
											editPrefs.putBoolean(Constants.prefTwoPlayers, false);
											break;
										case 1:
											application.twoPlayers = true;
											application.computerPlayer = false;
											editPrefs.putBoolean(Constants.prefTwoPlayers, true);
											editPrefs.putBoolean(Constants.prefComputerPlayer, false);
											break;
										case 2:
											application.twoPlayers = true;
											application.computerPlayer = true;
											editPrefs.putBoolean(Constants.prefTwoPlayers, true);
											editPrefs.putBoolean(Constants.prefComputerPlayer, true);
											break;
										}
										editPrefs.commit();
										alertDialog.dismiss();
									}
								})
						// .setPositiveButton("Ok", new
						// DialogInterface.OnClickListener() {
						// public void onClick(DialogInterface dialog, int
						// whichButton) {
						// }
						// })
						.create();
				alertDialog.show();
			}
		});
		
		btnBoardSize.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
				final Editor editPrefs = prefs.edit();
				int checkedItem = application.boardType;
				
				alertDialog = new AlertDialog.Builder(OptionsActivity.this)
						.setSingleChoiceItems(						
							new CharSequence[] { "5 x 8", "5 x 6", "5 x 4", "3 x 6", "3 x 4" },
							checkedItem, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
											
									switch (whichButton) {
										case Constants.board5by8:
											application.boardType = Constants.board5by8;
											application.columns = 5;
											application.rows = 8;
											break;
										case Constants.board5by6:
											application.boardType = Constants.board5by6;
											application.columns = 5;
											application.rows = 6;
											break;
										case Constants.board5by4:
											application.boardType = Constants.board5by4;
											application.columns = 5;
											application.rows = 4;
											break;
										case Constants.board3by6:
											application.boardType = Constants.board3by6;
											application.columns = 3;
											application.rows = 6;
											break;
										case Constants.board3by4:
											application.boardType = Constants.board3by4;
											application.columns = 3;
											application.rows = 4;
											break;
										}
									editPrefs.putInt(Constants.prefBoardSize, application.boardType);
									editPrefs.commit();
									alertDialog.dismiss();
								}
							})
						.create();
				alertDialog.show();
			}
		});
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		MainApplication.soundEnabled = isChecked;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OptionsActivity.this);
		Editor editPrefs = prefs.edit();
		editPrefs.putBoolean(Constants.prefSoundEffects, isChecked);
		editPrefs.commit();
	}
}
