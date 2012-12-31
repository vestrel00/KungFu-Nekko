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

package com.vestrel00.nekko.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.components.PowerUp;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.HUDUI;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.ui.components.HUDInputProcessor;
import com.vestrel00.nekko.ui.components.HUDSimple;

public class HUD implements Updatable, Drawable, Disposable, Touchable {

	public ShapeRenderer shape;
	public HUDUI ui;
	private Nekko player;
	public HUDInputProcessor processor;

	private float accelMult, maxSpeed;

	public HUD(Nekko player, HUDInputProcessor processor) {
		this.player = player;
		shape = new ShapeRenderer();
		// ui = new HUDPad(player, processor);
		ui = new HUDSimple(player, processor);
		this.processor = processor;
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void update() {
		ui.update();

		if (player.powerUp == PowerUp.QUICKFEET) {
			accelMult = 9.0f;
			maxSpeed = 13.0f;
		} else {
			accelMult = 6.0f;
			maxSpeed = 9.0f;
		}

		if (KFNekko.view == KFNekko.VIEW_GAME) {
			// -y(-10) <-------------landscape---------------> +y
			float accel = Gdx.input.getAccelerometerY();
			if (accel < -0.6f) {
				player.faceState = FaceState.LEFT;
				player.setHorizontalMotionState(HorizontalMotionState.MOVING);
				if (accel < -1.5f)
					accel = -1.5f;
				if (player.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
					if ((player.location.speed.maxXSpeed = -accel * accelMult) < -maxSpeed)
						player.location.speed.maxXSpeed = -maxSpeed;
			} else if (accel > 0.6f) {
				player.faceState = FaceState.RIGHT;
				player.setHorizontalMotionState(HorizontalMotionState.MOVING);
				if (accel > 1.5f)
					accel = 1.5f;
				if (player.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
					if ((player.location.speed.maxXSpeed = accel * accelMult) > maxSpeed)
						player.location.speed.maxXSpeed = maxSpeed;
			} else {
				if (player.horizontalMotionState != HorizontalMotionState.FORCED_MOVING) {
					// Comment out for desktop build
					player.setHorizontalMotionState(HorizontalMotionState.IDLE);
					player.location.speed.maxXSpeed = 0.0f;
				}
			}
		}

	}

	@Override
	public void draw(SpriteBatch batch) {
		shape.setProjectionMatrix(KFNekko.camera.camera.combined);
		shape.setColor(Color.BLACK);
		shape.begin(ShapeType.FilledRectangle);
		// draw bottom cover
		shape.filledRect(KFNekko.camera.rect.x, KFNekko.camera.rect.y,
				KFNekko.camera.rect.width, KFNekko.camera.rect.height * 0.21f);
		// draw top cover
		shape.filledRect(KFNekko.camera.rect.x, KFNekko.camera.rect.y
				+ KFNekko.camera.rect.height * 0.9f, KFNekko.camera.rect.width,
				KFNekko.camera.rect.height * 0.1f);
		shape.end();
		shape.begin(ShapeType.FilledCircle);
		// top left circle
		shape.filledCircle(KFNekko.camera.rect.x + 50.0f,
				KFNekko.camera.rect.y + 350.0f, 100.0f);
		// top right circle
		shape.filledCircle(KFNekko.camera.rect.x + 430.0f,
				KFNekko.camera.rect.y + 350.0f, 100.0f);
		// bottom left circle
		// shape.filledCircle(KFNekko.camera.rect.x + 90.0f,
		// KFNekko.camera.rect.y, 100.0f);
		shape.end();

		batch.begin();
		ui.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		shape.dispose();
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		return ui.onTouchDown(x, y);
	}

}
