package com.eduardogutierrezvalle.androidapps.pairsgame.framework;

import com.eduardogutierrezvalle.androidapps.pairsgame.glgraphics.GLGraphics;

public interface Game {
    public Input getInput();

    public FileIO getFileIO();

    public GLGraphics getGraphics();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}