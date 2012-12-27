package com.vestrel00.nekko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public Preferences prefs = Gdx.app.getPreferences("Nekko Settings");

	public float viewWidth, viewHeight, viewWidthHalf, viewHeightHalf,
			viewWidthQuarter, viewHeightQuarter;
	public boolean soundOn, musicOn;

	public Settings() {
		Gdx.input.setCatchBackKey(false);
		Gdx.input.setCatchMenuKey(false);

		soundOn = prefs.getBoolean("soundOn", true);
		musicOn = prefs.getBoolean("musicOn", true);

		viewWidth = 480.0f;
		viewHeight = 320.0f;
		viewWidthHalf = viewWidth * 0.5f;
		viewHeightHalf = viewHeight * 0.5f;
		viewWidthQuarter = viewWidth * 0.25f;
		viewHeightQuarter = viewHeight * 0.25f;
	}

	public void commit() {
		prefs.putBoolean("soundOn", soundOn);
		prefs.putBoolean("musicOn", musicOn);
		prefs.flush();
	}

}
