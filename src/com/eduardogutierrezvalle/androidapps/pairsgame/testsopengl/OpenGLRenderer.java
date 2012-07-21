package com.eduardogutierrezvalle.androidapps.pairsgame.testsopengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import com.eduardogutierrezvalle.androidapps.pairsgame.application.MainApplication;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.CardView;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.GLGraphics;
import com.eduardogutierrezvalle.androidapps.pairsgame.utils.AppLogger;

public class OpenGLRenderer implements Renderer {

	MainApplication application;
	
//	Card card;
//	Card card2;
	
	CardView card;
	CardView card2;
	
	float angle;

	public GLGraphics glgraphics;
	
	public OpenGLRenderer(MainApplication application) {
		super();
		this.application = application;
		AppLogger.i(application.closedTile.getHeight()+","+application.closedTile.getWidth());
		Bitmap bmpclosed = Bitmap.createScaledBitmap(application.closedTile, 64, 64, false);
		//card = new Card(application.bitmaps[0], application.closedTile);
		//card2 = new Card(application.bitmaps[1], application.closedTile);
		
		Bitmap bmp = Bitmap.createScaledBitmap(application.bitmaps[1], 64, 64, false);
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
        card = new CardView(glgraphics.getGL(), newbmp);
        card2 = new CardView(glgraphics.getGL(), newbmp);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Translates 5 units into the screen.
		gl.glTranslatef(0, 0, -5); 

		gl.glPushMatrix();
		gl.glTranslatef(2, 0, 0);
		gl.glRotatef(angle, 1, (float)0.5, 0);
		// Draw card.
		card.draw();
		gl.glPopMatrix();
		
		gl.glTranslatef(-2, 0, 0);
		gl.glRotatef(angle/2, 1, 1, 0);
		card2.draw();
		
		angle += 3.0;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		AppLogger.i("Surface size: " + width+","+height);
		gl.glViewport(0, 0, width, height);// OpenGL docs.
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);// OpenGL docs.
		// Reset the projection matrix
		gl.glLoadIdentity();// OpenGL docs.
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 67.0f, (float) width / (float) height, 0.1f,
				10.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);// OpenGL docs.
		// Reset the modelview matrix
		gl.glLoadIdentity();// OpenGL docs.
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  // OpenGL docs.
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);// OpenGL docs.
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);// OpenGL docs.
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);// OpenGL docs.
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);// OpenGL docs.
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, // OpenGL docs.
                          GL10.GL_NICEST);
	}

}
