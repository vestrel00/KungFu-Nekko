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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.Map;

public class IntroSelectionManager implements Updatable, Drawable, Touchable {

	private final CharSequence[] strings = { "The Last Stand", "Coming Soon",
			"Coming Soon", "Coming Soon" };

	private float[] widths;
	private int phase = 0;
	private Color color;
	private AtlasRegion buttonRegion;
	private Rectangle lastStandRect, mercenaryRect, collectorRect,
			adventurerRect;

	public IntroSelectionManager(TextureAtlas atlas) {
		color = new Color(Color.CLEAR);
		buttonRegion = atlas.findRegion("button_large");
		widths = new float[strings.length];
		for (int i = 0; i < widths.length; i++)
			widths[i] = KFNekko.resource.chunkFive.getBounds(strings[i]).width;
		lastStandRect = new Rectangle(KFNekko.camera.camera.position.x
				- (float) buttonRegion.originalWidth * 0.5f, 216.0f,
				(float) buttonRegion.originalWidth,
				(float) buttonRegion.originalHeight);
		mercenaryRect = new Rectangle(lastStandRect.x, 150.0f,
				lastStandRect.width, lastStandRect.height);
		collectorRect = new Rectangle(lastStandRect.x, 84.0f,
				lastStandRect.width, lastStandRect.height);
		adventurerRect = new Rectangle(lastStandRect.x, 20.0f,
				lastStandRect.width, lastStandRect.height);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(color);
		batch.draw(buttonRegion, lastStandRect.x, lastStandRect.y,
				lastStandRect.width, lastStandRect.height);
		batch.draw(buttonRegion, mercenaryRect.x, mercenaryRect.y,
				mercenaryRect.width, mercenaryRect.height);
		batch.draw(buttonRegion, collectorRect.x, collectorRect.y,
				collectorRect.width, collectorRect.height);
		batch.draw(buttonRegion, adventurerRect.x, adventurerRect.y,
				adventurerRect.width, adventurerRect.height);
		batch.end();

		batch.begin();
		KFNekko.resource.chunkFive.setColor(0.4118f, 0.6157f, 1.0f, color.a);
		KFNekko.resource.chunkFive.draw(batch, strings[0],
				KFNekko.camera.camera.position.x - widths[0] * 0.5f,
				lastStandRect.y + lastStandRect.height - 11.0f);
		// TODO REMOVE
		KFNekko.resource.chunkFive.setColor(0.25f, 0.25f, 0.25f, color.a);
		KFNekko.resource.chunkFive.draw(batch, strings[1],
				KFNekko.camera.camera.position.x - widths[1] * 0.5f,
				mercenaryRect.y + mercenaryRect.height - 11.0f);
		KFNekko.resource.chunkFive.draw(batch, strings[2],
				KFNekko.camera.camera.position.x - widths[2] * 0.5f,
				collectorRect.y + collectorRect.height - 11.0f);
		KFNekko.resource.chunkFive.draw(batch, strings[3],
				KFNekko.camera.camera.position.x - widths[3] * 0.5f,
				adventurerRect.y + adventurerRect.height - 11.0f);
	}

	@Override
	public void update() {
		updateColor(0.4f);
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (lastStandRect.contains(x, y)) {
			// lots of checks may be unnecessary but why not
			if (phase == 0 && KFNekko.map.finished) {
				KFNekko.map.setLevel(Map.LAST_STAND);
				KFNekko.background.setOffsetRatio();
				phase = 1;
			}
			return true;
		} else if (mercenaryRect.contains(x, y)) {
			// TODO
			return true;
		} else if (collectorRect.contains(x, y)) {
			// TODO
			return true;
		} else if (adventurerRect.contains(x, y)) {
			// TODO
			return true;
		} else {
			phase = 2;
			return false;
		}
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
		case 1: // fade out to game level intro
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				phase = 0;
				KFNekko.view = KFNekko.VIEW_LEVEL_INTRO;
				color.set(Color.CLEAR);
				return true;
			}
			break;
		case 2: // fade out to nothing
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				phase = 0;
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

}
