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
