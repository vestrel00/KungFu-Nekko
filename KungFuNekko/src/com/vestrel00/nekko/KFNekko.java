package com.vestrel00.nekko;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.Map;
import com.vestrel00.nekko.maps.components.Background;
import com.vestrel00.nekko.ui.HUD;

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
	public static HUD hud;

	public static final long UPS = 50L;
	public static final long UPDATE_TIME = 1000000000 / UPS;

	private long lastUpdateTime, lastGCTime;

	private static Array<Updatable> updatables;
	private static Array<Drawable> drawables;
	private static Array<Disposable> disposables;

	public static Color worldColor, targetWorldColor;
	public static float colorSpeed;

	@Override
	public void create() {
		view = VIEW_GAME;
		resource = new Resource();
		batch = new SpriteBatch();
		settings = new Settings();
		map = new Map();
		camera = new Camera();
		background = new Background(resource.atlas.findRegion("background"));

		initVars();
		initArrays();
	}

	private void initVars() {
		worldColor = new Color(0.7f, 0.7f, 0.7f, 1.0f);
		targetWorldColor = new Color(worldColor);
		colorSpeed = 0.01f;

		hud = new HUD();
	}

	private void initArrays() {
		updatables = new Array<Updatable>();
		updatables.add(camera);
		updatables.add(hud);
		updatables.add(map);
		updatables.add(background);

		drawables = new Array<Drawable>();
		drawables.add(background);
		drawables.add(map);

		disposables = new Array<Disposable>();
		disposables.add(batch);
		disposables.add(resource);
		disposables.add(hud);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		switch (view) {
		case VIEW_LOADING:
			// TODO
			break;
		case VIEW_GAME:
			// UPDATE game objects
			// put max UPS
			if (TimeUtils.nanoTime() - lastUpdateTime > UPDATE_TIME) {
				lastUpdateTime = TimeUtils.nanoTime();
				heapCheck();
				for (int i = 0; i < updatables.size; i++)
					updatables.get(i).update();
				updateWorldColor();
			}

			// DRAW
			batch.setProjectionMatrix(camera.camera.combined);
			batch.setColor(worldColor);
			batch.begin();
			for (int i = 0; i < drawables.size; i++)
				drawables.get(i).draw(batch);
			batch.end();

			// draw separately since thisconflicts with the SpriteBatch in use
			hud.draw(batch);
			break;
		}

	}

	private void updateWorldColor() {
		if (worldColor.r > targetWorldColor.r
				&& (worldColor.r -= colorSpeed) < targetWorldColor.r)
			worldColor.r = targetWorldColor.r;
		if (worldColor.g > targetWorldColor.g
				&& (worldColor.g -= colorSpeed) < targetWorldColor.g)
			worldColor.g = targetWorldColor.g;
		if (worldColor.b > targetWorldColor.b
				&& (worldColor.b -= colorSpeed) < targetWorldColor.b)
			worldColor.b = targetWorldColor.b;
	}

	/**
	 * If fps ever drops below 27 after 30 seconds from the last call to
	 * System.gc(), we will then again attempt to garbage collect.
	 */
	private void heapCheck() {
		if (Gdx.graphics.getFramesPerSecond() < 27
				&& TimeUtils.nanoTime() - lastGCTime > 30000000000L) {
			lastGCTime = TimeUtils.nanoTime();
			System.gc();
		}
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
