package com.eduardogutierrezvalle.androidapps.pairsgame.testsopengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Card {
	// Our vertices.
	// The order we like to connect them.
	private short[] indices1 = { 0, 1, 2, 0, 2, 3}; 
	private short[] indices2 = {0, 3, 1, 3, 2, 1 };

	private float vertices[] = {
			-1.0f,  1.0f, 0.0f, // 0, Top Left
			-1.0f, -1.0f, 0.0f, // 1, Bottom Left
		 	 1.0f, -1.0f, 0.0f, // 2, Bottom Right
			 1.0f,  1.0f, 0.0f, // 3, Top Right
	};

	float textureCoordinates[] = {
			0.0f, 0.0f,		// top left
			1.0f, 0.0f,		// bottom left
			1.0f, 1.0f,		// bottom right
			0.0f, 1.0f,		// top right
	};	
	
	// Our vertex buffer.
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer1;
	private ShortBuffer indexBuffer2;
	
	Bitmap frontCard, backCard;
	
	public Card(Bitmap front, Bitmap back) {
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices1.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer1 = ibb.asShortBuffer();
		indexBuffer1.put(indices1);
		indexBuffer1.position(0);

		ibb = ByteBuffer.allocateDirect(indices2.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer2 = ibb.asShortBuffer();
		indexBuffer2.put(indices2);
		indexBuffer2.position(0);

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoordinates);
		textureBuffer.position(0);
		
		frontCard = front;
		backCard = back;
		
	}
	
	public void draw(GL10 gl) {
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW); // OpenGL docs
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE); // OpenGL docs
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK); // OpenGL docs

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// OpenGL docs.
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		
		gl.glEnable(GL10.GL_TEXTURE_2D);

		loadTexture(gl, frontCard);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, // OpenGL docs
                                 vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices1.length,// OpenGL docs
				  GL10.GL_UNSIGNED_SHORT, indexBuffer1);

		loadTexture(gl, backCard);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices2.length,// OpenGL docs
				  GL10.GL_UNSIGNED_SHORT, indexBuffer2);

		gl.glDisable(GL10.GL_TEXTURE_2D);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); // OpenGL docs
	}
	
	private void loadTexture(GL10 gl, Bitmap bitmap) {
		int textureIds[] = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		int textureId = textureIds[0];
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}
}
