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

import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;
import com.vestrel00.nekko.interf.Updatable;

public class Location implements Updatable {

	private static final float KNOCKBACK_SPEED = 8.0f;

	public Actor actor;
	public Rectangle rect;
	public Speed speed;
	public float x, y, jumpHeight, jumpSpeed, startJumpY, finalJumpY,
			knockBackDirection, knockBackFinal, spawnX, spawnY;
	public boolean onPlatform, onSlope, doubleJumped;

	public Location(float x, float y, float maxXSpeed, float maxYSpeed,
			float jumpHeight, float jumpSpeed) {
		this.x = x;
		this.y = y;
		this.jumpHeight = jumpHeight;
		this.jumpSpeed = jumpSpeed;
		spawnX = x;
		spawnY = y;
		speed = new Speed(maxXSpeed, maxYSpeed);
		rect = new Rectangle();
		onPlatform = false;
		onSlope = false;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
		rect.set(actor.location.x, actor.location.y,
				(float) actor.sprite.currentTexture.originalWidth,
				(float) actor.sprite.currentTexture.originalHeight);
	}

	@Override
	public void update() {
		if (actor.statusState == StatusState.ALIVE) {
			actor.visibility = (KFNekko.camera.rect.overlaps(rect)) ? Visibility.VISIBLE
					: Visibility.NOT_VISIBLE;

			// determine the speed
			switch (actor.horizontalMotionState) {
			case IDLE:
				speed.decel();
				break;
			case MOVING:
				switch (actor.faceState) {
				case LEFT:
					speed.accelLeft();
					break;
				case RIGHT:
					speed.accelRight();
					break;
				}
				break;
			case KNOCKED_BACK:
				speed.xSpeed = KNOCKBACK_SPEED;
				if (knockBackDirection < 0.0f) {
					speed.xDirection = Speed.DIRECTION_LEFT;
					actor.faceState = FaceState.RIGHT;
					if (x <= knockBackFinal || rect.x < 10.0f)
						actor.horizontalMotionState = HorizontalMotionState.IDLE;
				} else if (knockBackDirection > 0.0f) {
					speed.xDirection = Speed.DIRECTION_RIGHT;
					actor.faceState = FaceState.LEFT;
					if (x >= knockBackFinal
							|| rect.x + rect.width > KFNekko.map.width - 10.0f)
						actor.horizontalMotionState = HorizontalMotionState.IDLE;
				}
				break;
			}

			switch (actor.verticalMotionState) {
			case JUMPING:
				if (rect.y < finalJumpY) {
					speed.ySpeed = finalJumpY - rect.y;
					// clip speed
					if (speed.ySpeed > jumpSpeed)
						speed.ySpeed = jumpSpeed;
					// detect that we reached top
					if (speed.ySpeed <= 2.0f)
						actor.verticalMotionState = VerticalMotionState.FALLING;
				} else {// detect that we reached top
					speed.ySpeed = 2.0f;
					actor.verticalMotionState = VerticalMotionState.FALLING;
				}
				break;
			case FALLING:
				speed.accelDown();
				break;
			}

			float dx = speed.getXVelocity();
			float dy = speed.getYVelocity();

			// decrease dx while on slope to patch up our flawed slope detection
			// algorithm
			if (onSlope)
				dx *= 0.6f;

			boolean addDx = false, addDy = false;

			// detect slope first
			if (!KFNekko.map.platform.hitSlope(actor, dx, dy)
					&& !KFNekko.map.platform.hitPlatform(actor, dx, dy))
				addDy = true;
			else {
				if (speed.yDirection == Speed.DIRECTION_DOWN)
					speed.ySpeed = 0.0f;
				actor.verticalMotionState = VerticalMotionState.FALLING;
			}

			if (actor.location.rect.x > 0.0f
					&& actor.location.rect.x + actor.location.rect.width < KFNekko.map.width)
				addDx = true;
			else if (actor.location.rect.x <= 0.0f)
				x += 1.0f;
			else if (actor.location.rect.x + actor.location.rect.width >= KFNekko.map.width)
				x -= 1.0f;
			// TODO wall detection

			if (addDx)
				x += dx;
			if (addDy)
				y += dy;

			// rough patch should never happen now though
			if (y < 0.0f)
				y = spawnY;

			// All sprites must have same dimensions!
			rect.set(x - (float) actor.sprite.currentTexture.originalWidth
					* 0.5f,
					y - (float) actor.sprite.currentTexture.originalHeight
							* 0.5f,
					(float) actor.sprite.currentTexture.originalWidth,
					(float) actor.sprite.currentTexture.originalHeight);

		}
	}

	public boolean jump() {
		if (onPlatform || onSlope) {
			if (actor.visibility == Visibility.VISIBLE)
				KFNekko.audio.footStep(x);
			onPlatform = false;
			onSlope = false;
			actor.verticalMotionState = VerticalMotionState.JUMPING;
			speed.yDirection = Speed.DIRECTION_UP;
			startJumpY = rect.y;
			finalJumpY = jumpHeight + startJumpY;
			return true;
		}
		return false;
	}

	public void knockBack(float knockBackDistance, float knockBackDirection) {
		if (actor.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK) {
			actor.horizontalMotionState = HorizontalMotionState.KNOCKED_BACK;
			this.knockBackDirection = knockBackDirection;
			knockBackFinal = x + knockBackDistance * knockBackDirection;
		}
	}
}
