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

package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class Projectile implements Updatable, Drawable {

	private AtlasRegion[] regions;
	private Array<Actor> targets;
	private int hitIndex, index, damage;
	private Rectangle rect, hitRect;
	private Vector2 initLoc;
	private float xSpeed, ySpeed, centerX, centerY, knockBackDistance;
	private boolean isActive, isDeactivating, aoe;
	private long duration, launchTime, indexChangeDelay, lastIndexChange;

	public Projectile(Array<Actor> targets) {
		this.targets = targets;
		rect = new Rectangle();
		hitRect = new Rectangle();
		initLoc = new Vector2();
	}

	public void launch(AtlasRegion[] regions, int hitIndex, float centerX,
			float centerY, float xSpeed, float ySpeed, long duration,
			long indexChangeDelay, int damage, float knockBackDistance,
			boolean aoe) {
		if (!isActive) {
			this.regions = regions;
			this.hitIndex = hitIndex;
			this.centerX = centerX;
			this.centerY = centerY;
			this.xSpeed = xSpeed;
			this.ySpeed = ySpeed;
			this.duration = duration;
			this.indexChangeDelay = indexChangeDelay;
			this.damage = damage;
			this.knockBackDistance = knockBackDistance;
			this.aoe = aoe;
			launchTime = TimeUtils.nanoTime();
			isActive = true;
			isDeactivating = false;
			index = 0;
		}
	}

	@Override
	public void update() {
		if (isActive) {
			if (TimeUtils.nanoTime() - lastIndexChange > indexChangeDelay) {
				lastIndexChange = TimeUtils.nanoTime();
				++index;
			}

			if (!isDeactivating && index == hitIndex)
				index = 0;
			if (index == regions.length) {
				isActive = false;
				return;
			}
			if (isDeactivating)
				return;

			initLoc.set(centerX, centerY);
			if (KFNekko.map.platform.hitSlopeVec(initLoc, xSpeed, ySpeed)
					|| KFNekko.map.platform.hitPlatformVec(initLoc, xSpeed,
							ySpeed)) {
				deactivate();
				KFNekko.audio.shotHit(centerX);
				return;
			}

			// update position and rectangle //////////////
			centerX += xSpeed;
			centerY += ySpeed;
			hitRect.set(centerX - (float) regions[index].originalWidth * 0.25f,
					centerY - (float) regions[index].originalHeight * 0.3f,
					(float) regions[index].originalWidth * 0.5f,
					(float) regions[index].originalHeight * 0.6f);
			rect.set(centerX - (float) regions[index].originalWidth * 0.5f,
					centerY - (float) regions[index].originalHeight * 0.5f,
					(float) regions[index].originalWidth,
					(float) regions[index].originalHeight);
			// /////////////////////

			boolean hit = false;
			// detect target collision ///////////////
			for (int i = 0; i < targets.size; i++)
				if (targets.get(i).statusState == StatusState.ALIVE
						&& hitRect.overlaps(targets.get(i).location.rect)) {
					targets.get(i).receiveDamage(damage);
					targets.get(i).location.knockBack(knockBackDistance,
							(xSpeed < 0.0) ? -1.0f : 1.0f);
					hit = true;
					if (!aoe)
						break;
				}
			if (hit) {
				deactivate();
				KFNekko.audio.shotHit(centerX);
				return;
			}
			// //////////////////////

			// check for time out ///////////////
			if (TimeUtils.nanoTime() - launchTime > duration) {
				deactivate();
				return;
			}
			// ////////////////////////////////
		}
	}

	private void deactivate() {
		if (!isDeactivating) {
			isDeactivating = true;
			index = hitIndex;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (isActive) {
			batch.draw(regions[index], rect.x, rect.y, rect.width * 0.5f,
					rect.height * 0.5f, rect.width, rect.height,
					(xSpeed < 0.0f) ? -1.0f : 1.0f, 1.0f, 0.0f);
		}
	}

}
