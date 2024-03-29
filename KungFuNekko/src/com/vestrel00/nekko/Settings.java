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

	public static final String LAST_STAND_1 = "Final Fortress",
			LAST_STAND_2 = "Road of Martyrs",
			LAST_STAND_3 = "Kings and Queens", LAST_STAND_4 = "TODO1",
			CATS_IN_ARMS_1 = "Front Lines", CATS_IN_ARMS_2 = "TODO2",
			CATS_IN_ARMS_3 = "TODO3", CATS_IN_ARMS_4 = "TODO4";

	public Preferences prefs = Gdx.app.getPreferences("Nekko Settings");

	public float viewWidth, viewHeight, viewWidthHalf, viewHeightHalf,
			viewWidthQuarter, viewHeightQuarter;
	public boolean soundOn, musicOn;
	// high scores
	public int lastStand_1, lastStand_2, lastStand_3, lastStand_4,
			catsInArms_1, catsInArms_2, catsInArms_3, catsInArms_4;

	public Settings() {
		Gdx.input.setCatchBackKey(false);
		Gdx.input.setCatchMenuKey(false);

		lastStand_1 = prefs.getInteger(LAST_STAND_1, 0);
		lastStand_2 = prefs.getInteger(LAST_STAND_2, 0);
		lastStand_3 = prefs.getInteger(LAST_STAND_3, 0);
		lastStand_3 = prefs.getInteger(LAST_STAND_3, 0);

		catsInArms_1 = prefs.getInteger(CATS_IN_ARMS_1, 999999);
		catsInArms_2 = prefs.getInteger(CATS_IN_ARMS_2, 999999);
		catsInArms_3 = prefs.getInteger(CATS_IN_ARMS_3, 999999);
		catsInArms_4 = prefs.getInteger(CATS_IN_ARMS_4, 999999);

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
		prefs.putInteger(LAST_STAND_1, lastStand_1);
		prefs.putInteger(LAST_STAND_2, lastStand_2);
		prefs.putInteger(LAST_STAND_3, lastStand_3);
		prefs.putInteger(LAST_STAND_4, lastStand_4);

		prefs.putInteger(CATS_IN_ARMS_1, catsInArms_1);
		prefs.putInteger(CATS_IN_ARMS_2, catsInArms_2);
		prefs.putInteger(CATS_IN_ARMS_3, catsInArms_3);
		prefs.putInteger(CATS_IN_ARMS_4, catsInArms_4);

		prefs.putBoolean("soundOn", soundOn);
		prefs.putBoolean("musicOn", musicOn);
		prefs.flush();
	}

}
