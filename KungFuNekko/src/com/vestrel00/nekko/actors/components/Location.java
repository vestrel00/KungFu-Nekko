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

	private static final float KNOCKBACK_SPEED = 10.0f;

	public Actor actor;
	public Rectangle rect;
	public Speed speed;
	public float x, y, jumpHeight, jumpSpeed, startJumpY, finalJumpY,
			knockBackDirection, knockBackFinal;
	public boolean onPlatform, onSlope, doubleJumped;

	public Location(float x, float y, float maxXSpeed, float maxYSpeed,
			float jumpHeight, float jumpSpeed) {
		this.x = x;
		this.y = y;
		this.jumpHeight = jumpHeight;
		this.jumpSpeed = jumpSpeed;
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
				if (!onSlope) {
					speed.xSpeed = KNOCKBACK_SPEED;
					if (knockBackDirection < 0.0f) {
						speed.xDirection = Speed.DIRECTION_LEFT;
						actor.faceState = FaceState.RIGHT;
						if (x <= knockBackFinal)
							actor.horizontalMotionState = HorizontalMotionState.IDLE;
					} else if (knockBackDirection > 0.0f) {
						speed.xDirection = Speed.DIRECTION_RIGHT;
						actor.faceState = FaceState.LEFT;
						if (x >= knockBackFinal)
							actor.horizontalMotionState = HorizontalMotionState.IDLE;
					}
				} else
					actor.horizontalMotionState = HorizontalMotionState.IDLE;
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
				dx *= 0.4f;

			if (actor.location.rect.x > 0.0f
					&& actor.location.rect.x + actor.location.rect.width < KFNekko.map.width)
				x += dx;
			else if (actor.location.rect.x <= 0.0f)
				x += 1.0f;
			else if (actor.location.rect.x + actor.location.rect.width >= KFNekko.map.width)
				x -= 1.0f;
			// TODO wall detection

			// detect slope first
			if (!KFNekko.map.platform.hitSlope(actor, dx, dy)
					&& !KFNekko.map.platform.hitPlatform(actor, dx, dy))
				y += dy;
			else {
				if (speed.yDirection == Speed.DIRECTION_DOWN)
					speed.ySpeed = 0.0f;

				actor.onLanding();
				doubleJumped = false;
				actor.verticalMotionState = VerticalMotionState.FALLING;
			}

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
		if (!onSlope
				&& actor.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK) {
			actor.horizontalMotionState = HorizontalMotionState.KNOCKED_BACK;
			this.knockBackDirection = knockBackDirection;
			knockBackFinal = x + knockBackDistance * knockBackDirection;
		}
	}
}
