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

package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.CuteMonsterSprite;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.Visibility;

/**
 * Highest movement speed out of all three monsters.
 * 
 * @author Estrellado, Vandolf
 * 
 */
public class CuteMonster extends Monster {

	private static long lastGrowlTime;
	private CuteMonsterSprite cuteSprite;
	private long lastjumpTime;
	public int damage;
	public float knockBackDistance;

	public CuteMonster(TextureAtlas atlas, Location location,
			Array<Actor> targets, float aggroRange, float motionRange,
			Color color, int level, boolean attackAOE) {
		super(targets, location, 0, aggroRange, motionRange);
		cuteSprite = new CuteMonsterSprite(this, atlas, color, attackAOE);
		sprite = cuteSprite;
		attackDelay = 800000000L;
		location.setActor(this);
		reset(level);
	}

	@Override
	public void reset(int level) {
		knockBackDistance = 20.0f + (float) level;
		damage = 1 + level / 10; // long division ftw > level>>2
		this.maxHealth = 4 + level / 5;
		health = this.maxHealth;
		this.level = level;
	}

	@Override
	protected void updateMotionState() {
		super.updateMotionState();
		if (target == null && TimeUtils.nanoTime() - lastjumpTime > 5000000000L) {
			lastjumpTime = TimeUtils.nanoTime();
			jump();
		}
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		super.attack(damage, aoe, knockBackDistance);
		if (visibility == Visibility.VISIBLE
				&& TimeUtils.nanoTime() - lastGrowlTime > 500000000L) {
			lastGrowlTime = TimeUtils.nanoTime();
			KFNekko.audio.cuteGrowl(location.x);
		}
	}

}
