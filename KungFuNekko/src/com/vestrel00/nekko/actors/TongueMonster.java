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
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.TongueMonsterSprite;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;

/**
 * Highest DPS out of all three monsters.
 * 
 * @author Estrellado, Vandolf
 * 
 */
public class TongueMonster extends Monster {

	private static long lastGrowlTime;
	private TongueMonsterSprite tongueSprite;
	public int damage;
	public float knockBackDistance;

	public TongueMonster(TextureAtlas atlas, Location location,
			Array<Actor> targets, float aggroRange, float motionRange,
			Color color, int level, boolean attackAOE) {
		super(targets, location, 0, aggroRange, motionRange);
		tongueSprite = new TongueMonsterSprite(this, atlas, color, attackAOE);
		sprite = tongueSprite;
		attackDelay = 2000000000L;
		location.setActor(this);
		reset(level);
	}

	@Override
	protected boolean withinAggroRange(Actor targ) {
		aggroRect.set(location.x - aggroRange, location.y - 6000.0f,
				aggroRange * 2.0f, 12000.0f);
		return aggroRect.overlaps(targ.location.rect);
	}

	@Override
	protected void updateMotionState() {
		super.updateMotionState();
		if (target != null) {
			if (location.y > target.location.rect.y
					+ target.location.rect.height)
				verticalMotionState = VerticalMotionState.FALLING;
			else if (location.y < target.location.y)
				verticalMotionState = VerticalMotionState.FLYING;
		} else {
			if (location.y > location.spawnY + location.rect.height)
				verticalMotionState = VerticalMotionState.FALLING;
			else if (location.y < location.spawnY - location.rect.height)
				verticalMotionState = VerticalMotionState.FLYING;
		}
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		super.attack(damage, aoe, knockBackDistance);
		if (visibility == Visibility.VISIBLE
				&& TimeUtils.nanoTime() - lastGrowlTime > 500000000L) {
			lastGrowlTime = TimeUtils.nanoTime();
			KFNekko.audio.growl(location.x, 2.0f);
		}
	}

	@Override
	protected void updateCombatState() {
		if (TimeUtils.nanoTime() - lastAttackTime > 500000000L) {
			lastAttackTime = TimeUtils.nanoTime();
			if (target != null && sprite.combatIndex == 0
					&& location.rect.overlaps(target.location.rect)) {
				setCombatState(CombatState.ATTACK);
			}
		}
	}

	@Override
	public void reset(int level) {
		knockBackDistance = 40.0f + (float) level * 2.0f;
		damage = 4 + level / 7; // long division ftw > level>>2
		this.maxHealth = 6 + level / 4;
		health = this.maxHealth;
		this.level = level;
		tongueSprite.combatIndex = 0;
	}

}
