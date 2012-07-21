package com.eduardogutierrezvalle.androidapps.pairsgame.application;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.app.Application;

import com.eduardogutierrezvalle.androidapps.pairsgame.R;
import com.eduardogutierrezvalle.androidapps.pairsgame.application.GameState.Turn;
import com.eduardogutierrezvalle.androidapps.pairsgame.data.Constants;
import com.eduardogutierrezvalle.androidapps.pairsgame.data.Position;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Assets;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.GLScreen;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Game;
import com.eduardogutierrezvalle.androidapps.pairsgame.framework.Input.TouchEvent;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.CardView;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.FPSCounter;
import com.eduardogutierrezvalle.androidapps.pairsgame.utils.AppLogger;


public class GameScreen extends GLScreen {

    public interface ScreenListener {
        public void updateScorePlayer1(int score);
        public void updateScorePlayer2(int score);
        public void updateTurn(String turn);
    }
	
    ScreenListener listener;
    
	static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
    
    static final int CHECK_STATE = 5;
    static final int ALLOW_SELECTION = 6;
    static final int AUTOANIM_RUN = 7;
    static final int AUTOANIM_HIDING = 8;
    static final int DRAGGING = 9;
    
    int state, displayState;
    int rows, columns;

    GameState board;
    // when selecting and flipping cards, it is not the same the state of the board to the cards
    // that are currently showing.
    boolean cardsShowing[][];
    
    FPSCounter fpsCounter;

    Renderer renderer;
    
//    int currentAnimColumn, currentAnimRow, pendingAnim;
//    float currentCardX, currentCardY;
//    boolean startNewCard;

    // used to rotate cards
    float startingAnimAngle;
    float angle;
    float startDragX, startDragY, currentDragX, currentDragY;
    float angleX, angleY;
    float scale;
    boolean scaleUp;
    
    // used to select cards while touching
    int touchColumn, touchRow;
    boolean firstTouch;
	int card1Row = -1;
	int card1Column = -1;
	int card1Id;
	int card2Row = -1;
	int card2Column = -1;
	int card2Id;

	ComputerPlayer computerPlayer;
	
	// display settings variables
    float worldWidth, worldHeight, cardWidth;
    
    public GameScreen(Game game, MainApplication application) {
        super(game);

        rows = application.rows;
        columns = application.columns;
        board = new GameState(rows,columns);
        board.generateRandomBoard();
        board.turn = Turn.player1; 
        
        // used for testing
//        board.generateSequentialBoard();      
//        board.setAllDiscovered(true);

        renderer = new Renderer(application, glGraphics, board);
        
        cardsShowing = new boolean[rows][columns];
        setAllVisible(true);
        
        
		// set for starting animations.
        state = GAME_READY;
        
        cardWidth = 2; // coordinates range from -1 to 1
        switch (application.boardType) {
        case Constants.board5by8:
        	worldWidth = 10;
        	worldHeight = 16;
        	break;
        case Constants.board5by6:
        	worldWidth = 11;
        	worldHeight = 15;
        	break;
        case Constants.board5by4:
        	worldWidth = 10;
        	worldHeight = 8;
        	break;
        case Constants.board3by6:
        	worldWidth = 6;
        	worldHeight = 12;
        	break;
        case Constants.board3by4:
        	worldWidth = 6;
        	worldHeight = 8;
        	break;
        }
        
		if (application.computerPlayer) {
			computerPlayer = new ComputerPlayer(board);
		}

        firstTouch = true;
        
        fpsCounter = new FPSCounter();
        
    }

	boolean isCardVisible(int row, int column) {
	    return cardsShowing[row][column];
	}

	void setCardVisible(int row, int column) {
	    cardsShowing[row][column] = true;
	}

	void setCardInvisible(int row, int column) {
	    cardsShowing[row][column] = false;
	}

	public void setAllVisible(boolean isVisible) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				cardsShowing[i][j] = isVisible;
	}
	
	@Override
	public void update(float deltaTime) {
//	    if(deltaTime > 0.1f)
//	        deltaTime = 0.1f;
	    
	    switch(state) {
	    case GAME_READY:
	        updateReady(deltaTime);
	        break;
	    case GAME_RUNNING:
	        updateRunning(deltaTime);
	        break;
	    case GAME_PAUSED:
	        updatePaused();
	        break;
	    case GAME_LEVEL_END:
	        updateLevelEnd();
	        break;
	    case GAME_OVER:
	        updateGameOver(deltaTime);
	        break;
	    }
	}
	
	// used for starting animation, once cards are flipped over, game can start
	private void updateReady(float deltaTime) {
		if (startingAnimAngle < 90)
			startingAnimAngle += 75 * deltaTime;
		if (startingAnimAngle >= 90)
			startingAnimAngle += 75 * deltaTime;
		if (startingAnimAngle >= 180) {
			state = GAME_RUNNING;
			setAllVisible(false);
			displayState = ALLOW_SELECTION;
		}
	}
	
	private void updateRunning(float deltaTime) {
		
		if (displayState == AUTOANIM_RUN) {
        	if (angleX > 0)
        		angleX += 140 * deltaTime;
        	else
        		angleX -= 140 * deltaTime;

        	if (Math.abs(angleX) >= 180) {
       			displayState = CHECK_STATE;
    			setCardVisible(touchRow, touchColumn);
        	}
		} else if (displayState == AUTOANIM_HIDING) {
        	angle -= 180 * deltaTime;
        	if (angle <= 0) {
        		displayState = ALLOW_SELECTION;
        	}
		} else if (displayState == DRAGGING) {
        	float dragX = currentDragX - startDragX;
        	float dragY = currentDragY - startDragY;
        	startDragX = currentDragX;
        	startDragY = currentDragY;
        	float maxDrag = Math.abs(dragX) > Math.abs(dragY) ? dragX : dragY;

        	AppLogger.i("maxDrag: " +maxDrag);
        	angleX -= maxDrag;
        	AppLogger.i("Angle: " +angleX);
        	// once the front of the card is slightly visible, turns to automatic animation to prevent player cheating.
        	if (Math.abs(angleX) > 90) {
         		displayState = AUTOANIM_RUN;
         		Assets.playSound(Assets.turnCardSound);
        	}
		} else if (displayState == CHECK_STATE) {
			if (firstTouch) {
				card1Row = touchRow;
				card1Column = touchColumn;
				card1Id = board.board[card1Row][card1Column];
				displayState = ALLOW_SELECTION;
				firstTouch = false;
			} else {
				card2Row = touchRow;
				card2Column = touchColumn;
				card2Id = board.board[card2Row][card2Column];
				firstTouch = true;
				
				if (card1Id == card2Id) {
					Assets.playSound(Assets.popSound);
					board.setCardDiscovered(card1Row, card1Column);
					board.setCardDiscovered(card2Row, card2Column);
					
					board.pending -= 2;
					if (board.turn == Turn.player1) {
						board.player1Score += 2;
						listener.updateScorePlayer1(board.player1Score);
					}
					else {
						board.player2Score += 2;
						listener.updateScorePlayer2(board.player2Score);
					}
					
					if (board.pending == 0) {
						board.gameOver = true;
						String winner;
						if (board.player1Score > board.player2Score)
							winner = MainApplication.getAppContext().getString(R.string.player1_wins);
						else if (board.player2Score > board.player1Score)
							winner = MainApplication.getAppContext().getString(R.string.player2_wins);
						else
							winner = MainApplication.getAppContext().getString(R.string.tie_game);
						listener.updateTurn(winner);
					}
					else {
						// TODO deberia poner un displayState DISCOVERED que espere un segundo o dos y que saque algo chulo por pantalla
						displayState = ALLOW_SELECTION;
					}
					
				} else {
					setCardInvisible(card1Row, card1Column);
					setCardInvisible(card2Row, card2Column);
					displayState = AUTOANIM_HIDING;
					angle = 180;
				}
				
				if (glGame.application.twoPlayers && !board.gameOver) {
					if (board.turn == Turn.player1) {
						board.turn = Turn.player2;
						listener.updateTurn(MainApplication.getAppContext().getString(R.string.player2_turn));
					} else {
						board.turn = Turn.player1;
						listener.updateTurn(MainApplication.getAppContext().getString(R.string.player1_turn));
					}
				}
			}
		}
		
		if (glGame.application.computerPlayer && board.turn == Turn.player2) {
			if (displayState == ALLOW_SELECTION) {
				if (firstTouch) {
					// computer choice
					Position lastPlayerCard1 = new Position(card1Row, card1Column, board.board[card1Row][card1Column]);
					Position lastPlayerCard2 = new Position(card2Row, card2Column, board.board[card2Row][card2Column]);
					computerPlayer.computerPlay(lastPlayerCard1, lastPlayerCard2);
					touchRow = computerPlayer.card1Row;
					touchColumn = computerPlayer.card1Column;
					displayState = AUTOANIM_RUN;
	         		Assets.playSound(Assets.turnCardSound);
					angleX = 0;
				} else {
					touchRow = computerPlayer.card2Row;
					touchColumn = computerPlayer.card2Column;
					displayState = AUTOANIM_RUN;
	         		Assets.playSound(Assets.turnCardSound);
					angleX = 0;
				}
			}
			
		} else if (displayState == ALLOW_SELECTION || displayState == DRAGGING) {
	    	List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    	int len = touchEvents.size();
	    	for(int i = 0; i < len; i++) {
	    		TouchEvent event = touchEvents.get(i);
		        if (event.type == TouchEvent.TOUCH_DOWN) {
		        	AppLogger.i("Down: " + event.x + "," + event.y);
		        	touchColumn = (int) ((float)event.x * worldWidth / glGraphics.getWidth() / cardWidth);
		        	touchRow = (int) ((float) event.y * worldHeight / glGraphics.getHeight() / cardWidth);
		        	if (touchRow >= rows || touchColumn >= columns)
		        		continue;
		        	if (!board.isCardDiscovered(touchRow, touchColumn) &&
		        		!isCardVisible(touchRow, touchColumn)) {
		        		displayState = DRAGGING;
		        		currentDragX = startDragX = event.x;
		        		currentDragY = startDragY = event.y;
		        		angleX = 0;
		        		angleY = 0;
		        		AppLogger.i("columna y fila: " + touchColumn + "," + touchRow);
		        	}
		        } else if (event.type == TouchEvent.TOUCH_UP) {
		        	AppLogger.i("Up: " + event.x + "," + event.y);
		        	displayState = ALLOW_SELECTION;
		        } else if (event.type == TouchEvent.TOUCH_DRAGGED) {
		        	AppLogger.i("Drag: " + event.x + "," + event.y);
		        	currentDragX = event.x;
		        	currentDragY = event.y;
		        }
	        }
	    }
	    
	    if(board.gameOver) {
	        state = GAME_OVER;
	        scale = 1;
	        scaleUp = true;
	    }
	}
	
	private void updatePaused() {
	}
	
	private void updateLevelEnd() {
	}
	
	private void updateGameOver(float deltaTime) {
    	angle += 90 * deltaTime;
    	if (scaleUp)
    		scale += 0.25 * deltaTime;
    	else
    		scale -= 0.25 * deltaTime;
    	if (scale <= 0.65) 
    		scaleUp = true;
    	else if (scale >= 1.2) 
    		scaleUp = false;
	}

	@Override
	public void present(float deltaTime) {
	    GL10 gl = glGraphics.getGL();
	    gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	    
	    setCamera(worldWidth, worldHeight);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        switch(state) {
        case GAME_READY:
	        renderer.presentLoadCards(gl, deltaTime, worldHeight, startingAnimAngle);
	        break;
        case GAME_RUNNING:
	        presentRunning(gl, deltaTime);
	        break;
        case GAME_OVER:
        	presentGameOver(gl, deltaTime);
        }
	    
        gl.glDisable(GL10.GL_BLEND);
      
	    fpsCounter.logFrame();
	}
	

	
	private void presentRunning(GL10 gl, float deltaTime) {

		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
		        gl.glPushMatrix();
		        gl.glTranslatef(1 + j*2, (worldHeight-1) - i*2, 0);
		     	if (isCardVisible(i,j) == false) {
		        	gl.glRotatef(180, 0, 1, 0);
		        }
				if (displayState == AUTOANIM_HIDING) {
					if ((card1Row == i && card1Column == j)
							|| (card2Row == i && card2Column == j)) {
						gl.glRotatef(angle, 0, 1, 0);
					}
				} else if (displayState == AUTOANIM_RUN  && touchColumn == j && touchRow == i) {
		        	gl.glRotatef(-angleX, 0, 1, 0);
		        	
		        } else if (displayState == DRAGGING && touchColumn == j && touchRow == i) {
		        	gl.glRotatef(-angleX, 0, 1, 0);
		        }
		        renderer.renderCard(count);
		        gl.glPopMatrix();
		        count++;
			}
		}
	}

	private void presentGameOver(GL10 gl, float deltaTime) {
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				
		        gl.glPushMatrix();
		        gl.glTranslatef(1 + j*2, (worldHeight-1) - i*2, 0);
		        gl.glScalef(scale, scale, 0);
		        float angle2 = (int) angle;
		        angle2 %= 360;
		        gl.glRotatef(angle2 + (float)count*3, (float)0.25, (float)0.75, (float)0.5);
		        renderer.renderCard(count);
		        gl.glPopMatrix();
		        count++;
			}
		}
	}

    // method will be called each frame to start from a clean slate
    void setCamera(float frustumWidth, float frustumHeight) {
    	GL10 gl = glGraphics.getGL();
    	
    	gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, frustumWidth, 0, frustumHeight, 2, -2);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
	
    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}
