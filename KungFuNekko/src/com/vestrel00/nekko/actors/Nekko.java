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
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.NekkoSprite;
import com.vestrel00.nekko.actors.components.PowerUp;
import com.vestrel00.nekko.actors.components.Speed;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class Nekko extends Actor {

	public NekkoSprite nekkoSprite;
	private long powerUpTime, powerupDuration;
	private float speedTmp;
	public int stamina, maxStamina, powerUp;

	public Nekko(TextureAtlas atlas, Location location, Array<Actor> targets,
			int maxHealth, Color color, int maxStamina) {
		super(targets, location, maxHealth);
		this.maxStamina = maxStamina;
		nekkoSprite = new NekkoSprite(this, atlas, color);
		sprite = nekkoSprite;
		location.setActor(this);
		sprite = nekkoSprite;
		stamina = maxStamina;
		powerUp = PowerUp.NONE;
	}

	protected void hit(Actor target) {
		switch (combatState) {
		case SUPERUPPERCUT:
		case UPPERCUT:
		case SPIN:
		case FLYINGKICK:
		case ROUNDKICK:
			target.location.onPlatform = true;
			target.jump();
			break;
		}
	}

	@Override
	public void update() {
		if (statusState != StatusState.DEAD) {
			// cannot attack and walk/move/run at the same time while on
			// platform
			if (combatState != CombatState.IDLE
					&& (location.onPlatform || location.onSlope)) {
				location.speed.xSpeed = 0.0f;
				horizontalMotionState = HorizontalMotionState.IDLE;
			}
			super.update();
			if (powerUp != PowerUp.NONE
					&& TimeUtils.nanoTime() - powerUpTime > powerupDuration) {
				onDeactivatePowerUp();
			}
		}
	}

	protected void executeCombatMove() {
		switch (combatState) {
		case SUPERUPPERCUT:
			nekkoSprite.activateSmoke();
		case SPIN:
			jump();
			horizontalMotionState = HorizontalMotionState.FORCED_MOVING;
			location.speed.xDirection = (faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
					: Speed.DIRECTION_RIGHT;
			location.speed.xSpeed = 6.0f;
			break;
		case FLYINGKICK:
			jump();
			horizontalMotionState = HorizontalMotionState.FORCED_MOVING;
			location.speed.xDirection = (faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
					: Speed.DIRECTION_RIGHT;
			location.speed.xSpeed = 20.0f;
			break;
		case JUMPSHOT:
			jump();
			break;
		}
	}

	@Override
	public void onDeactivateCombat() {
		switch (combatState) {
		case SPIN:
		case SUPERUPPERCUT:
		case FLYINGKICK:
			horizontalMotionState = HorizontalMotionState.IDLE;
			location.speed.xSpeed = 0.0f;
			break;
		}

		super.onDeactivateCombat();
	}

	@Override
	public void setCombatState(CombatState combatState) {
		// wait for the combat move in execution to finish
		if (nekkoSprite.combatIndex == 0 && combatState != CombatState.IDLE) {
			if (enoughStamina(combatState)) {
				this.combatState = combatState;
				executeCombatMove();
			}
		} else if (nekkoSprite.combatIndex == 0
				&& combatState == CombatState.IDLE)
			this.combatState = combatState;
	}

	private boolean enoughStamina(CombatState combat) {
		if (powerUp == PowerUp.ENDURANCE)
			return true;

		int staminaCost = 0;
		switch (combat) {
		case JAB:
		case LOWMIDDLEKICK:
		case ONETWOCOMBO:
		case FASTSHOT:
			staminaCost = 2;
			break;
		case SPIN:
		case UPPERCUT:
		case ROUNDKICK:
		case JUMPSHOT:
			staminaCost = 3;
			break;
		case FLYINGKICK:
		case POWERSHOT:
			staminaCost = 5;
			break;
		case HIGHKICK:
		case DOWNWARDKICK:
			staminaCost = 3;
			break;
		case TWOSIDEDATTACK:
		case SUPERUPPERCUT:
			staminaCost = 6;
			break;
		case SUPERSHOT:
			staminaCost = 8;
			break;
		case BEATDOWN:
		case LIGHTNINGKICKS:
		case SPEEDYHANDS:
		case GATTLINGSHOT:
			staminaCost = 10;
			break;
		}
		if (stamina >= staminaCost) {
			stamina -= staminaCost;
			return true;
		}
		return false;
	}

	@Override
	public boolean jump() {
		if (super.jump()) {
			nekkoSprite.jumpIndex = 0;
			return true;
		} else {
			if (!location.doubleJumped) {
				location.onPlatform = true;
				location.doubleJumped = true;
				nekkoSprite.doubleJumpIndex = 0;
				location.jump();
			}
		}
		return false;
	}

	@Override
	public void onDeath() {
		if (statusState == StatusState.ALIVE) {
			statusState = StatusState.DYING;
			if (visibility == Visibility.VISIBLE)
				KFNekko.audio.meow(location.x);
		}
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		boolean hit = false;
		for (int i = 0; i < targets.size; i++)
			if (targets.get(i).statusState == StatusState.ALIVE
					&& location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				hit(targets.get(i));
				targets.get(i).location.knockBack(knockBackDistance,
						(faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				hit = true;
				if (!aoe)
					break;
			}
		switch (combatState) {
		case BEATDOWN:
		case LIGHTNINGKICKS:
		case SPEEDYHANDS:
			KFNekko.audio.punchSpecial(location.x, 0.5f);
			break;
		default:
			KFNekko.audio.punch(location.x);
			break;
		}
		if (hit) {
			if (combatState == CombatState.SUPERUPPERCUT
					|| combatState == CombatState.FLYINGKICK)
				KFNekko.audio.superSmack(location.x);
			else
				KFNekko.audio.smack(location.x);
		}
	}

	private void onDeactivatePowerUp() {
		if (powerUp == PowerUp.NONE)
			return;
		// undo changes
		nekkoSprite.targetColor.set(Color.WHITE);
		switch (powerUp) {
		// cannot be hit
		case PowerUp.INVISIBILITY:
			location.knockBackImmune = false;
			break;
		// do twice damage
		case PowerUp.RAGE:
			nekkoSprite.damageMultiplier = 1;
			break;
		// move 1.5 x speed
		case PowerUp.QUICKFEET:
			location.speed.maxXSpeed = speedTmp;
			location.speed.xSpeed = speedTmp;
			break;
		// animation speedup
		case PowerUp.KUNGFU_MASTER:
			nekkoSprite.speedUp = 0;
			break;
		// do not subtract stamina
		case PowerUp.ENDURANCE:
			// nothing
			break;
		}
		powerUp = PowerUp.NONE;
	}

	public boolean powerUp(int type, long powerupDuration, boolean mute) {
		if (powerUp != PowerUp.NONE)
			return false;

		this.powerupDuration = powerupDuration;
		if (!mute)
			KFNekko.audio.powerup(location.x);
		powerUp = type;
		powerUpTime = TimeUtils.nanoTime();
		switch (type) {
		// cannot be hit
		case PowerUp.INVISIBILITY:
			nekkoSprite.targetColor.set(1.0f, 1.0f, 1.0f, 0.4f);
			location.knockBackImmune = true;
			break;
		// do twice damage
		case PowerUp.RAGE:
			nekkoSprite.damageMultiplier = 2;
			nekkoSprite.targetColor.set(1.0f, 0.0f, 0.0f, 1.0f);
			break;
		// move 1.5 x speed
		case PowerUp.QUICKFEET:
			nekkoSprite.targetColor.set(Color.BLUE);
			speedTmp = location.speed.maxXSpeed;
			location.speed.maxXSpeed = speedTmp * 1.5f;
			break;
		// animation speedup
		case PowerUp.KUNGFU_MASTER:
			nekkoSprite.targetColor.set(0.1f, 0.1f, 0.1f, 1.0f);
			nekkoSprite.speedUp = 20000000L;
			break;
		// do not subtract stamina
		case PowerUp.ENDURANCE:
			nekkoSprite.targetColor.set(Color.YELLOW);
			break;
		}
		return true;
	}

	@Override
	public void receiveDamage(int damage) {
		if (powerUp == PowerUp.INVISIBILITY)
			return;
		super.receiveDamage(damage);
		KFNekko.camera.setEffect(Camera.EFFECT_SHAKE, -1.0f, 3.0f, 200000000L);
	}

	@Override
	public void reset(float locationX, float locationY) {
		super.reset(locationX, locationY);
		stamina = maxStamina;
		health = maxHealth;
		nekkoSprite.combatIndex = 0;
		onDeactivatePowerUp();
	}

}
