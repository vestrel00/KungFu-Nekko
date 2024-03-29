/***************************************************************************
 *  Copyright (C) 2012 by Vandolf Estrellado
 *  All Rights Reserved
 * 
 *  This file is part of KungFu Nekko.
 *  KungFu Nekko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KungFu Nekko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KungFu Nekko.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/

package com.vestrel00.nekko;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.Map;
import com.vestrel00.nekko.maps.components.Background;
import com.vestrel00.nekko.ui.HUD;
import com.vestrel00.nekko.ui.components.HUDInputProcessor;

public class KFNekko implements ApplicationListener {

	// the public static modifier without the final modifier is not secure.
	public static final int VIEW_INTRO = 0, VIEW_GAME = 2, VIEW_PAUSED = 3,
			VIEW_OPTIONS = 4, VIEW_LEVEL_INTRO = 5, VIEW_GAME_OVER = 6,
			VIEW_GAME_VICTORY = 7;
	public static int view;

	public static SpriteBatch batch;
	public static Camera camera;
	public static Settings settings;
	public static Background background;
	public static Map map;
	public static IntroScreen intro;
	public static Resource resource;
	public static Audio audio;
	public static HUD hud;
	public static Array<Actor> allies, enemies;
	public static Nekko player;
	public static PauseManager pauseManager;
	public static OptionsManager optionsManager;

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
		enemies = new Array<Actor>();
		allies = new Array<Actor>();
		view = VIEW_INTRO;
		resource = new Resource();
		batch = new SpriteBatch();
		settings = new Settings();
		audio = new Audio();
		camera = new Camera();
		map = new Map();
		background = new Background();
		initPlayer();
		hud = new HUD(player, new HUDInputProcessor(player));
		intro = new IntroScreen();
		pauseManager = new PauseManager();
		optionsManager = new OptionsManager();
		initVars();
		initArrays();
	}

	private void initPlayer() {
		Location location = new Location(0.0f, 0.0f, 9.0f, 22.0f, 100.0f, 18.0f);
		player = new Nekko(resource.atlas, location, enemies, 400, new Color(
				Color.WHITE), 300);
		player.setState(FaceState.RIGHT, StatusState.ALIVE, CombatState.IDLE,
				HorizontalMotionState.IDLE, VerticalMotionState.FALLING);
		// turn camera effects on for this nekko
		player.nekkoSprite.cameraEffect = true;
	}

	private void initVars() {
		worldColor = new Color(0.7f, 0.7f, 0.7f, 1.0f);
		targetWorldColor = new Color(worldColor);
		colorSpeed = 0.01f;
	}

	private void initArrays() {
		updatables = new Array<Updatable>();
		updatables.add(player);
		updatables.add(camera);
		updatables.add(hud);
		updatables.add(map);
		updatables.add(background);

		drawables = new Array<Drawable>();
		drawables.add(background);
		drawables.add(map);
		drawables.add(player);

		disposables = new Array<Disposable>();
		disposables.add(batch);
		disposables.add(resource);
		disposables.add(hud);
		disposables.add(audio);
		disposables.add(intro);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// UPDATE
		if (TimeUtils.nanoTime() - lastUpdateTime > UPDATE_TIME) {
			lastUpdateTime = TimeUtils.nanoTime();
			heapCheck();
			switch (view) {
			case VIEW_INTRO:
				intro.update();
				break;
			case VIEW_LEVEL_INTRO:
			case VIEW_GAME:
				for (int i = 0; i < updatables.size; i++)
					updatables.get(i).update();
				/*
				 * Note about the above. I know how to use iterators but choose
				 * not to because I think using the following methods below
				 * allocates new object in the heap due to usage of abstract
				 * iterators. Thus, i use the integer method to iterate through
				 * all of my loops.
				 */

				// Alternate method 1
				// for (Updatable updatable : updatables)
				// updatable.update();

				// Alternate method 2
				// Iterator<Updatable> iter = updatables.iterator();
				// while (iter.hasNext())
				// iter.next().update();
				// ///////

				Methods.updateColor(worldColor, targetWorldColor, colorSpeed);
				Methods.updateColor(worldColor, targetWorldColor, colorSpeed);
				break;
			case VIEW_GAME_VICTORY:
			case VIEW_GAME_OVER:
				map.manager.update();
				Methods.updateColor(worldColor, targetWorldColor, colorSpeed);
				break;
			case VIEW_PAUSED:
				if (audio.music.isPlaying())
					audio.music.pause();
				pauseManager.update();
				break;
			case VIEW_OPTIONS:
				if (audio.music.isPlaying())
					audio.music.pause();
				pauseManager.update();
				optionsManager.update();
				break;
			}
		}

		// DRAW
		batch.setProjectionMatrix(camera.camera.combined);
		switch (view) {
		case VIEW_INTRO:
			intro.draw(batch);
			break;
		case VIEW_LEVEL_INTRO:
		case VIEW_GAME:
		case VIEW_GAME_VICTORY:
		case VIEW_GAME_OVER:
			batch.setColor(worldColor);
			drawGame();
			break;
		case VIEW_PAUSED:
			batch.setColor(Color.DARK_GRAY);
			drawGame();
			pauseManager.draw(batch);
			break;
		case VIEW_OPTIONS:
			batch.setColor(Color.DARK_GRAY);
			drawGame();
			optionsManager.draw(batch);
			pauseManager.draw(batch);
			break;
		}

	}

	private void drawGame() {
		batch.begin();
		for (int i = 0; i < drawables.size; i++)
			drawables.get(i).draw(batch);
		batch.end();

		// draw separately since this conflicts with the SpriteBatch in use
		// make sure that zoom does not pass a certain amount (1 - 0.065) or
		// (1 + 0.02)
		if (camera.camera.zoom < 0.935f) {
			camera.camera.zoom = 0.935f;
			camera.camera.update();
			batch.setProjectionMatrix(camera.camera.combined);
		} else if (camera.camera.zoom > 1.02f) {
			camera.camera.zoom = 1.02f;
			camera.camera.update();
			batch.setProjectionMatrix(camera.camera.combined);
		}
		hud.draw(batch);
		map.manager.drawText(batch);
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

	public static void bumpWC(float r, float g, float b) {
		worldColor.r += r;
		worldColor.g += g;
		worldColor.b += b;

		// clip
		if (worldColor.r > 1.0f)
			worldColor.r = 1.0f;
		if (worldColor.g > 1.0f)
			worldColor.g = 1.0f;
		if (worldColor.b > 1.0f)
			worldColor.b = 1.0f;
	}

	// ////////////////
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		settings.commit();
		// record high scores regardless of view to make sure it is recorded!
		if (map.manager != null) {
			map.manager.saveHighScores();
			if (view == VIEW_GAME) {
				view = VIEW_PAUSED;
				map.manager.pause();
			}
		}
	}

	@Override
	public void resume() {
		if (map.manager != null) {
			map.manager.resume();
		}
	}

	@Override
	public void dispose() {
		for (int i = 0; i < disposables.size; i++)
			disposables.get(i).dispose();
	}
	// //////////////////

}
