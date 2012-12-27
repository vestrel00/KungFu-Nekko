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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class PauseManager implements Touchable, Drawable, Updatable {

	private final float COLOR_SPEED = 0.01f;
	private final CharSequence PAUSED = "PAUSED", RESUME = "RESUME";

	private Rectangle resumeRect;
	private float halfWidth;
	public Color resumeColor, targetResumeColor;
	private Random rand;

	public PauseManager() {
		TextBounds bound = KFNekko.resource.chunkFive.getBounds(RESUME);
		resumeRect = new Rectangle(0, 0, bound.width * 0.5f,
				bound.height * 0.5f);
		bound = KFNekko.resource.chunkFive.getBounds(PAUSED);
		halfWidth = bound.width * 0.5f;
		resumeColor = new Color(Color.WHITE);
		targetResumeColor = new Color(Color.YELLOW);
		rand = new Random();
	}

	@Override
	public void update() {
		updateResumeColor();
		resumeRect.x = KFNekko.camera.rect.x + 380.0f;
		resumeRect.y = KFNekko.camera.rect.y + 290.0f;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		if (KFNekko.view == KFNekko.VIEW_PAUSED) {
			KFNekko.resource.chunkFive.setColor(Color.WHITE);
			KFNekko.resource.chunkFive.setScale(1.5f);
			KFNekko.resource.chunkFive.draw(batch, PAUSED,
					KFNekko.camera.camera.position.x - halfWidth * 1.5f,
					KFNekko.camera.camera.position.y + 25.0f);
		}
		KFNekko.resource.chunkFive.setColor(resumeColor);
		KFNekko.resource.chunkFive.setScale(0.5f);
		KFNekko.resource.chunkFive.draw(batch, RESUME, resumeRect.x,
				resumeRect.y + resumeRect.height);
		KFNekko.resource.chunkFive.setScale(1.0f);
		batch.end();
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (resumeRect.contains(x, y)) {
			KFNekko.view = KFNekko.VIEW_GAME;
			KFNekko.audio.touch();
			KFNekko.map.manager.resume();
			if (KFNekko.settings.musicOn)
				KFNekko.audio.music.play();
			return true;
		} else
			return false;
	}

	private void updateResumeColor() {
		boolean change = false;
		if (resumeColor.r > targetResumeColor.r) {
			if ((resumeColor.r -= COLOR_SPEED) < targetResumeColor.r)
				change = true;
		} else if (resumeColor.r < targetResumeColor.r) {
			if ((resumeColor.r += COLOR_SPEED) > targetResumeColor.r)
				change = true;
		}

		if (resumeColor.g > targetResumeColor.g) {
			if ((resumeColor.g -= COLOR_SPEED) < targetResumeColor.g)
				change = true;
		} else if (resumeColor.g < targetResumeColor.g) {
			if ((resumeColor.g += COLOR_SPEED) > targetResumeColor.g)
				change = true;
		}

		if (resumeColor.b > targetResumeColor.b) {
			if ((resumeColor.b -= COLOR_SPEED) < targetResumeColor.b)
				change = true;
		} else if (resumeColor.b < targetResumeColor.b) {
			if ((resumeColor.b += COLOR_SPEED) > targetResumeColor.b)
				change = true;
		}

		if (change)
			targetResumeColor = genColor();

	}

	private Color genColor() {
		return new Color((float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f, 1.0f);
	}

}
