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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.Sprite;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public abstract class Actor implements Updatable, Drawable {

	public Sprite sprite;
	public Location location;
	public Array<Actor> targets;

	public FaceState faceState;
	public StatusState statusState;
	public CombatState combatState;
	public HorizontalMotionState horizontalMotionState;
	public VerticalMotionState verticalMotionState;
	public Visibility visibility;

	public int maxHealth, health;

	public Actor(Array<Actor> targets, Location location, int maxHealth) {
		this.location = location;
		this.targets = targets;
		this.maxHealth = maxHealth;
		health = maxHealth;
	}

	public void setState(FaceState faceState, StatusState statusState,
			CombatState combatState,
			HorizontalMotionState horizontalMotionState,
			VerticalMotionState verticalMotionState) {
		this.faceState = faceState;
		this.statusState = statusState;
		this.combatState = combatState;
		this.horizontalMotionState = horizontalMotionState;
		this.verticalMotionState = verticalMotionState;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (statusState != StatusState.DEAD)
			sprite.draw(batch);
	}

	@Override
	public void update() {
		if (statusState != StatusState.DEAD) {
			location.update();
			sprite.update();
		}
	}

	public boolean jump() {
		return location.jump();
	}

	public abstract void attack(int damage, boolean aoe, float knockBackDistance);

	public void setHorizontalMotionState(
			HorizontalMotionState horizontalMotionState) {
		if (this.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
			this.horizontalMotionState = horizontalMotionState;
	}

	public void receiveDamage(int damage) {
		if (health > 0) {
			health -= damage;
			if (health <= 0)
				onDeath();
		}
	}

	public void onDeactivateCombat() {
		combatState = CombatState.IDLE;
	}

	public void setCombatState(CombatState combatState) {
		this.combatState = combatState;
	}

	public void onDeath() {
		if (statusState == StatusState.ALIVE)
			statusState = StatusState.DYING;
	}

	public void reset(float locationX, float locationY) {
		// All sprites must have same dimensions!
		location.x = locationX;
		location.y = locationY;
		location.spawnX = locationX;
		location.spawnY = locationY;
		location.rect.set(location.x
				- (float) sprite.currentTexture.originalWidth * 0.5f,
				location.y - (float) sprite.currentTexture.originalHeight
						* 0.5f, (float) sprite.currentTexture.originalWidth,
				(float) sprite.currentTexture.originalHeight);
	}

}
