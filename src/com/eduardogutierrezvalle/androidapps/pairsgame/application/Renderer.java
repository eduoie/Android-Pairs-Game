package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import javax.microedition.khronos.opengles.GL10;

import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.CardView;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.GLGraphics;

public class Renderer {

	GLGraphics glGraphics;
    CardView cards[];
    int rows;
    int columns;

    public Renderer(MainApplication application, GLGraphics glGraphics, GameState board) {
    	this.glGraphics = glGraphics;
    	this.rows = application.rows;
    	this.columns = application.columns;
        cards = new CardView[rows * columns];

        int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cards[count++] = new CardView(glGraphics.getGL(),
						application.texturebmp[board.board[i][j]]);
			}
		}
    }
    
    public void renderCard(int count) {
    	cards[count].draw();
    }

	public void presentLoadCards(GL10 gl, float deltaTime, float worldHeight, float angle) {
		
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				
		        gl.glPushMatrix();
		        gl.glTranslatef(1 + j*2, (worldHeight-1) - i*2, 0);
		        gl.glRotatef(angle, 0, 1, 0);
		        renderCard(count);
		        gl.glPopMatrix();
		        count++;
			}
		}
	}
}
