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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.Map;

public class IntroSelectionManager implements Updatable, Drawable, Touchable {

	private final CharSequence[] MODE_STR = { "The Last Stand", "Cats In Arms",
			"Coming Soon", "Coming Soon" };
	private final CharSequence[] LS_MAP_STR = { "Final Fortress",
			"Road of Martyrs", "King & Queens", "Coming Soon" };

	private final CharSequence[] CIA_MAP_STR = { "Front Lines", "Coming Soon",
			"Coming Soon", "Coming Soon" };

	public static final int VIEW_MODE = 0, VIEW_MAP = 1, VIEW_MAP_LS = 2,
			VIEW_MAP_CIA = 3, VIEW_MAP_MODE3 = 4, VIEW_MAP_MODE4 = 5;

	private CharSequence[] chosenMode;
	private float[] modeWidths, lsMapModeWidths, chosenMapModeWidths,
			ciaMapModeWidths;
	public int view = VIEW_MODE, mapView, phase = 0, nextView;
	public Color color;
	private AtlasRegion buttonRegion;
	private Rectangle lsRect, ciaRect, collectorRect, adventurerRect;

	public IntroSelectionManager(TextureAtlas atlas) {
		color = new Color(Color.CLEAR);
		buttonRegion = atlas.findRegion("button_large");
		modeWidths = new float[MODE_STR.length];
		lsMapModeWidths = new float[LS_MAP_STR.length];
		ciaMapModeWidths = new float[CIA_MAP_STR.length];
		for (int i = 0; i < modeWidths.length; i++)
			modeWidths[i] = KFNekko.resource.chunkFive.getBounds(MODE_STR[i]).width;
		for (int i = 0; i < lsMapModeWidths.length; i++)
			lsMapModeWidths[i] = KFNekko.resource.chunkFive
					.getBounds(LS_MAP_STR[i]).width;
		for (int i = 0; i < ciaMapModeWidths.length; i++)
			ciaMapModeWidths[i] = KFNekko.resource.chunkFive
					.getBounds(CIA_MAP_STR[i]).width;

		lsRect = new Rectangle(KFNekko.camera.camera.position.x
				- (float) buttonRegion.originalWidth * 0.5f, 216.0f,
				(float) buttonRegion.originalWidth,
				(float) buttonRegion.originalHeight);
		ciaRect = new Rectangle(lsRect.x, 150.0f, lsRect.width, lsRect.height);
		collectorRect = new Rectangle(lsRect.x, 84.0f, lsRect.width,
				lsRect.height);
		adventurerRect = new Rectangle(lsRect.x, 20.0f, lsRect.width,
				lsRect.height);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(color);
		batch.draw(buttonRegion, lsRect.x, lsRect.y, lsRect.width,
				lsRect.height);
		batch.draw(buttonRegion, ciaRect.x, ciaRect.y, ciaRect.width,
				ciaRect.height);
		batch.draw(buttonRegion, collectorRect.x, collectorRect.y,
				collectorRect.width, collectorRect.height);
		batch.draw(buttonRegion, adventurerRect.x, adventurerRect.y,
				adventurerRect.width, adventurerRect.height);
		batch.end();

		batch.begin();
		switch (view) {
		case VIEW_MODE:
			KFNekko.resource.chunkFive
					.setColor(0.4118f, 0.6157f, 1.0f, color.a);
			KFNekko.resource.chunkFive.draw(batch, MODE_STR[0],
					KFNekko.camera.camera.position.x - modeWidths[0] * 0.5f,
					lsRect.y + lsRect.height - 11.0f);
			KFNekko.resource.chunkFive.draw(batch, MODE_STR[1],
					KFNekko.camera.camera.position.x - modeWidths[1] * 0.5f,
					ciaRect.y + ciaRect.height - 11.0f);
			// TODO REMOVE
			KFNekko.resource.chunkFive.setColor(0.25f, 0.25f, 0.25f, color.a);
			KFNekko.resource.chunkFive.draw(batch, MODE_STR[2],
					KFNekko.camera.camera.position.x - modeWidths[2] * 0.5f,
					collectorRect.y + collectorRect.height - 11.0f);
			KFNekko.resource.chunkFive.draw(batch, MODE_STR[3],
					KFNekko.camera.camera.position.x - modeWidths[3] * 0.5f,
					adventurerRect.y + adventurerRect.height - 11.0f);
			break;
		case VIEW_MAP:
			KFNekko.resource.chunkFive
					.setColor(0.4118f, 0.6157f, 1.0f, color.a);
			KFNekko.resource.chunkFive.draw(batch, chosenMode[0],
					KFNekko.camera.camera.position.x - chosenMapModeWidths[0]
							* 0.5f, lsRect.y + lsRect.height - 11.0f);
			if (mapView == VIEW_MAP_CIA)
				// TODO REMOVE
				KFNekko.resource.chunkFive.setColor(0.25f, 0.25f, 0.25f,
						color.a);
			KFNekko.resource.chunkFive.draw(batch, chosenMode[1],
					KFNekko.camera.camera.position.x - chosenMapModeWidths[1]
							* 0.5f, ciaRect.y + ciaRect.height - 11.0f);
			KFNekko.resource.chunkFive.draw(batch, chosenMode[2],
					KFNekko.camera.camera.position.x - chosenMapModeWidths[2]
							* 0.5f, collectorRect.y + collectorRect.height
							- 11.0f);
			// TODO REMOVE
			KFNekko.resource.chunkFive.setColor(0.25f, 0.25f, 0.25f, color.a);
			KFNekko.resource.chunkFive.draw(batch, chosenMode[3],
					KFNekko.camera.camera.position.x - chosenMapModeWidths[3]
							* 0.5f, adventurerRect.y + adventurerRect.height
							- 11.0f);
			break;
		}
	}

	@Override
	public void update() {
		updateColor(0.4f);
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (lsRect.contains(x, y)) {
			// set the InputProcessor to an unresponsive state to prevent
			// calling the below method more than once
			KFNekko.hud.processor.isListening = false;
			// make sure that the parent view (IntroMenuManger) is on this
			// view
			KFNekko.intro.menu.view = IntroMenuManager.VIEW_SELECTION;
			KFNekko.intro.menu.nextView = IntroMenuManager.VIEW_SELECTION;
			if (view == VIEW_MAP) {
				switch (mapView) {
				case VIEW_MAP_LS:
					KFNekko.map.setLevel(Map.LAST_STAND, Map.MAP_1);
					break;
				case VIEW_MAP_CIA:
					KFNekko.map.setLevel(Map.CATS_IN_ARMS, Map.MAP_1);
					KFNekko.hud.processor.isListening = false;
					phase = 2;
					break;
				case VIEW_MAP_MODE3:
					// TODO
					break;
				case VIEW_MAP_MODE4:
					// TODO
					break;
				}
				phase = 2;
			} else {
				mapView = VIEW_MAP_LS;
				chosenMode = LS_MAP_STR;
				chosenMapModeWidths = lsMapModeWidths;
				nextView = VIEW_MAP;
				phase = 1;
			}
			return true;
		} else if (ciaRect.contains(x, y)) {
			KFNekko.intro.menu.view = IntroMenuManager.VIEW_SELECTION;
			KFNekko.intro.menu.nextView = IntroMenuManager.VIEW_SELECTION;
			if (view == VIEW_MAP) {
				switch (mapView) {
				case VIEW_MAP_LS:
					KFNekko.map.setLevel(Map.LAST_STAND, Map.MAP_2);
					KFNekko.hud.processor.isListening = false;
					phase = 2;
					break;
				case VIEW_MAP_CIA:
					// TODO
					break;
				case VIEW_MAP_MODE3:
					// TODO
					break;
				case VIEW_MAP_MODE4:
					// TODO
					break;
				}
				// TODO
				// KFNekko.hud.processor.isListening = false;
				// phase = 2;
			} else {
				KFNekko.hud.processor.isListening = false;
				mapView = VIEW_MAP_CIA;
				chosenMode = CIA_MAP_STR;
				chosenMapModeWidths = ciaMapModeWidths;
				nextView = VIEW_MAP;
				phase = 1;
			}
			return true;
		} else if (collectorRect.contains(x, y)) {
			// set the InputProcessor to an unresponsive state to prevent
			// calling the below method more than once
			// KFNekko.hud.processor.isListening = false;
			// make sure that the parent view (IntroMenuManger) is on this
			// view
			KFNekko.intro.menu.view = IntroMenuManager.VIEW_SELECTION;
			KFNekko.intro.menu.nextView = IntroMenuManager.VIEW_SELECTION;
			if (view == VIEW_MAP) {
				switch (mapView) {
				case VIEW_MAP_LS:
					KFNekko.map.setLevel(Map.LAST_STAND, Map.MAP_3);
					// TODO remove
					KFNekko.hud.processor.isListening = false;
					phase = 2;
					break;
				case VIEW_MAP_CIA:
					// TODO
					break;
				case VIEW_MAP_MODE3:
					// TODO
					break;
				case VIEW_MAP_MODE4:
					// TODO
					break;
				}
				// TODO
				// phase = 2;
			} else {
				// TODO
				// mapView = VIEW_MAP_LS;
				// chosenMode = LS_MAP_STR;
				// chosenMapModeWidths = lsMapModeWidths;
				// nextView = VIEW_MAP;
				// phase = 1;
			}
			return true;
		} else if (adventurerRect.contains(x, y)) {
			// TODO
			return true;
		} else {
			switch (view) {
			case VIEW_MAP:
				nextView = VIEW_MODE;
				phase = 1;
				return true;
			default:
				phase = 3;
				return false;
			}
		}
	}

	private boolean updateColor(float colorSpeed) {
		switch (phase) {
		case 0: // fade in view
			color.r += colorSpeed;
			color.g += colorSpeed;
			color.b += colorSpeed;
			color.a += colorSpeed;
			if (color.a > 1.0f) {
				color.r = 1.0f;
				color.g = 1.0f;
				color.b = 1.0f;
				color.a = 1.0f;
				return true;
			}
			break;
		case 1: // fade out to nextView
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				view = nextView;
				phase = 0;
				color.set(Color.CLEAR);
				KFNekko.hud.processor.isListening = true;
				return true;
			}
			break;
		case 2: // fade out to game level intro
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				KFNekko.view = KFNekko.VIEW_LEVEL_INTRO;
				phase = 0;
				color.set(Color.CLEAR);
				// reset the view
				view = VIEW_MODE;
				nextView = VIEW_MODE;
				// set the processor back to a responsive state
				KFNekko.hud.processor.isListening = true;
				return true;
			}
			break;
		case 3: // fade out to nothing
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				phase = 0;
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

}
