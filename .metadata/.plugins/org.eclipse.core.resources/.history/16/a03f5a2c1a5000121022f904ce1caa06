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

public class Speed {

	public static final int DIRECTION_UP = 1, DIRECTION_DOWN = -1,
			DIRECTION_LEFT = -1, DIRECTION_RIGHT = 1;
	private static final float HORI_ACCEL = 3.0f, VERT_ACCEL = 4.0f;

	public int xDirection, yDirection;
	public float xSpeed, ySpeed, maxXSpeed, maxYSpeed;

	public Speed(float maxXSpeed, float maxYSpeed) {
		this.maxXSpeed = maxXSpeed;
		this.maxYSpeed = maxYSpeed;
		init();
	}

	public Speed() {
		init();
	}

	public void set(Speed speed) {
		xDirection = speed.xDirection;
		yDirection = speed.yDirection;
		xSpeed = speed.xSpeed;
		ySpeed = speed.ySpeed;
		maxXSpeed = speed.maxXSpeed;
		maxYSpeed = speed.maxYSpeed;
	}

	private void init() {
		xDirection = DIRECTION_RIGHT;
		yDirection = DIRECTION_DOWN;
		xSpeed = 0.0f;
		ySpeed = 0.0f;
	}

	public void decel() {
		decreaseHorizontalSpeed();
	}

	public void accelRight() {
		if (xDirection == DIRECTION_LEFT) {
			if (xSpeed > 0.0f)
				decreaseHorizontalSpeed();
			else
				xDirection = DIRECTION_RIGHT;
		} else
			increaseHorizontalSpeed();
	}

	public void accelLeft() {
		if (xDirection == DIRECTION_RIGHT) {
			if (xSpeed > 0.0f)
				decreaseHorizontalSpeed();
			else
				xDirection = DIRECTION_LEFT;
		} else
			increaseHorizontalSpeed();
	}

	public void accelDown() {
		if (yDirection == DIRECTION_UP) {
			if (ySpeed > 0.0f)
				decreaseVerticalSpeed();
			else
				yDirection = DIRECTION_DOWN;
		} else
			increaseVerticalSpeed();
	}

	public void accelUp() {
		if (yDirection == DIRECTION_DOWN) {
			if (ySpeed > 0.0f)
				decreaseVerticalSpeed();
			else
				yDirection = DIRECTION_UP;
		} else
			increaseVerticalSpeed();
	}

	public void increaseHorizontalSpeed() {
		if (xSpeed < maxXSpeed && (xSpeed += HORI_ACCEL) > maxXSpeed)
			xSpeed = maxXSpeed;
	}

	public void decreaseHorizontalSpeed() {
		if (xSpeed > 0.0f && (xSpeed -= HORI_ACCEL) < 0.0f)
			xSpeed = 0.0f;
	}

	public void increaseVerticalSpeed() {
		if (ySpeed < maxYSpeed && (ySpeed += VERT_ACCEL) > maxYSpeed)
			ySpeed = maxYSpeed;
	}

	public void decreaseVerticalSpeed() {
		if (ySpeed > 0.0f && (ySpeed -= VERT_ACCEL) < 0.0f)
			ySpeed = 0.0f;
	}

	public float getXVelocity() {
		return xSpeed * (float) xDirection;
	}

	public float getYVelocity() {
		return ySpeed * (float) yDirection;
	}

}
