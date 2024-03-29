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

package com.vestrel00.nekko;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.interf.Updatable;

public class Camera implements Updatable {

	public static final int EFFECT_ZOOM = 0, EFFECT_SHAKE = 1, MODE_NORMAL = 2,
			MODE_SCROLL = 3;
	public static final int DEFAULT_NORMALIZE_SPEED = 2;

	public OrthographicCamera camera;
	public Rectangle rect;
	public Actor targetActor;
	public float xRange, yRange, zoom, targetZoom, zoomSpeed, shakeSpeed,
			shakeOrientation, shakeDirection;
	private long shakeStartTime, shakeDuration;
	private boolean zoomEffect, shakeEffect;
	public Vector2 targetLoc;
	public int mode = MODE_SCROLL, normalizeXSpeed, normalizeYSpeed;

	public Camera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, KFNekko.settings.viewWidth,
				KFNekko.settings.viewHeight);
		rect = new Rectangle();
		xRange = KFNekko.settings.viewWidth * 0.18f;
		yRange = KFNekko.settings.viewHeight * 0.15f;
		normalizeXSpeed = DEFAULT_NORMALIZE_SPEED;
		normalizeYSpeed = DEFAULT_NORMALIZE_SPEED;
		shakeDirection = 1.0f;
		targetZoom = 1.0f;
		zoom = 1.0f;
		targetLoc = new Vector2();
	}

	public void setEffect(int effect, float magnitude, float speed,
			long duration) {
		switch (effect) {
		case EFFECT_ZOOM:
			zoomEffect = true;
			zoom = targetZoom - magnitude;
			zoomSpeed = speed;
			break;
		case EFFECT_SHAKE:
			// magnitude = shake orientation
			// -1 = Horizontal shake
			// 1 = vertical shake
			shakeStartTime = TimeUtils.nanoTime();
			shakeEffect = true;
			shakeOrientation = magnitude;
			shakeDuration = duration;
			shakeSpeed = speed;
			break;
		}
	}

	@Override
	public void update() {
		if (targetActor != null)
			targetLoc.set(targetActor.location.x, targetActor.location.y);

		if (targetLoc.x > 0)
			switch (mode) {
			case MODE_NORMAL:
				if (targetLoc.x > camera.position.x + xRange)
					camera.position.x = targetLoc.x - xRange;
				else if (targetLoc.x < camera.position.x - xRange)
					camera.position.x = targetLoc.x + xRange;
				else if (targetLoc.x - KFNekko.settings.viewWidthHalf >= 0
						&& targetLoc.x + KFNekko.settings.viewWidthHalf < KFNekko.map.width)
					normalizeX();

				// force the camera to not pass the edge of the map
				if (camera.position.x - KFNekko.settings.viewWidthHalf < 0)
					camera.position.x = KFNekko.settings.viewWidthHalf;
				else if (camera.position.x + KFNekko.settings.viewWidthHalf > KFNekko.map.width)
					camera.position.x = KFNekko.map.width
							- KFNekko.settings.viewWidthHalf;

				if (targetLoc.y > camera.position.y + yRange)
					camera.position.y = targetLoc.y - yRange;
				else if (targetLoc.y < camera.position.y - yRange)
					camera.position.y = targetLoc.y + yRange;
				else
					normalizeY();
				break;
			case MODE_SCROLL:
				normalizeX();
				normalizeY();
				break;
			}

		rect.set(camera.position.x - KFNekko.settings.viewWidthHalf,
				camera.position.y - KFNekko.settings.viewHeightHalf,
				KFNekko.settings.viewWidth, KFNekko.settings.viewHeight);

		updateEffects();

		camera.zoom = zoom;
		camera.update();
	}

	private void updateEffects() {
		if (zoomEffect)
			if (zoom < targetZoom) {
				if ((zoom += zoomSpeed) > targetZoom) {
					zoom = targetZoom;
					zoomEffect = false;
				}
			} else if (zoom > targetZoom) {
				if ((zoom -= zoomSpeed) < targetZoom) {
					zoom = targetZoom;
					zoomEffect = false;
				}
			}

		if (shakeEffect) {
			if (shakeOrientation < 0.0f)
				camera.position.x += shakeDirection * shakeSpeed;
			else
				camera.position.y += shakeDirection * shakeSpeed;
			shakeDirection *= -1.0f;
			if (TimeUtils.nanoTime() - shakeStartTime > shakeDuration)
				shakeEffect = false;
		}

	}

	private void normalizeX() {
		if (targetLoc.x > camera.position.x
				&& (camera.position.x += normalizeXSpeed) > targetLoc.x)
			camera.position.x = targetLoc.x;
		else if (targetLoc.x < camera.position.x
				&& (camera.position.x -= normalizeXSpeed) < targetLoc.x) {
			camera.position.x = targetLoc.x;
		}
	}

	private void normalizeY() {
		if (targetLoc.y > camera.position.y
				&& (camera.position.y += normalizeYSpeed) > targetLoc.y)
			camera.position.y = targetLoc.y;
		else if (targetLoc.y < camera.position.y
				&& (camera.position.y -= normalizeYSpeed) < targetLoc.y) {
			camera.position.y = targetLoc.y;
		}
	}

	public void reset() {
		camera.position.x = KFNekko.settings.viewWidthHalf;
		camera.position.y = KFNekko.settings.viewHeightHalf;
		camera.zoom = 1.0f;
		camera.update();
		rect.set(camera.position.x - KFNekko.settings.viewWidthHalf,
				camera.position.y - KFNekko.settings.viewHeightHalf,
				KFNekko.settings.viewWidth, KFNekko.settings.viewHeight);

	}

	public void centerAt(Actor actor) {
		camera.position.x = actor.location.x;
		camera.position.y = actor.location.y;
		update();
		rect.set(camera.position.x - KFNekko.settings.viewWidthHalf,
				camera.position.y - KFNekko.settings.viewHeightHalf,
				KFNekko.settings.viewWidth, KFNekko.settings.viewHeight);
	}

}
