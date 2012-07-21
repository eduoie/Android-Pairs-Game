package com.eduardogutierrezvalle.androidapps.pairsgame.framework;

import com.eduardogutierrezvalle.androidapps.pairsgame.application.GLGame;
import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.GLGraphics;


public abstract class GLScreen extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics = ((GLGame)game).getGLGraphics();
    }

}
