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
