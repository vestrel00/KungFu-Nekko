package com.vestrel00.nekko;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vestrel00.nekko.maps.Map;
import com.vestrel00.nekko.maps.components.Background;

public class KFNekko implements ApplicationListener {

	public static final int VIEW_LOADING = 0;
	public static final int VIEW_GAME = 1;
	public static int view;

	public static SpriteBatch batch;
	public static Camera camera;
	public static Settings settings;
	public static Background background;
	public static Map map;
	public static Resource resource;

	@Override
	public void create() {
		view = VIEW_LOADING;
		resource = new Resource();
		settings = new Settings();
		map = new Map();
		background = new Background(resource.atlas.findRegion("background"));

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// update
		// draw

	}

	// ////////////////
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		resource.dispose();
	}
	// //////////////////

}
