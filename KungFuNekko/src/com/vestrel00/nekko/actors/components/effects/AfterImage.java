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

package com.vestrel00.nekko.actors.components.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class AfterImage implements Updatable, Drawable {

	private AtlasRegion image;
	private Color color;
	private float alpha, alphaSpeed, centerX, centerY, halfWidth, halfHeight,
			xSpeed, xScale, xSpeedDecrease;
	private boolean isActive;

	public AfterImage() {
		isActive = false;
	}

	public void activate(AtlasRegion image, float centerX, float centerY,
			float alphaSpeed, float xSpeed, boolean flipped,
			float xSpeedDecrease, Color color) {
		this.image = image;
		this.xSpeed = xSpeed;
		this.centerX = centerX;
		this.centerY = centerY;
		this.alphaSpeed = alphaSpeed;
		this.xSpeedDecrease = xSpeedDecrease;
		this.color = color;

		isActive = true;
		alpha = 1.0f;
		xScale = (flipped) ? -1.0f : 1.0f;
		halfWidth = (float) image.originalWidth * 0.5f;
		halfHeight = (float) image.originalHeight * 0.5f;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (isActive) {
			batch.setColor(color.r, color.g, color.b, alpha);
			batch.draw(image, centerX - halfWidth, centerY - halfHeight,
					halfWidth, halfHeight, (float) image.originalWidth,
					(float) image.originalHeight, xScale, 1.0f, 0.0f);
			// resetting of the batch color is left to the caller
		}
	}

	@Override
	public void update() {
		if (isActive) {
			if ((alpha -= alphaSpeed) < 0.0f)
				isActive = false;

			centerX += xSpeed;
			if (xSpeed > 0.0f) {
				if ((xSpeed -= xSpeedDecrease) < 0.0f)
					xSpeed = 0.0f;
			} else if (xSpeed < 0.0f) {
				if ((xSpeed += xSpeedDecrease) > 0.0f)
					xSpeed = 0.0f;
			}
		}
	}

}
