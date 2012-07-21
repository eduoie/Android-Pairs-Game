package com.eduardogutierrezvalle.androidapps.pairsgame.framework;

import com.eduardogutierrezvalle.androidapps.pairsgame.application.GLGame;
import com.eduardogutierrezvalle.androidapps.pairsgame.application.MainApplication;

public class Assets {

	public static Music music;
	public static Sound turnCardSound;
	public static Sound popSound;

	public static void load(GLGame game) {
		// music = game.getAudio().newMusic("music.mp3");
		// music.setLooping(true);
		// music.setVolume(0.5f);
		// if(Settings.soundEnabled)
		// music.play();
		turnCardSound = game.getAudio().newSound("swoosh.mp3");
		popSound = game.getAudio().newSound("pop.mp3");
	}

	public static void reload() {
		// if(Settings.soundEnabled)
		// music.play();
	}

	public static void playSound(Sound sound) {
		if (MainApplication.soundEnabled)
			sound.play(1);
	}
}
