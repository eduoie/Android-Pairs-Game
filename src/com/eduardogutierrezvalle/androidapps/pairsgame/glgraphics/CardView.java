package com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class CardView {

	// Our vertices.
	// The order we like to connect them.
//	private short[] indices =
//			{ 0, 1, 2, 0, 2, 3, 
//			  0+4, 3+4, 1+4, 3+4, 2+4, 1+4 ,
//			};
//
	private short[] indices =
		{ 0, 1, 2, 0, 2, 3, 
		  0+4, 3+4, 1+4, 3+4, 2+4, 1+4 ,
		  0+8,  1+8,  2+8,  0+8,  2+8,  3+8,
		  0+12, 1+12, 2+12, 0+12, 2+12, 3+12,
		  0+16, 1+16, 2+16, 0+16, 2+16, 3+16,
		  0+20, 1+20, 2+20, 0+20, 2+20, 3+20,
		};

	
//	private float points[] = {
//			-1.0f,  1.0f, 0.0f, 0.0f, 0.0f, // 0, Top Left
//			-1.0f, -1.0f, 0.0f, 0.0f, 1.0f, // 1, Bottom Left
//		 	 1.0f, -1.0f, 0.0f, 0.5f, 1.0f, // 2, Bottom Right
//			 1.0f,  1.0f, 0.0f, 0.5f, 0.0f, // 3, Top Right
//			 
//			-1.0f,  1.0f, 0.0f, 0.5f, 0.0f, // 0, Top Left
//			-1.0f, -1.0f, 0.0f, 0.5f, 1.0f, // 1, Bottom Left
//		 	 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, // 2, Bottom Right
//			 1.0f,  1.0f, 0.0f, 1.0f, 0.0f, // 3, Top Right
//
//	};

	private float points[] = {
			// front of the card
			-1.0f,  1.0f, 0.1f, 0.0f, 0.0f, // 0, Top Left
			-1.0f, -1.0f, 0.1f, 0.0f, 1.0f, // 1, Bottom Left
		 	 1.0f, -1.0f, 0.1f, 0.5f, 1.0f, // 2, Bottom Right
			 1.0f,  1.0f, 0.1f, 0.5f, 0.0f, // 3, Top Right

			// back of the card 
			-1.0f,  1.0f, -0.1f, 0.5f, 0.0f, // 0, Top Left
			-1.0f, -1.0f, -0.1f, 0.5f, 1.0f, // 1, Bottom Left
		 	 1.0f, -1.0f, -0.1f, 1.0f, 1.0f, // 2, Bottom Right
			 1.0f,  1.0f, -0.1f, 1.0f, 0.0f, // 3, Top Right

			// four sides
			// top side
			-1.0f,  1.0f, 0.1f, 0.5f, 0.0f, 
			-1.0f,  1.0f, -0.1f, 0.5f, 1.0f, 
			 1.0f,  1.0f, -0.1f, 1.0f, 1.0f,
  			 1.0f,  1.0f, 0.1f, 1.0f, 0.0f,
			// bottom side
			-1.0f, -1.0f, 0.1f, 0.5f, 0.0f,
			-1.0f, -1.0f, -0.1f, 0.5f, 1.0f,
	 		 1.0f, -1.0f, -0.1f, 1.0f, 1.0f,
			 1.0f, -1.0f, 0.1f, 1.0f, 0.0f,
			// left side
			-1.0f,  1.0f, 0.1f, 0.5f, 0.0f,
			-1.0f,  1.0f, -0.1f, 0.5f, 1.0f,
			-1.0f, -1.0f, -0.1f, 1.0f, 1.0f,
			-1.0f, -1.0f, 0.1f, 1.0f, 0.0f,
			// right side
			 1.0f, -1.0f, 0.1f, 0.5f, 0.0f,
			 1.0f, -1.0f, -0.1f, 0.5f, 1.0f,
		 	 1.0f,  1.0f, -0.1f, 1.0f, 1.0f,
			 1.0f,  1.0f, 0.1f, 1.0f, 0.0f,
	};

	// Our vertex buffer.
	private FloatBuffer vertices;

	// Our index buffer.
	private ShortBuffer indexBuffer;
	
	Bitmap card;
	int VERTEX_SIZE = (3 + 2) * 4;
	GL10 gl;
	int textureId;
	
	public CardView(GL10 gl, Bitmap card) {
		
		// TEMP engordo el ancho de la carta
//		for (int i = 0; i < points.length / 5; i++) {
//			points[(i*5)+2] = (float) (points[(i*5)+2] * 0.3);
//		}
		
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(points.length / 5 * VERTEX_SIZE);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertices = byteBuffer.asFloatBuffer();
		vertices.put( points );
		vertices.flip();
		
		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		this.card = card;
		this.gl = gl;
		int textureIds[] = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, card, 0);

	}
	
	public void draw() {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glFrontFace(GL10.GL_CCW); 
		gl.glEnable(GL10.GL_CULL_FACE); 
		gl.glCullFace(GL10.GL_BACK); 
		
		loadTexture(gl);
		
		vertices.position(0);
		gl.glVertexPointer(3, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
		vertices.position(3);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				  GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glDisable(GL10.GL_CULL_FACE);
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
		
		
	}
	
	private void loadTexture(GL10 gl) {
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}
}


