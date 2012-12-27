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

package com.vestrel00.nekko.maps.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class Background implements Updatable, Drawable {

	private AtlasRegion background;
	private float yOffset, xOffset, maxXOffset, xOffsetRatio;

	public Background() {
		background = KFNekko.resource.atlas.findRegion("background");
		// the height of the bottom hud cover
		yOffset = KFNekko.settings.viewHeight * 0.1f;
		// calculate the amount of unseen space in the texture
		maxXOffset = (float) background.originalWidth
				- KFNekko.settings.viewWidth;
	}

	/**
	 * To be called after map is created.
	 */
	public void setOffsetRatio() {
		// ratio of map width to maxXOffset
		xOffsetRatio = maxXOffset / KFNekko.map.width;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(background, KFNekko.camera.rect.x - xOffset,
				KFNekko.camera.rect.y + yOffset);
	}

	@Override
	public void update() {
		xOffset = xOffsetRatio * KFNekko.camera.rect.x;
	}

}
