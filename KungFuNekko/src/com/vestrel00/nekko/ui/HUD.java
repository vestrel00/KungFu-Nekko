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

package com.vestrel00.nekko.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Nekko;
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

	public HUD(Nekko player, HUDInputProcessor processor) {
		this.player = player;
		shape = new ShapeRenderer();
		// ui = new HUDPad(player, processor);
		ui = new HUDSimple(player, processor);
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	public void update() {
		ui.update();

		// -y(-10) <-------------landscape---------------> +y
		float accel = Gdx.input.getAccelerometerY();
		if (accel < -0.6f) {
			player.faceState = FaceState.LEFT;
			player.setHorizontalMotionState(HorizontalMotionState.MOVING);
			if (accel < -1.5f)
				accel = -1.5f;
			if (player.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
				if ((player.location.speed.maxXSpeed = -accel * 5.0f) < -7.0f)
					player.location.speed.maxXSpeed = -7.0f;
		} else if (accel > 0.6f) {
			player.faceState = FaceState.RIGHT;
			player.setHorizontalMotionState(HorizontalMotionState.MOVING);
			if (accel > 1.5f)
				accel = 1.5f;
			if (player.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
				if ((player.location.speed.maxXSpeed = accel * 5.0f) > 7.0f)
					player.location.speed.maxXSpeed = 7.0f;
		} else {
			if (player.horizontalMotionState != HorizontalMotionState.FORCED_MOVING) {
				// player.setHorizontalMotionState(HorizontalMotionState.IDLE);
				// player.location.speed.maxXSpeed = 0.0f;
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
