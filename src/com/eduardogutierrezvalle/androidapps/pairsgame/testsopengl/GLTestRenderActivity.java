package com.eduardogutierrezvalle.androidapps.pairsgame.testsopengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.eduardogutierrezvalle.androidapps.pairsgame.activities.BaseActivity;

public class GLTestRenderActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
 		GLSurfaceView view = new GLSurfaceView(this);
   		view.setRenderer(new OpenGLRenderer(application));
   		setContentView(view);
    }
}
