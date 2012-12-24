package com.vestrel00.nekko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class IntroScreen implements Updatable, Drawable, Disposable, Touchable {

	private static final int VIEW_SPLASH = 0, VIEW_MENU = 1,
			VIEW_SELECTION = 2;

	private static final CharSequence[] strings = { "HoaLW", "Productions",
			"Play", "Credits" };
	private static float[] widths;

	
	// TODO show on video change from sound to music
	// Sound in android may not be able to play onCreate or at least not right
	// after creation due to SoundPool implementation or LibGDX?
	private Music introsition;
	private TextureAtlas atlas;
	private StringTyper typer;
	private Color color;
	private ShapeRenderer shape;
	private Rectangle splashRect, playRect, creditsRect;

	private int view, phase;
	private long phaseEndTime;

	public IntroScreen() {
		introsition = Gdx.audio.newMusic(Gdx.files
				.internal("sound/introsition.mp3"));
		introsition.play();
		phase = 0;
		view = VIEW_SPLASH;
		color = new Color(Color.CLEAR);
		widths = new float[strings.length];
		for (int i = 0; i < widths.length; i++)
			widths[i] = KFNekko.resource.chunkFive.getBounds(strings[i]).width;
		typer = new StringTyper(strings[0], 300000000L);
		shape = new ShapeRenderer();
		splashRect = new Rectangle();
		playRect = new Rectangle(KFNekko.settings.viewWidthHalf
				- (widths[2] + 20.0f) * 0.5f,
				KFNekko.settings.viewHeightHalf + 30.0f, widths[2] + 20.0f,
				35.0f);
		creditsRect = new Rectangle(KFNekko.settings.viewWidthHalf
				- (widths[3] + 20.0f) * 0.5f, 100.0f, widths[3] + 20.0f, 35.0f);
	}

	@Override
	public void update() {
		switch (view) {
		case VIEW_SPLASH:
			updateSplash();
			break;
		case VIEW_MENU:
			updateMenu();
			break;
		case VIEW_SELECTION:

			break;
		}
	}

	private void updateMenu() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(SpriteBatch batch) {
		switch (view) {
		case VIEW_SPLASH:
			shape.begin(ShapeType.FilledRectangle);
			if (phase == 2)
				shape.setColor(color.a, color.a, color.a, color.a);
			else
				shape.setColor(Color.WHITE);
			shape.filledRect(splashRect.x, splashRect.y, splashRect.width,
					splashRect.height);
			shape.setColor(Color.BLACK);
			shape.filledRect(splashRect.x, 0.0f, KFNekko.settings.viewWidth,
					70.0f);
			shape.filledRect(splashRect.x, KFNekko.settings.viewHeight - 70.0f,
					KFNekko.settings.viewWidth, 70.0f);
			shape.end();

			batch.begin();
			KFNekko.resource.chunkFive.setColor(color);
			KFNekko.resource.chunkFive.draw(batch, typer.getTypedStr(),
					KFNekko.camera.camera.position.x - widths[0] * 0.5f,
					KFNekko.camera.camera.position.y + 30.0f);
			KFNekko.resource.chunkFive.draw(batch, strings[1],
					KFNekko.camera.camera.position.x - widths[1] * 0.5f,
					KFNekko.camera.camera.position.y - 5.0f);
			batch.end();
			break;
		case VIEW_MENU:
			shape.begin(ShapeType.Rectangle);
			shape.setColor(Color.WHITE);
			shape.rect(playRect.x, playRect.y, playRect.width, playRect.height);
			shape.rect(creditsRect.x, creditsRect.y, creditsRect.width,
					creditsRect.height);
			shape.end();

			batch.begin();
			KFNekko.resource.chunkFive.setColor(Color.WHITE);
			KFNekko.resource.chunkFive.draw(batch, strings[2],
					KFNekko.camera.camera.position.x - widths[2] * 0.5f,
					playRect.y + playRect.height - 6.0f);
			KFNekko.resource.chunkFive.draw(batch, strings[3],
					KFNekko.camera.camera.position.x - widths[3] * 0.5f,
					creditsRect.y + creditsRect.height - 6.0f);
			batch.end();
			break;
		case VIEW_SELECTION:

			break;
		}
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		switch (view) {
		case VIEW_SPLASH:

			break;
		case VIEW_MENU:
			if (playRect.contains(x, y)) {
				// TODO CHANGE
				KFNekko.view = KFNekko.VIEW_GAME;
				KFNekko.audio.music.play();
			}
			break;
		case VIEW_SELECTION:

			break;
		}
		return false;
	}

	@Override
	public void dispose() {
		atlas.dispose();
		introsition.dispose();
		shape.dispose();
	}

	private void updateSplash() {
		switch (phase) {
		case 0:
			splashRect
					.set(KFNekko.camera.rect.x,
							KFNekko.camera.camera.position.y
									- splashRect.height * 0.5f,
							splashRect.width + 10.0f, 4.0f);
			typer.update();
			color.r += 0.02f;
			color.g += 0.02f;
			color.b += 0.02f;
			color.a += 0.02f;
			if (color.a > 1.0f) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 1;
				color.r = 1.0f;
				color.g = 1.0f;
				color.b = 1.0f;
				color.a = 1.0f;
			}
			break;
		case 1:
			if ((color.r -= 0.02f) < 0.0f)
				color.r = 0.0f;
			if ((color.g -= 0.02f) < 0.0f)
				color.g = 0.0f;
			if ((color.b -= 0.02f) < 0.0f)
				color.b = 0.0f;
			splashRect.height += 4.0f;
			splashRect.y = KFNekko.camera.camera.position.y - splashRect.height
					* 0.5f;
			if (TimeUtils.nanoTime() - phaseEndTime > 2000000000L) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 2;
			}
			break;
		case 2:
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				color.a = 0.0f;
				phase = 0;
				view = VIEW_MENU;
			}
		}
	}

}