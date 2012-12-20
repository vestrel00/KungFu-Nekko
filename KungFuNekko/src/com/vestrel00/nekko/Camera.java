package com.vestrel00.nekko;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Updatable;

public class Camera implements Updatable {

	public OrthographicCamera camera;
	public Rectangle rect;

	public Camera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, KFNekko.settings.viewWidth,
				KFNekko.settings.viewHeight);
		rect = new Rectangle();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		rect.set(camera.position.x - KFNekko.settings.viewWidthHalf,
				camera.position.y - KFNekko.settings.viewHeightHalf,
				KFNekko.settings.viewWidth, KFNekko.settings.viewHeight);

		camera.update();
	}

}
