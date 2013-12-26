package com.shenko.golfgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "GolfGame";
		cfg.useGL20 = false;
		cfg.width = 600;
		cfg.height = 812;
		
		new LwjglApplication(new GolfGame(), cfg);
	}
}
