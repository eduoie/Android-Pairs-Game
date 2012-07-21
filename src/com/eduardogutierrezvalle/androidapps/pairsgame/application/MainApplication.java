package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.preference.PreferenceManager;

import com.eduardogutierrezvalle.androidapps.pairsgame.data.Constants;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.AndroidFileIO;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.FileIO;
import com.eduardogutierrezvalle.androidapps.pairsgame.utils.AppLogger;

// This class will held data to be shared among the whole application

public class MainApplication extends Application {

	public final int numberOfBitmaps = 20;
	public static final String SDCardDirectory = "PairsGameApp";
	
	public Bitmap bitmaps[];
	public Bitmap background;
	public Bitmap closedTile;
	public Bitmap[] texturebmp;

	public boolean twoPlayers;
	public boolean computerPlayer;

	public int rows;
	public int columns;
	public int boardType;

    public FileIO fileIO;

	public static boolean soundEnabled;

	private static Context context;

	public static Context getAppContext() {
		return MainApplication.context;
	}
	
	public void onCreate() {
		super.onCreate();
        MainApplication.context = getApplicationContext();

		bitmaps = new Bitmap[numberOfBitmaps];
		texturebmp = new Bitmap[numberOfBitmaps];
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boardType = prefs.getInt(Constants.prefBoardSize, Constants.board5by8);

		switch(boardType) {
		case Constants.board5by8:
			columns = 5;
			rows = 8;
			break;
		case Constants.board5by6:
			columns = 5;
			rows = 6;
			break;
		case Constants.board5by4:
			columns = 5;
			rows = 4;
			break;
		case Constants.board3by6:
			columns = 3;
			rows = 6;
			break;
		case Constants.board3by4:
			columns = 3;
			rows = 4;
			break;
		}
		
		twoPlayers = prefs.getBoolean(Constants.prefTwoPlayers, false);
		computerPlayer = prefs.getBoolean(Constants.prefComputerPlayer, false);

		soundEnabled = prefs.getBoolean(Constants.prefSoundEffects, true);
		
        fileIO = new AndroidFileIO(getAssets());
	}
	
	public void loadTexturesFromDisk() {
		for (int i = 0; i < numberOfBitmaps; i++) {
			InputStream in = null;
			try {
				in = fileIO.readAsset("texture" + i);
				texturebmp[i] = BitmapFactory.decodeStream(in);
			} catch (IOException e) {
				throw new RuntimeException("Couldn't load textures");
			} finally {
				if (in != null)
					try { in.close(); } catch (IOException e) { }
			}
		}
	}

	public void loadBitmapsFromDisk() {
		InputStream in = null;
		try {
			in = fileIO.readFile("closedtile");
			closedTile = BitmapFactory.decodeStream(in);

			for (int i = 0; i < numberOfBitmaps; i++) {
				in = null;
				in = fileIO.readFile("bitmap" + i);
				bitmaps[i] = BitmapFactory.decodeStream(in);
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmaps");
		} finally {
			if (in != null)
				try { in.close(); } catch (IOException e) {	}
		}
	}
	
	public void loadBitmapsFromAssets() {
		InputStream in = null;
		try {
			in = fileIO.readAsset("closedtile");
			closedTile = BitmapFactory.decodeStream(in);

			for (int i = 0; i < numberOfBitmaps; i++) {
				in = null;
				in = fileIO.readAsset("bitmap" + i);
				bitmaps[i] = BitmapFactory.decodeStream(in);
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmaps");
		} finally {
			if (in != null)
				try { in.close(); } catch (IOException e) {	}
		}		
	}

	
	public void setupBitmapTexture(int i) {
		int width;	
		int length = width = 128;
		Bitmap bmpclosed = Bitmap.createScaledBitmap(closedTile, width, length, false);
		Bitmap bmp = Bitmap.createScaledBitmap(bitmaps[i], width, length, false);
		Bitmap newbmp = Bitmap.createBitmap(width*2,length, bmp.getConfig());
		Canvas canvas = new Canvas(newbmp);
		canvas.drawBitmap(bmp,
				new Rect(0,0, bmp.getWidth(),bmp.getHeight()),
				new Rect(0,0, width,length),
				null);
		canvas.drawBitmap(bmpclosed,
				new Rect(0,0, bmp.getWidth(),bmp.getHeight()),
				new Rect(width,0, width*2,length),
				null);
		
		texturebmp[i] = newbmp;
	}
	
	public void setupBitmapTextures() {
		
		for (int i = 0; i < numberOfBitmaps; i++) {
			Bitmap bmpclosed = Bitmap.createScaledBitmap(closedTile, 64, 64, false);
			Bitmap bmp = Bitmap.createScaledBitmap(bitmaps[i], 64, 64, false);
			Bitmap newbmp = Bitmap.createBitmap(128,64, bmp.getConfig());
			Canvas canvas = new Canvas(newbmp);
			canvas.drawBitmap(bmp,
					new Rect(0,0, bmp.getWidth(),bmp.getHeight()),
					new Rect(0,0, 64,64),
					null);
			canvas.drawBitmap(bmpclosed,
					new Rect(0,0, bmp.getWidth(),bmp.getHeight()),
					new Rect(64,0, 128,64),
					null);
			
			texturebmp[i] = newbmp;
		}
	}

	public void saveBitmapTextures() {
		for (int i = 0; i < numberOfBitmaps; i++) {
			OutputStream os = null;
			try {
				os = fileIO.writeFile("texture"+i);
				if (!texturebmp[i].compress(CompressFormat.PNG, 100, os)) {
					throw new IOException();
				}
			} catch(IOException e) {
	            AppLogger.e("Error writing to external storage");
	        } finally {
	            if(os != null)
	                try { os.close(); } catch (IOException e) { }
	        }
		}
	}
	
	public void saveBitmapTexture(int i) {
		OutputStream os = null;
		try {
			os = fileIO.writeFile("texture"+i);
			if (!texturebmp[i].compress(CompressFormat.PNG, 100, os)) {
				throw new IOException();
			}
		} catch(IOException e) {
            AppLogger.e("Error writing to external storage");
        } finally {
            if(os != null)
                try { os.close(); } catch (IOException e) { }
        }
	}
	
	public void saveBitmaps() {
		OutputStream os = null;
		
		try {
			os = fileIO.writeFile("closedtile");
			if (!closedTile.compress(CompressFormat.PNG, 100, os)) {
				throw new IOException();
			}
			for (int i = 0; i < numberOfBitmaps; i++) {
				os = null;
				os = fileIO.writeFile("bitmap" + i);
				if (!bitmaps[i].compress(CompressFormat.PNG, 100, os)) {
					throw new IOException();
				}
			}
		} catch (IOException e) {
			AppLogger.e("Error writing to external storage");
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
				}
		}
	}

	public void saveBitmap(int i) {
		OutputStream os = null;
		try {
			os = fileIO.writeFile("bitmap"+i);
			if (!bitmaps[i].compress(CompressFormat.PNG, 100, os)) {
				throw new IOException();
			}
		} catch(IOException e) {
            AppLogger.e("Error writing to external storage");
        } finally {
            if(os != null)
                try { os.close(); } catch (IOException e) { }
        }
	}

}
