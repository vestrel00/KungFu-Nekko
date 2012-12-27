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
