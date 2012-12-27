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

package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.interf.PickUp;

public class PowerUp implements PickUp {

	public static final int INVISIBILITY = 0, RAGE = 1, QUICKFEET = 2,
			KUNGFU_MASTER = 3, ENDURANCE = 4;;
	private static final long DURATION = 20000000000L;

	private Rectangle rect;
	private AtlasRegion region;
	private long dropTime;
	private int type;
	private float xDirection, xSpeed, rot = 0.0f;
	private boolean isActive = false;

	public PowerUp(int type) {
		this.type = type;
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
			if (TimeUtils.nanoTime() - dropTime > 2000000000L
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
		}
	}

	@Override
	public void pickUp() {
		if (isActive) {
			isActive = false;
			KFNekko.player.powerUp(type);
		}
	}

}
