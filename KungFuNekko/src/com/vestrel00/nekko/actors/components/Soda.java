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
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.interf.PickUp;

public class Soda implements PickUp {

	public static final int STAMINA = 0, HEALTH = 1, MIX = 2;
	private Rectangle rect;
	private AtlasRegion region;
	private float finalY;
	private long dropTime;
	private int type;
	private boolean isActive = false;

	public Soda(int type) {
		this.type = type;
		switch (type) {
		case STAMINA:
			region = KFNekko.resource.atlas.findRegion("orangeSoda");
			break;
		case HEALTH:
			region = KFNekko.resource.atlas.findRegion("redSoda");
			break;
		case MIX:
			region = KFNekko.resource.atlas.findRegion("greenSoda");
			break;
		}
		rect = new Rectangle(0, 0, (float) region.originalWidth,
				(float) region.originalHeight);
	}

	@Override
	public void update() {
		if (isActive) {
			// cannot pickup the drop before 1 second has passed
			if (TimeUtils.nanoTime() - dropTime > 1000000000L
					&& KFNekko.player.location.rect.overlaps(rect))
				pickUp();
			if ((rect.y -= 22.0f) < finalY)
				rect.y = finalY;

			if (TimeUtils.nanoTime() - dropTime > DURATION)
				isActive = false;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (isActive)
			batch.draw(region, rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void drop(Location location) {
		if (!isActive) {
			rect.x = location.rect.x;
			rect.y = location.rect.y + location.rect.height * 2.0f;
			finalY = location.rect.y;
			isActive = true;
			dropTime = TimeUtils.nanoTime();
			if (KFNekko.camera.rect.contains(location.rect))
				KFNekko.audio.sodaDrop(location.x);
		}
	}

	@Override
	public void pickUp() {
		if (isActive) {
			switch (type) {
			case STAMINA:
				if (KFNekko.player.stamina < KFNekko.player.maxStamina - 50) {
					if ((KFNekko.player.stamina += 50) > KFNekko.player.maxStamina)
						KFNekko.player.stamina = KFNekko.player.maxStamina;
				} else
					return;
				break;
			case HEALTH:
				if (KFNekko.player.health < KFNekko.player.maxHealth - 50) {
					if ((KFNekko.player.health += 50) > KFNekko.player.maxHealth)
						KFNekko.player.health = KFNekko.player.maxHealth;
				} else
					return;
				break;
			case MIX:
				if (KFNekko.player.health < KFNekko.player.maxHealth - 50
						|| KFNekko.player.stamina < KFNekko.player.maxStamina - 50) {
					if ((KFNekko.player.health += 50) > KFNekko.player.maxHealth)
						KFNekko.player.health = KFNekko.player.maxHealth;
					if ((KFNekko.player.stamina += 50) > KFNekko.player.maxStamina)
						KFNekko.player.stamina = KFNekko.player.maxStamina;
				} else
					return;
				break;
			}
			KFNekko.audio.sodaOpen(KFNekko.player.location.x);
			isActive = false;
		}
	}

}
