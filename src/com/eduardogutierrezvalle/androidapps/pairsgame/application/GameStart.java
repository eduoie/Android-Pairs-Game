package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Assets;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Screen;


public class GameStart extends GLGame {
    boolean firstTimeCreate = true;
    
    @Override
    public Screen getStartScreen() {
        return new GameScreen(this, this.application);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
  //          Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
//        if(Settings.soundEnabled)
//            Assets.music.pause();
    }
}