package com.vestrel00.nekko.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class HUD implements Updatable, Drawable, Disposable {

	private ShapeRenderer shape;

	public HUD() {
		shape = new ShapeRenderer();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

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
		shape.filledCircle(KFNekko.camera.rect.x + 90.0f,
				KFNekko.camera.rect.y, 100.0f);
		shape.end();
	}

	@Override
	public void dispose() {
		shape.dispose();
	}

}
