package com.eduardogutierrezvalle.androidapps.pairsgame.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.data.Constants;
	
public class SelectImagesActivity extends BaseActivity implements OnClickListener  {
	
	private static final int ACTIVITY_SELECT_IMAGE = 0;
	
	ImageView selectedImage;
	int position;
	ImageView selectImages[];
	
	private Handler mHandler = new Handler(new MyHandlerCallback());
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.select_images);

		Button btnReloadImages = (Button) findViewById(R.id.buttonReloadImages);
		btnReloadImages.setOnClickListener(this);

		loadImages();
		
	}

	private void loadImages() {
		selectImages = new ImageView[application.numberOfBitmaps];
		
		final int[] images = {R.id.imageView1, R.id.imageView2,R.id.imageView3,
				R.id.imageView4,R.id.imageView5,R.id.imageView6,
				R.id.imageView7,R.id.imageView8,R.id.imageView9,
				R.id.imageView10,R.id.imageView11,R.id.imageView12,
				R.id.imageView13,R.id.imageView14,R.id.imageView15,
				R.id.imageView16,R.id.imageView17,R.id.imageView18,
				R.id.imageView19,R.id.imageView20, R.id.imageView20};

		for (int i = 0; i < application.numberOfBitmaps; i++) {
			selectImages[i] = (ImageView) findViewById(images[i]);
			selectImages[i].setClickable(true);
			selectImages[i].setOnClickListener(this);
			selectImages[i].setImageBitmap(Bitmap.createScaledBitmap(application.bitmaps[i], 96, 96, false));
		}
	}

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.buttonReloadImages) {
			Thread t = new Thread() {
				public void run() {
					reloadImages();
				}
			};
			mHandler.sendEmptyMessage(0);
			t.start();
			return;
		}
		
		selectedImage = (ImageView) v;
		for (int i = 0; i < selectImages.length; i++) {
			if (selectedImage.equals(selectImages[i])) {
				position = i;
				break;
			}
		}
		Intent i = new Intent(Intent.ACTION_PICK,
	               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, ACTIVITY_SELECT_IMAGE); 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case ACTIVITY_SELECT_IMAGE:
	        if(resultCode == RESULT_OK){  
	            Uri selection = imageReturnedIntent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selection, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
	            if (yourSelectedImage != null) {
	            	yourSelectedImage = Bitmap.createScaledBitmap(yourSelectedImage, 256, 256, false);
		            Bitmap scaleImage = Bitmap.createScaledBitmap(yourSelectedImage, 96, 96, false);
		            selectedImage.setImageBitmap(scaleImage);
		            application.bitmaps[position] = yourSelectedImage;
		            
		            application.setupBitmapTexture(position);
		            application.saveBitmapTexture(position);
		            application.saveBitmap(position);
	            }
	        }
	    }
	}
	
	private void reloadImages() {
		try {
//			application.loadBitmapsFromServer(Constants.IMAGES_URL);
			application.loadBitmapsFromAssets();
			application.setupBitmapTextures();
			application.saveBitmapTextures();
			application.saveBitmaps();
			mHandler.sendEmptyMessage(1);
		} catch (Exception e) {
			mHandler.sendEmptyMessage(2);
		}
	}
	
	private class MyHandlerCallback implements Callback {
		public boolean handleMessage(Message msg) {
			if (msg.what == 0) {
				Toast.makeText(application, getString(R.string.reloading_original_images), Toast.LENGTH_SHORT)
				.show();
				return true;
			}
			if (msg.what == 1) {
				loadImages();
				Toast.makeText(application, getString(R.string.images_reloaded), Toast.LENGTH_SHORT)
				.show();
				return true;
			}
			if (msg.what == 2) {
				Toast.makeText(application, getString(R.string.error_updating_images), Toast.LENGTH_SHORT)
				.show();
				return true;
			}
			
			return false;
		}
	}
}
