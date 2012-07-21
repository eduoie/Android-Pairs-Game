package com.eduardogutierrezvalle.androidapps.pairsgame.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.application.GameStart;

public class MainScreenActivity extends BaseActivity {

	Button buttonPlayGame;
	Button buttonOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

//        ImageView imagentmp = (ImageView) findViewById(R.id.imageView1);
//        imagentmp.setImageBitmap(application.texturebmp[0]);

		buttonPlayGame = (Button) findViewById(R.id.playGameButton);
		buttonPlayGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				ImageView logo;
//				logo = (ImageView) findViewById(R.id.Logo);
//				int logoHeight;
//				int logoWidth;
//				logoHeight = logo.getHeight();
//				AppLogger.i(logo.getMeasuredHeight() + "," + logo.getBottom()
//						+ "," + logo.getPaddingBottom() +"," + logo.getTop());
//				logoWidth = logo.getWidth();
//				Intent intent = new Intent(application, GLGameActivity.class);
//				intent.putExtra("logoHeight", logoHeight);
//				intent.putExtra("logoWidth", logoWidth);
//				startActivity(intent);
				startActivity(new Intent(application, GameStart.class));
				
			}
		});
		
		buttonOptions = (Button) findViewById(R.id.options_button);
		buttonOptions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(application, OptionsActivity.class));
			}
		});
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// switch (item.getItemId()) {
		// case R.id.itemOptions:
		startActivity(new Intent(this, OptionsActivity.class));
		// break;
		// }
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// exitPressed(null);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exitPressed(View v) {
		// showAlert(this,
		// null,
		// getString(R.string.areyousuretoquitapp),
		// getString(R.string.yes),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// finish();
		// overridePendingTransition(R.anim.screenfadein,
		// R.anim.screenfadeout);
		// }
		// }
		// ,
		// getString(R.string.no),
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// // dialog.cancel();
		// }
		// },
		// null);

	}
}