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

package com.vestrel00.nekko.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.LevelManager;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.components.MapPieceGenerator;
import com.vestrel00.nekko.maps.components.MapSection;
import com.vestrel00.nekko.maps.components.Platform;

public class Map implements Updatable, Drawable {

	public static final int LAST_STAND = 0;

	private Array<MapSection> sections;
	public LevelManager manager;
	public Platform platform;
	public float width, height;

	public Map() {
		MapPieceGenerator.init();
	}

	public void setLevel(int mode) {
		sections = new Array<MapSection>();
		switch (mode) {
		case LAST_STAND:
			manager = new LastStand(sections);
			break;
		}
		platform = new Platform(sections);
	}

	@Override
	public void update() {
		manager.update();
		for (int i = 0; i < KFNekko.enemies.size; i++)
			KFNekko.enemies.get(i).update();
	}

	@Override
	public void draw(SpriteBatch batch) {
		for (int i = 0; i < sections.size; i++)
			sections.get(i).draw(batch);
		for (int i = 0; i < KFNekko.enemies.size; i++)
			KFNekko.enemies.get(i).draw(batch);
	}

}
