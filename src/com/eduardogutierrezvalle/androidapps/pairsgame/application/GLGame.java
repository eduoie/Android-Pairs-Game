package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.activities.BaseActivity;
import com.eduardogutierrezvalle.androidapps.pairsgame.application.GameScreen.ScreenListener;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.AndroidAudio;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.AndroidInput;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Audio;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.FileIO;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Game;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Input;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Screen;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.GLGraphics;

public abstract class GLGame extends BaseActivity implements Game, Renderer {
    enum GLGameState {
        Initialized,
        Running,
        Paused,
        Finished,
        Idle
    }
    
    TextView textPlayer1, textPlayer2, textTurn;
    
    GLSurfaceView glView;    
    GLGraphics glGraphics;
    Audio audio;
    Input input;
    Screen screen;
    GLGameState state = GLGameState.Initialized;
    Object stateChanged = new Object();
    long startTime = System.nanoTime();
    WakeLock wakeLock;
    ScreenListener listener;
    
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
//        glView = new GLSurfaceView(this);
//        glView.setRenderer(this);
//        setContentView(glView);

        setContentView(R.layout.surface_overlay);
        glView = (GLSurfaceView) findViewById(R.id.glsurfaceview);
        glView.setRenderer(this);
        
        textPlayer1 = (TextView) findViewById(R.id.textPlayer1);
        textPlayer2 = (TextView) findViewById(R.id.textPlayer2);
        if (application.twoPlayers == false) 
        	textPlayer2.setText("");
        textTurn = (TextView) findViewById(R.id.textPlayerTurn);

        // as it is invoked from a GLSurfaceView thread,
        // a runOnUiThread is required
        listener = new ScreenListener() {
			public void updateTurn(final String turn) {
				runOnUiThread(new Runnable() {
					public void run() {
						textTurn.setText(turn);
					}
				});
			}
			public void updateScorePlayer2(final int score) {
				runOnUiThread(new Runnable() {
					public void run() {
						textPlayer2.setText(MainApplication.getAppContext().getString(R.string.player2_score) + score);
					}
				});
			}
			public void updateScorePlayer1(final int score) {
				runOnUiThread(new Runnable() {
					public void run() {
						textPlayer1.setText(MainApplication.getAppContext().getString(R.string.player1_score) + score);
					}
				});
			}
		};
        
        glGraphics = new GLGraphics(glView);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");     
        
        Toast.makeText(application, R.string.how_to_play, 2).show();
        
    }
    
    public void onResume() {
        super.onResume();
        glView.onResume();
        wakeLock.acquire();
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {        
        glGraphics.setGL(gl);
        
        synchronized(stateChanged) {
            if(state == GLGameState.Initialized) {
                screen = getStartScreen();
                ((GameScreen)screen).listener = listener;
            }
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }        
    }
    
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {        
    }

    @Override
    public void onDrawFrame(GL10 gl) {                
        GLGameState state = null;
        
        synchronized(stateChanged) {
            state = this.state;
        }
        
        if(state == GLGameState.Running) {
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            
            screen.update(deltaTime);
            screen.present(deltaTime);
        }
        
        if(state == GLGameState.Paused) {
            screen.pause();            
            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        }
        
        if(state == GLGameState.Finished) {
            screen.pause();
            screen.dispose();
            synchronized(stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }            
        }
    }   
    
    @Override 
    public void onPause() {        
        synchronized(stateChanged) {
            if(isFinishing())            
                state = GLGameState.Finished;
            else
                state = GLGameState.Paused;
            while(true) {
                try {
                    stateChanged.wait();
                    break;
                } catch(InterruptedException e) {         
                }
            }
        }
        wakeLock.release();
        glView.onPause();  
        super.onPause();
    }    
    
    public GLGraphics getGLGraphics() {
        return glGraphics;
    }  
    
    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return application.fileIO;
    }

    @Override
    public GLGraphics getGraphics() {
        throw new IllegalStateException("We are using OpenGL!");
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }  
    
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            finish();
			            break;

			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to abandon?").setPositiveButton("Yes", dialogClickListener)
			    .setNegativeButton("No", dialogClickListener).show();
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
