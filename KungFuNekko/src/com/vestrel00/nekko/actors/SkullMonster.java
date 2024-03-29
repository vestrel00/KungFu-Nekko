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
import com.vestrel00.nekko.actors.components.SkullMonsterSprite;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.Visibility;

/**
 * Highest health out of all three monsters.
 * 
 * @author Estrellado, Vandolf
 * 
 */
public class SkullMonster extends Monster {

	private static long lastGrowlTime;
	private SkullMonsterSprite skullSprite;
	public long lastSpecialAttTime;
	public int damage;
	public float knockBackDistance, speedTmp;

	public SkullMonster(TextureAtlas atlas, Location location,
			Array<Actor> targets, float aggroRange, float motionRange,
			Color color, int level, boolean attackAOE) {
		super(targets, location, 0, aggroRange, motionRange);
		skullSprite = new SkullMonsterSprite(this, atlas, color, attackAOE);
		sprite = skullSprite;
		attackDelay = 2000000000L;
		location.setActor(this);
		reset(level);
	}

	@Override
	public void onDeactivateCombat() {
		if (combatState == CombatState.SPECIAL) {
			location.knockBackImmune = false;
			location.speed.maxXSpeed = speedTmp;
			location.speed.xSpeed = speedTmp;
		}
		combatState = CombatState.IDLE;
	}

	@Override
	protected void updateCombatState() {
		if (target != null
				&& (location.x > target.location.x - 200.0f && location.x < target.location.x + 200.0f)
				&& TimeUtils.nanoTime() - lastSpecialAttTime > 8000000000L) {
			lastSpecialAttTime = TimeUtils.nanoTime();
			setCombatState(CombatState.SPECIAL);
			location.knockBackImmune = true;
			speedTmp = location.speed.maxXSpeed;
			location.speed.maxXSpeed = 12.0f;
			return;
		}
		if (TimeUtils.nanoTime() - lastAttackTime > 2000000000L) {
			lastAttackTime = TimeUtils.nanoTime();
			if (target != null && sprite.combatIndex == 0
					&& location.rect.overlaps(target.location.rect)) {
				setCombatState(CombatState.ATTACK);
			}
		}
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		super.attack(damage, aoe, knockBackDistance);
		if (visibility == Visibility.VISIBLE
				&& TimeUtils.nanoTime() - lastGrowlTime > 500000000L) {
			lastGrowlTime = TimeUtils.nanoTime();
			KFNekko.audio.growl(location.x, 1.0f);
		}
	}

	@Override
	public void reset(int level) {
		knockBackDistance = 80.0f + (float) level * 3.0f;
		damage = 2 + level / 8; // long division ftw > level>>2
		this.maxHealth = 8 + level / 3;
		health = this.maxHealth;
		this.level = level;
		skullSprite.combatIndex = 0;
	}

}
