package com.eduardogutierrezvalle.androidapps.pairsgame.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.data.Constants;

public class SplashScreenActivity extends BaseActivity {

	private boolean cancelDownload;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.splashscreen);

		// previously images were loaded from a server. Not anymore
		new LoadImagesTask().execute();
	}


	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			cancelDownload = true;
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class LoadImagesTask extends AsyncTask<String, Integer, Long> {

//		private void loadFromServer(String url) throws Exception {
//			application.loadBitmapsFromServer(url);
//		}
		
		private void loadFromAssets() {
			application.loadBitmapsFromAssets();
		}
		protected Long doInBackground(String... urls) {

			try {
				application.loadBitmapsFromDisk();
				application.loadTexturesFromDisk();
			} catch (Exception e) {
				// couldn't load images or textures from sdcard, load and setup from assets
				try {
//					loadFromServer(urls[0]);
					// if they were not saved into sdcard
					loadFromAssets();
					application.setupBitmapTextures();
					application.saveBitmapTextures();
					application.saveBitmaps();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			return (long) 22;
		}

		

		protected void onProgressUpdate(Integer... progress) {
			// super.onProgressUpdate(progress[0]);
			// setProgress(progress[0]);
		}

		protected void onPostExecute(Long result) {
			if (!cancelDownload) {
				Toast.makeText(application, "Load completed", Toast.LENGTH_SHORT)
				.show();
			} 
			startActivity(new Intent(application, MainScreenActivity.class));
			finish();
			overridePendingTransition(R.anim.screenfadein,
					R.anim.screenfadeout);
		}

	}
}