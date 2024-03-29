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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class IntroScreen implements Updatable, Drawable, Disposable, Touchable {

	public static final int VIEW_SPLASH_HOALW = 0, VIEW_SPLASH_DOGCHIKEN = 1,
			VIEW_SPLASH_BUCH = 2, VIEW_MENU = 3;

	private final CharSequence[] strings = { "HoaLW", "Productions", "Skip",
			"", "DogChicken", "Buch", "@OpenGameArt" };
	private float[] widths;

	// Sound in android may not be able to play onCreate or at least not right
	// after creation due to SoundPool implementation or LibGDX?
	private Music introsition, wrath;
	public Music music;
	private Sound gruff;
	private TextureAtlas atlas;
	private AtlasRegion dogChikenRegion, buchRegion;
	private StringTyper typer;
	private Color color, skipColor;
	private ShapeRenderer shape;
	private Rectangle splashRect, skipRect;
	public IntroMenuManager menu;

	public int view;
	private int phase;
	private long phaseEndTime;

	public IntroScreen() {
		initResources();
		color = new Color(Color.CLEAR);
		skipColor = new Color(Color.WHITE);
		widths = new float[strings.length];
		for (int i = 0; i < widths.length; i++)
			widths[i] = KFNekko.resource.chunkFive.getBounds(strings[i]).width;
		typer = new StringTyper(strings[0], 300000000L);
		shape = new ShapeRenderer();
		splashRect = new Rectangle();
		skipRect = new Rectangle(8.0f, KFNekko.settings.viewHeight * 0.90f,
				widths[2],
				KFNekko.resource.chunkFive.getBounds(strings[2]).height);
		reset();
	}

	private void initResources() {
		atlas = new TextureAtlas(Gdx.files.internal("texture/intro.pack"));
		dogChikenRegion = atlas.findRegion("dogchiken");
		buchRegion = atlas.findRegion("buch");
		introsition = Gdx.audio.newMusic(Gdx.files
				.internal("sound/introsition.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music/intro.mp3"));
		music.setLooping(true);
		gruff = Gdx.audio.newSound(Gdx.files.internal("sound/gruff.mp3"));
		wrath = Gdx.audio
				.newMusic(Gdx.files.internal("sound/wrathOfMagic.mp3"));
		menu = new IntroMenuManager(atlas);
	}

	/**
	 * Reset/begin everything.
	 */
	public void reset() {
		color.set(Color.CLEAR);
		phase = 0;
		view = VIEW_SPLASH_HOALW;
		typer.reset(strings[0], 100000000L);
		splashRect.set(KFNekko.camera.rect.x, KFNekko.camera.camera.position.y
				- splashRect.height * 0.5f, 0, 2.0f);
		if (KFNekko.settings.soundOn) {
			introsition.play();
			wrath.play();
		}
	}

	@Override
	public void update() {
		switch (view) {
		case VIEW_SPLASH_HOALW:
			updateHoaLWSplash();
			if ((skipColor.a += 0.02f) > 1.0f)
				skipColor.a -= 1.0f;
			break;
		case VIEW_SPLASH_DOGCHIKEN:
			updateDogChikenSplash();
			if ((skipColor.a += 0.02f) > 1.0f)
				skipColor.a -= 1.0f;
			break;
		case VIEW_SPLASH_BUCH:
			updateBuchSplash();
			if ((skipColor.a += 0.02f) > 1.0f)
				skipColor.a -= 1.0f;
			break;
		case VIEW_MENU:
			menu.update();
			break;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		switch (view) {
		case VIEW_SPLASH_HOALW:
			// forgetting to set the projection matrix may (most likely)
			// have been the cause of the bug in the Intro!
			shape.setProjectionMatrix(KFNekko.camera.camera.combined);
			/////////
			shape.begin(ShapeType.FilledRectangle);
			if (phase == 2)
				shape.setColor(color.a, color.a, color.a, color.a);
			else
				shape.setColor(Color.WHITE);
			shape.filledRect(splashRect.x, splashRect.y, splashRect.width,
					splashRect.height);
			shape.setColor(Color.BLACK);
			shape.filledRect(splashRect.x, 0.0f, KFNekko.settings.viewWidth,
					70.0f);
			shape.filledRect(splashRect.x, KFNekko.settings.viewHeight - 70.0f,
					KFNekko.settings.viewWidth, 70.0f);
			shape.end();

			batch.begin();
			KFNekko.resource.chunkFive.setColor(skipColor);
			KFNekko.resource.chunkFive.draw(batch, strings[2], skipRect.x,
					skipRect.y + skipRect.height);
			KFNekko.resource.chunkFive.setColor(color);
			KFNekko.resource.chunkFive.draw(batch, typer.getTypedStr(),
					KFNekko.camera.camera.position.x - widths[0] * 0.5f,
					KFNekko.camera.camera.position.y + 30.0f);
			KFNekko.resource.chunkFive.draw(batch, strings[1],
					KFNekko.camera.camera.position.x - widths[1] * 0.5f,
					KFNekko.camera.camera.position.y - 5.0f);
			batch.end();
			break;
		case VIEW_SPLASH_DOGCHIKEN:
			batch.begin();
			batch.setColor(color);
			batch.draw(dogChikenRegion, 0.0f, 0.0f);
			KFNekko.resource.chunkFive.setColor(skipColor);
			KFNekko.resource.chunkFive.draw(batch, strings[2], skipRect.x,
					skipRect.y + skipRect.height);
			KFNekko.resource.chunkFive.setColor(0.98f, 0.31f, 0.262f, color.a);
			KFNekko.resource.chunkFive.draw(batch, typer.getTypedStr(), 20.0f,
					KFNekko.camera.camera.position.y + 15.0f);
			KFNekko.resource.chunkFive.draw(batch, strings[6],
					KFNekko.camera.camera.position.x - widths[6] * 0.5f, 60.0f);
			batch.end();
			break;
		case VIEW_SPLASH_BUCH:
			batch.begin();
			batch.setColor(color);
			batch.draw(buchRegion, 0.0f, 0.0f);
			KFNekko.resource.chunkFive.setColor(skipColor);
			KFNekko.resource.chunkFive.draw(batch, strings[2], skipRect.x,
					skipRect.y + skipRect.height);
			KFNekko.resource.chunkFive.setColor(0.5764f, 0.2549f, 0.0235f,
					color.a);
			KFNekko.resource.chunkFive.draw(batch, typer.getTypedStr(),
					KFNekko.camera.camera.position.x + 120.0f,
					KFNekko.camera.camera.position.y + 130.0f);
			KFNekko.resource.chunkFive.draw(batch, strings[6],
					KFNekko.camera.camera.position.x - widths[6] * 0.5f, 60.0f);
			batch.end();
			break;
		case VIEW_MENU:
			menu.draw(batch);
			break;
		}
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		switch (view) {
		case VIEW_SPLASH_HOALW:
		case VIEW_SPLASH_DOGCHIKEN:
		case VIEW_SPLASH_BUCH:
			if (skipRect.contains(x, y)) {
				KFNekko.audio.touch();
				view = VIEW_MENU;
				if (KFNekko.settings.musicOn)
					music.play();
			}
			return true;
		case VIEW_MENU:
			return menu.onTouchDown(x, y);
		}
		return false;
	}

	@Override
	public void dispose() {
		atlas.dispose();
		introsition.dispose();
		shape.dispose();
		music.dispose();
		gruff.dispose();
	}

	private boolean updateColor(float colorSpeed) {
		switch (phase) {
		case 0:
			color.r += colorSpeed;
			color.g += colorSpeed;
			color.b += colorSpeed;
			color.a += colorSpeed;
			if (color.a > 1.0f) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 1;
				color.r = 1.0f;
				color.g = 1.0f;
				color.b = 1.0f;
				color.a = 1.0f;
				return true;
			}
			break;
		case 1:
			if (TimeUtils.nanoTime() - phaseEndTime > 2000000000L) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 2;
				return true;
			}
			break;
		case 2:
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

	private void updateBuchSplash() {
		switch (phase) {
		case 0:
			typer.update();
			if (updateColor(0.04f) && KFNekko.settings.soundOn)
				gruff.play(1.0f, 1.0f, Audio
						.getSoundPan(KFNekko.camera.camera.position.x - 40.0f));
			break;
		case 1:
			typer.update();
			if (updateColor(0.04f))
				// make sure typer is finished
				typer.finish();
			break;
		case 2:
			if (updateColor(0.04f)) {
				phase = 0;
				view = VIEW_MENU;
				if (KFNekko.settings.musicOn)
					music.play();
			}
		}
	}

	private void updateDogChikenSplash() {
		switch (phase) {
		case 0:
			typer.update();
			if (updateColor(0.04f)) {
				KFNekko.audio.meow(KFNekko.camera.camera.position.x + 40.0f);
			}
			break;
		case 1:
			typer.update();
			if (updateColor(0.04f))
				// make sure typer is finished
				typer.finish();

			break;
		case 2:
			if (updateColor(0.04f)) {
				phase = 0;
				typer.reset(strings[5], 300000000L);
				view = VIEW_SPLASH_BUCH;
			}
		}
	}

	private void updateHoaLWSplash() {
		switch (phase) {
		case 0:
			splashRect
					.set(KFNekko.camera.rect.x,
							KFNekko.camera.camera.position.y
									- splashRect.height * 0.5f,
							splashRect.width + 10.0f, 2.0f);
			typer.update();
			if (updateColor(0.02f)) {
				// make sure typer is finished
				typer.finish();
				// make sure width is full
				splashRect.width = KFNekko.settings.viewWidth;
			}
			break;
		case 1:
			if ((color.r -= 0.02f) < 0.0f)
				color.r = 0.0f;
			if ((color.g -= 0.02f) < 0.0f)
				color.g = 0.0f;
			if ((color.b -= 0.02f) < 0.0f)
				color.b = 0.0f;
			splashRect.height += 4.0f;
			splashRect.y = KFNekko.camera.camera.position.y - splashRect.height
					* 0.5f;
			updateColor(0.02f);
			break;
		case 2:
			if (updateColor(0.02f)) {
				phase = 0;
				typer.reset(strings[4], 100000000L);
				view = VIEW_SPLASH_DOGCHIKEN;
			}
		}
	}

}
