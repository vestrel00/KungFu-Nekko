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
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.interf.PickUp;

public class PowerUp implements PickUp {

	public static final int INVISIBILITY = 0, RAGE = 1, QUICKFEET = 2,
			KUNGFU_MASTER = 3, ENDURANCE = 4, NONE = -1;

	private Rectangle rect;
	private AtlasRegion region;
	private long dropTime, duration;
	private int type;
	private float xDirection, xSpeed, rot = 0.0f;
	private boolean isActive = false, mute;

	public PowerUp(int type, long duration, boolean mute) {
		this.duration = duration;
		this.type = type;
		this.mute = mute;
		switch (type) {
		case INVISIBILITY:
			region = KFNekko.resource.atlas.findRegion("invisibility");
			break;
		case RAGE:
			region = KFNekko.resource.atlas.findRegion("rage");
			break;
		case QUICKFEET:
			region = KFNekko.resource.atlas.findRegion("quickfeet");
			break;
		case KUNGFU_MASTER:
			region = KFNekko.resource.atlas.findRegion("kungfu_master");
			break;
		case ENDURANCE:
			region = KFNekko.resource.atlas.findRegion("endurance");
			break;
		}
		rect = new Rectangle(0, 0, (float) region.originalWidth,
				(float) region.originalHeight);
	}

	@Override
	public void update() {
		if (isActive) {
			if ((rot += 10.0f) > 360.0f)
				rot -= 360.0f;
			// cannot pickup the drop before 1 second has passed
			if (TimeUtils.nanoTime() - dropTime > 1000000000L
					&& KFNekko.player.location.rect.overlaps(rect))
				pickUp();
			if ((xSpeed -= 2.0f) < 0.0f)
				xSpeed = 0.0f;
			rect.x += xSpeed * xDirection;
			if (rect.x < 0.0f)
				rect.x = 0.0f;
			else if (rect.x + rect.width > KFNekko.map.width)
				rect.x = KFNekko.map.width - rect.width;

			if (TimeUtils.nanoTime() - dropTime > DURATION)
				isActive = false;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (isActive)
			batch.draw(region, rect.x, rect.y, rect.width * 0.5f,
					rect.height * 0.5f, rect.width, rect.height, 1.0f, 1.0f,
					rot);
	}

	@Override
	public void drop(Location location) {
		if (!isActive) {
			rect.x = location.rect.x;
			rect.y = location.rect.y;
			isActive = true;
			xDirection = (location.actor.faceState == FaceState.LEFT) ? 1.0f
					: -1.0f;
			xSpeed = 20.0f;
			dropTime = TimeUtils.nanoTime();
			if (KFNekko.camera.rect.overlaps(rect))
				KFNekko.audio.powerupDrop(location.x);
		}
	}

	@Override
	public void pickUp() {
		if (isActive) {
			isActive = !KFNekko.player.powerUp(type, duration, mute);
		}
	}

}
