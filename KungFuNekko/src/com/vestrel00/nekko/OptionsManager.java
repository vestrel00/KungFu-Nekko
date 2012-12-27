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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class OptionsManager implements Touchable, Drawable, Updatable {

	private AtlasRegion soundOn, soundOff, musicOn, musicOff;
	private Rectangle soundRect, musicRect;

	public OptionsManager() {
		soundOn = KFNekko.resource.atlas.findRegion("soundOn");
		soundOff = KFNekko.resource.atlas.findRegion("soundOff");
		musicOn = KFNekko.resource.atlas.findRegion("musicOn");
		musicOff = KFNekko.resource.atlas.findRegion("musicOff");
		soundRect = new Rectangle(0, 0, (float) soundOn.originalWidth,
				(float) soundOn.originalHeight);
		musicRect = new Rectangle(0, 0, (float) musicOn.originalWidth,
				(float) musicOn.originalHeight);
	}

	@Override
	public void update() {
		if (KFNekko.view == KFNekko.VIEW_OPTIONS) {
			soundRect.x = KFNekko.camera.rect.x + 130.0f;
			musicRect.x = KFNekko.camera.rect.x + 276.0f;
			soundRect.y = KFNekko.camera.rect.y + 139.0f;
			musicRect.y = KFNekko.camera.rect.y + 139.0f;
		} else { // place at IntroMenuManager
			soundRect.x = KFNekko.camera.rect.x + 10.0f;
			musicRect.x = KFNekko.camera.rect.x + 470.0f - musicRect.width;
			soundRect.y = KFNekko.camera.rect.y + 216.0f;
			musicRect.y = KFNekko.camera.rect.y + 216.0f;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		if (KFNekko.view == KFNekko.VIEW_OPTIONS)
			batch.setColor(Color.WHITE);
		else
			batch.setColor(KFNekko.intro.menu.color);
		if (KFNekko.settings.soundOn)
			batch.draw(soundOn, soundRect.x, soundRect.y, soundRect.width,
					soundRect.height);
		else
			batch.draw(soundOff, soundRect.x, soundRect.y, soundRect.width,
					soundRect.height);

		if (KFNekko.settings.musicOn)
			batch.draw(musicOn, musicRect.x, musicRect.y, musicRect.width,
					musicRect.height);
		else
			batch.draw(musicOff, musicRect.x, musicRect.y, musicRect.width,
					musicRect.height);
		batch.setColor(KFNekko.worldColor);
		batch.end();
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (soundRect.contains(x, y)) {
			KFNekko.settings.soundOn = !KFNekko.settings.soundOn;
			KFNekko.audio.touch();
			return true;
		} else if (musicRect.contains(x, y)) {
			KFNekko.settings.musicOn = !KFNekko.settings.musicOn;
			if (KFNekko.settings.musicOn) {
				if (KFNekko.view == KFNekko.VIEW_OPTIONS)
					KFNekko.audio.music.play();
				else
					// we are in IntroMenuManager
					KFNekko.intro.music.play();
			} else {
				if (KFNekko.view == KFNekko.VIEW_OPTIONS)
					KFNekko.audio.music.stop();
				else
					// we are in IntroMenuManager
					KFNekko.intro.music.stop();
			}
			KFNekko.audio.touch();
			return true;
		} else
			return false;
	}
}
