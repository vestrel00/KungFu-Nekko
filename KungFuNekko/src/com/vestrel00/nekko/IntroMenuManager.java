/*******************************************************************************
 * Copyright 2012 Vandolf Estrellado
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.vestrel00.nekko;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class IntroMenuManager implements Updatable, Drawable, Touchable {

	public static final int VIEW_MENU = 0, VIEW_CREDITS = 1,
			VIEW_SELECTION = 2, VIEW_INSTRUCTIONS = 3;
	private final float COLOR_SPEED = 0.01f;

	private final CharSequence[] strings = { "Play", "Credits", "KungFu Nekko",
			"Instructions" };
	private float[] widths;

	private AtlasRegion baseRegion, buttonRegion, buttonLargeRegion;
	private IntroSelectionManager selection;
	private Instructions instructions;
	private Credits credits;
	public Color color, titleColor, targetTitleColor;
	private Rectangle playRect, creditsRect, instructionsRect;
	private Random rand;
	public int phase = 0, view = VIEW_MENU, nextView;

	public IntroMenuManager(TextureAtlas atlas) {
		rand = new Random();
		baseRegion = atlas.findRegion("base");
		buttonRegion = atlas.findRegion("button_small");
		buttonLargeRegion = atlas.findRegion("button_large");
		color = new Color(Color.CLEAR);
		widths = new float[strings.length];
		for (int i = 0; i < widths.length; i++)
			widths[i] = KFNekko.resource.chunkFive.getBounds(strings[i]).width;
		playRect = new Rectangle(KFNekko.camera.camera.position.x
				- (float) buttonRegion.originalWidth * 0.5f,
				KFNekko.camera.camera.position.y + 20.0f,
				(float) buttonRegion.originalWidth,
				(float) buttonRegion.originalHeight);
		instructionsRect = new Rectangle(KFNekko.camera.camera.position.x
				- (float) buttonLargeRegion.originalWidth * 0.5f, playRect.y
				- playRect.height - 10.0f,
				(float) buttonLargeRegion.originalWidth,
				(float) buttonLargeRegion.originalHeight);
		creditsRect = new Rectangle(playRect.x, instructionsRect.y
				- playRect.height - 10.0f, playRect.width, playRect.height);
		titleColor = new Color(Color.YELLOW);
		targetTitleColor = genColor();
		credits = new Credits();
		instructions = new Instructions();
		selection = new IntroSelectionManager(atlas);
	}

	private Color genColor() {
		return new Color((float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f, 1.0f);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		if (view != VIEW_INSTRUCTIONS) {
			batch.setColor(Color.WHITE);
			batch.draw(baseRegion, 0, 0);
		}
		batch.setColor(color);
		switch (view) {
		case VIEW_MENU:
			batch.draw(buttonRegion, playRect.x, playRect.y, playRect.width,
					playRect.height);
			batch.draw(buttonLargeRegion, instructionsRect.x,
					instructionsRect.y, instructionsRect.width,
					instructionsRect.height);
			batch.draw(buttonRegion, creditsRect.x, creditsRect.y,
					creditsRect.width, creditsRect.height);
			batch.end();

			batch.begin();
			KFNekko.resource.chunkFive.setColor(titleColor.r, titleColor.g,
					titleColor.b, color.a);
			KFNekko.resource.chunkFive.draw(batch, strings[2],
					KFNekko.camera.camera.position.x - widths[2] * 0.5f,
					KFNekko.settings.viewHeight - 7.0f);
			KFNekko.resource.chunkFive
					.setColor(0.4118f, 0.6157f, 1.0f, color.a);
			// PLAY
			KFNekko.resource.chunkFive.draw(batch, strings[0],
					KFNekko.camera.camera.position.x - widths[0] * 0.5f,
					playRect.y + playRect.height - 11.0f);
			// CREDITS
			KFNekko.resource.chunkFive.draw(batch, strings[1],
					KFNekko.camera.camera.position.x - widths[1] * 0.5f,
					creditsRect.y + creditsRect.height - 11.0f);
			// INSTRUCTIONS
			KFNekko.resource.chunkFive.draw(batch, strings[3],
					KFNekko.camera.camera.position.x - widths[3] * 0.5f,
					instructionsRect.y + instructionsRect.height - 11.0f);
			break;
		case VIEW_CREDITS:
			credits.draw(batch);
			break;
		case VIEW_SELECTION:
			KFNekko.resource.chunkFive.setColor(titleColor.r, titleColor.g,
					titleColor.b, color.a);
			KFNekko.resource.chunkFive.draw(batch, strings[2],
					KFNekko.camera.camera.position.x - widths[2] * 0.5f,
					KFNekko.settings.viewHeight - 7.0f);
			selection.draw(batch);

			break;
		case VIEW_INSTRUCTIONS:
			batch.end();
			// draw the hud!
			KFNekko.hud.draw(batch);
			instructions.draw(batch);
			break;
		}
		batch.end();

		// draw the optionsManger!
		// Different textures but no switch since batch ended =)
		if (view == VIEW_MENU)
			KFNekko.optionsManager.draw(batch);
	}

	@Override
	public void update() {
		updateColor(0.04f);
		switch (view) {
		case VIEW_SELECTION:
			updateTitleColor();
			selection.update();
			break;
		case VIEW_CREDITS:
			credits.update();
			break;
		case VIEW_MENU:
			KFNekko.optionsManager.update();
			updateTitleColor();
			break;
		case VIEW_INSTRUCTIONS:
			updateTitleColor();
			instructions.update();
			break;
		}
	}

	private void updateTitleColor() {
		boolean change = false;
		if (titleColor.r > targetTitleColor.r) {
			if ((titleColor.r -= COLOR_SPEED) < targetTitleColor.r)
				change = true;
		} else if (titleColor.r < targetTitleColor.r) {
			if ((titleColor.r += COLOR_SPEED) > targetTitleColor.r)
				change = true;
		}

		if (titleColor.g > targetTitleColor.g) {
			if ((titleColor.g -= COLOR_SPEED) < targetTitleColor.g)
				change = true;
		} else if (titleColor.g < targetTitleColor.g) {
			if ((titleColor.g += COLOR_SPEED) > targetTitleColor.g)
				change = true;
		}

		if (titleColor.b > targetTitleColor.b) {
			if ((titleColor.b -= COLOR_SPEED) < targetTitleColor.b)
				change = true;
		} else if (titleColor.b < targetTitleColor.b) {
			if ((titleColor.b += COLOR_SPEED) > targetTitleColor.b)
				change = true;
		}

		if (change)
			targetTitleColor = genColor();

	}

	@Override
	public boolean onTouchDown(float x, float y) {
		switch (view) {
		case VIEW_MENU:
			if (KFNekko.optionsManager.onTouchDown(x, y))
				return true;
			if (playRect.contains(x, y)) {
				nextView = VIEW_SELECTION;
				phase = 1;
				KFNekko.audio.touch();
				return true;
			} else if (creditsRect.contains(x, y)) {
				nextView = VIEW_CREDITS;
				phase = 1;
				KFNekko.audio.touch();
				return true;
			} else if (instructionsRect.contains(x, y)) {
				nextView = VIEW_INSTRUCTIONS;
				phase = 1;
				KFNekko.audio.touch();
				return true;
			}
			break;
		case VIEW_CREDITS:
			nextView = VIEW_MENU;
			phase = 1;
			KFNekko.audio.touch();
			return true;
		case VIEW_INSTRUCTIONS:
			return instructions.onTouchDown(x, y);
		case VIEW_SELECTION:
			KFNekko.audio.touch();
			if (!selection.onTouchDown(x, y)) {
				phase = 1;
				nextView = VIEW_MENU;
				return false;
			} else
				return true;
		}
		return false;
	}

	private boolean updateColor(float colorSpeed) {
		switch (phase) {
		case 0: // fade in view
			color.r += colorSpeed;
			color.g += colorSpeed;
			color.b += colorSpeed;
			color.a += colorSpeed;
			if (color.a > 1.0f) {
				color.r = 1.0f;
				color.g = 1.0f;
				color.b = 1.0f;
				color.a = 1.0f;
				return true;
			}
			break;
		case 1: // fade out to next view
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				phase = 0;
				view = nextView;
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}
}
