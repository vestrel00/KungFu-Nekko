package com.vestrel00.nekko;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.interf.Updatable;

public class Camera implements Updatable {

	public static final int EFFECT_ZOOM = 0, EFFECT_SHAKE = 1;

	public OrthographicCamera camera;
	public Rectangle rect;
	public Actor targetActor;
	public float xRange, yRange, normalizeSpeed, zoom, targetZoom, zoomSpeed,
			shakeSpeed, shakeOrientation, shakeDirection;
	private long shakeStartTime, shakeDuration;
	private boolean zoomEffect, shakeEffect;

	public Camera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, KFNekko.settings.viewWidth,
				KFNekko.settings.viewHeight);
		rect = new Rectangle();
		xRange = KFNekko.settings.viewWidth * 0.18f;
		yRange = KFNekko.settings.viewHeight * 0.15f;
		normalizeSpeed = 1.8f;
		shakeDirection = 1.0f;
		targetZoom = 1.0f;
		zoom = 1.0f;
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
		if (targetActor.location.x > camera.position.x + xRange)
			camera.position.x = targetActor.location.x - xRange;
		else if (targetActor.location.x < camera.position.x - xRange)
			camera.position.x = targetActor.location.x + xRange;
		else if (targetActor.location.x - KFNekko.settings.viewWidthHalf >= 0
				&& targetActor.location.x + KFNekko.settings.viewWidthHalf < KFNekko.map.width)
			normalizeX(targetActor);

		// force the camera to not pass the edge of the map
		if (camera.position.x - KFNekko.settings.viewWidthHalf < 0)
			camera.position.x = KFNekko.settings.viewWidthHalf;
		else if (camera.position.x + KFNekko.settings.viewWidthHalf > KFNekko.map.width)
			camera.position.x = KFNekko.map.width
					- KFNekko.settings.viewWidthHalf;

		if (targetActor.location.y > camera.position.y + yRange)
			camera.position.y = targetActor.location.y - yRange;
		else if (targetActor.location.y < camera.position.y - yRange)
			camera.position.y = targetActor.location.y + yRange;
		else
			normalizeY(targetActor);

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

	private void normalizeX(Actor actor) {
		if (actor.location.x > camera.position.x
				&& (camera.position.x += normalizeSpeed) > actor.location.x)
			camera.position.x = actor.location.x;
		else if (actor.location.x < camera.position.x
				&& (camera.position.x -= normalizeSpeed) < actor.location.x) {
			camera.position.x = actor.location.x;
		}
	}

	private void normalizeY(Actor actor) {
		if (actor.location.y > camera.position.y
				&& (camera.position.y += normalizeSpeed) > actor.location.y)
			camera.position.y = actor.location.y;
		else if (actor.location.y < camera.position.y
				&& (camera.position.y -= normalizeSpeed) < actor.location.y) {
			camera.position.y = actor.location.y;
		}
	}

}
