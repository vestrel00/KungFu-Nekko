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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;

public abstract class Monster extends Actor {

	protected Actor target, primeTarget;
	protected float aggroRange, motionRange;
	public long lastAttackTime, attackDelay;
	protected Rectangle aggroRect;
	public int level;
	public FaceState primaryFaceState;

	public Monster(Array<Actor> targets, Location location, int maxHealth,
			float aggroRange, float motionRange) {
		super(targets, location, maxHealth);
		this.aggroRange = aggroRange;
		this.motionRange = motionRange;
		aggroRect = new Rectangle();
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		for (int i = 0; i < targets.size; i++)
			if (targets.get(i).statusState == StatusState.ALIVE
					&& location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				targets.get(i).location.knockBack(knockBackDistance,
						(faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				if (!aoe)
					break;
			}
	}

	@Override
	public void onDeath() {
		super.onDeath();
		KFNekko.map.manager.monsterDown(this);
	}

	public abstract void reset(int level);

	@Override
	public void receiveDamage(int damage) {
		super.receiveDamage(damage);
		float c = (float) damage / (float) maxHealth;

		if ((sprite.color.r += c) > 1.0f)
			sprite.color.r = 1.0f;
		if ((sprite.color.g += c) > 1.0f)
			sprite.color.g = 1.0f;
		if ((sprite.color.b += c) > 1.0f)
			sprite.color.b = 1.0f;
	}

	@Override
	public void update() {
		if (statusState != StatusState.DEAD) {
			// make sure that target is still alive and within the aggro range
			if (primeTarget != null)
				if (primeTarget.statusState != StatusState.DEAD)
					target = primeTarget;

			if (target != null
					&& (target.statusState == StatusState.DEAD || !withinAggroRange(target)))
				target = null;
			// target selection
			if (target == null)
				for (int i = targets.size - 1; i > -1; i--)
					if (targets.get(i).statusState == StatusState.ALIVE
							&& withinAggroRange(targets.get(i))) {
						target = targets.get(i);
						break;
					}
			// update combatState
			updateCombatState();

			// update motionState
			updateMotionState();

			super.update();
		}
	}

	protected void updateCombatState() {
		if (TimeUtils.nanoTime() - lastAttackTime > attackDelay) {
			lastAttackTime = TimeUtils.nanoTime();
			if (target != null && sprite.combatIndex == 0
					&& location.rect.overlaps(target.location.rect)) {
				setCombatState(CombatState.ATTACK);
			}
		}
	}

	protected void updateMotionState() {
		if (target != null) {
			faceState = (target.location.x >= location.x) ? FaceState.RIGHT
					: FaceState.LEFT;
			setHorizontalMotionState(HorizontalMotionState.MOVING);
		} else if (primaryFaceState != null) {
			faceState = primaryFaceState;
			setHorizontalMotionState(HorizontalMotionState.MOVING);
		}

		if (location.x < 30.0f || location.x < location.spawnX - motionRange)
			faceState = FaceState.RIGHT;
		else if (location.x > KFNekko.map.width - 30.0f
				|| location.x > location.spawnX + motionRange)
			faceState = FaceState.LEFT;

	}

	protected boolean withinAggroRange(Actor targ) {
		aggroRect.set(location.x - aggroRange, location.rect.y,
				aggroRange * 2.0f, location.rect.height);
		return aggroRect.overlaps(targ.location.rect);
	}

	public void setPrimeTarget(Actor primeTarget) {
		this.primeTarget = primeTarget;
	}

}
