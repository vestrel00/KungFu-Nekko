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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.states.Visibility;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public abstract class Sprite implements Updatable, Drawable {

	protected Actor actor;
	public Color color;
	public AtlasRegion currentTexture;
	public float xScale;
	protected float rotation;
	protected long animationDelay, lastAnimationTime;
	public int combatIndex = 0;
	public boolean attackAOE;

	// used by monsters to prevent too many footstep sounds from being played at
	// once
	protected static final long STEP_DELAY = 300000000L;
	protected static long lastStepTime;

	public Sprite(Actor actor, Color color) {
		this.actor = actor;
		this.color = color;
		lastAnimationTime = 0L;
		rotation = 0;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (actor.visibility == Visibility.VISIBLE) {
			batch.setColor(color);
			batch.draw(currentTexture, actor.location.rect.x,
					actor.location.rect.y, actor.location.rect.width * 0.5f,
					actor.location.rect.height * 0.5f,
					actor.location.rect.width, actor.location.rect.height,
					xScale, 1.0f, rotation);
			// batch.setColor(KFNekko.worldColor);
			// let the caller reset the batch color
		}
	}
}
