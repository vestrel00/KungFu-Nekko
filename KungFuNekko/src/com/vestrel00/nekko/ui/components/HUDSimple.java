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

package com.vestrel00.nekko.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.interf.HUDUI;
import com.vestrel00.nekko.maps.Map;

public class HUDSimple implements HUDUI {

	private Array<Rectangle> baseRects, rects;
	private Array<AtlasRegion> regions;
	// screen rectangles
	private Rectangle attack1Base, attack2Base, attack3Base, attack4Base,
			pauseBase, optionsBase, topLeftBase, topRightBase;
	// scene rectangles
	private Rectangle attack1, attack2, attack3, attack4, pause, options,
			topLeft, topRight;
	private AtlasRegion topRightRegion, topLeftRegion, attack1Region,
			attack2Region, attack3Region, attack4Region, pauseRegion,
			optionsRegion;

	private Nekko player;
	private HUDInputProcessor processor;

	// for CatsInArms mode (I KNOW IT SHOULDN"T BE HERE)
	// but too lazy to... ehhh XD
	private AtlasRegion catPortrait;
	private Rectangle redCatRectBase, blackCatRectBase, pinkCatRectBase,
			blueCatRectBase, yellowCatRectBase;
	private Rectangle redCatRect, blackCatRect, pinkCatRect, blueCatRect,
			yellowCatRect;

	public HUDSimple(Nekko player, HUDInputProcessor processor) {
		this.player = player;
		this.processor = processor;
		initRegions();
		initRects();
	}

	private void initRegions() {
		topLeftRegion = KFNekko.resource.atlas.findRegion("topLeftCorner");
		topRightRegion = KFNekko.resource.atlas.findRegion("topRightCorner");
		attack1Region = KFNekko.resource.atlas.findRegion("jump");
		attack2Region = KFNekko.resource.atlas.findRegion("attack");
		attack3Region = KFNekko.resource.atlas.findRegion("attack");
		attack4Region = KFNekko.resource.atlas.findRegion("jump");
		pauseRegion = KFNekko.resource.atlas.findRegion("pause");
		optionsRegion = KFNekko.resource.atlas.findRegion("options");

		regions = new Array<AtlasRegion>();
		regions.add(attack1Region);
		regions.add(attack2Region);
		regions.add(attack3Region);
		regions.add(attack4Region);
		regions.add(pauseRegion);
		regions.add(optionsRegion);

		// CatsInArms
		catPortrait = KFNekko.resource.atlas.findRegion("catPortrait");
	}

	private void initRects() {
		// SCREEN RECTS
		topLeftBase = new Rectangle(8.0f, 264.0f,
				(float) topLeftRegion.originalWidth,
				(float) topLeftRegion.originalHeight);
		topRightBase = new Rectangle(361.0f, 264.0f,
				(float) topRightRegion.originalWidth,
				(float) topRightRegion.originalHeight);
		attack1Base = new Rectangle(36.0f, 9.0f,
				(float) attack1Region.originalWidth,
				(float) attack1Region.originalHeight);
		attack2Base = new Rectangle(104.0f, 9.0f,
				(float) attack2Region.originalWidth,
				(float) attack2Region.originalHeight);
		attack3Base = new Rectangle(328.0f, 9.0f,
				(float) attack4Region.originalWidth,
				(float) attack4Region.originalHeight);
		attack4Base = new Rectangle(396.0f, 9.0f,
				(float) attack3Region.originalWidth,
				(float) attack3Region.originalHeight);
		pauseBase = new Rectangle(252.0f, 13.0f,
				(float) pauseRegion.originalWidth,
				(float) pauseRegion.originalHeight);
		optionsBase = new Rectangle(183.0f, 13.0f,
				(float) optionsRegion.originalWidth,
				(float) optionsRegion.originalHeight);

		baseRects = new Array<Rectangle>();
		baseRects.add(attack1Base);
		baseRects.add(attack2Base);
		baseRects.add(attack3Base);
		baseRects.add(attack4Base);
		baseRects.add(pauseBase);
		baseRects.add(optionsBase);
		// ///

		// SCENE
		topLeft = new Rectangle(topLeftBase);
		topRight = new Rectangle(topRightBase);
		attack1 = new Rectangle(attack1Base);
		attack2 = new Rectangle(attack2Base);
		attack3 = new Rectangle(attack3Base);
		attack4 = new Rectangle(attack4Base);
		pause = new Rectangle(pauseBase);
		options = new Rectangle(optionsBase);

		rects = new Array<Rectangle>();
		rects.add(attack1);
		rects.add(attack2);
		rects.add(attack3);
		rects.add(attack4);
		rects.add(pause);
		rects.add(options);

		// CatsInArms
		redCatRectBase = new Rectangle(22, 67,
				(float) catPortrait.originalWidth,
				(float) catPortrait.originalHeight);
		blackCatRectBase = new Rectangle(112, 67,
				(float) catPortrait.originalWidth,
				(float) catPortrait.originalHeight);
		pinkCatRectBase = new Rectangle(202, 67,
				(float) catPortrait.originalWidth,
				(float) catPortrait.originalHeight);
		blueCatRectBase = new Rectangle(292, 67,
				(float) catPortrait.originalWidth,
				(float) catPortrait.originalHeight);
		yellowCatRectBase = new Rectangle(382, 67,
				(float) catPortrait.originalWidth,
				(float) catPortrait.originalHeight);

		redCatRect = new Rectangle(redCatRectBase);
		blackCatRect = new Rectangle(blackCatRectBase);
		pinkCatRect = new Rectangle(pinkCatRectBase);
		blueCatRect = new Rectangle(blueCatRectBase);
		yellowCatRect = new Rectangle(yellowCatRectBase);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		for (int i = 0; i < rects.size; i++)
			batch.draw(regions.get(i), rects.get(i).x, rects.get(i).y,
					rects.get(i).width, rects.get(i).height);

		// health
		float c = 1.0f - (float) player.health / (float) player.maxHealth;
		batch.setColor(1.0f, c, c, 1.0f);
		batch.draw(topLeftRegion, topLeft.x, topLeft.y, topLeft.width,
				topLeft.height);
		// stamina
		c = 1.0f - (float) player.stamina / (float) player.maxStamina;
		batch.setColor(1.0f, 1.0f, c, 1.0f);
		batch.draw(topRightRegion, topRight.x, topRight.y, topRight.width,
				topRight.height);

		// CatsInArms
		if (KFNekko.map.mode == Map.CATS_IN_ARMS) {
			batch.setColor(1, 0, 0, 0.40f); // red
			batch.draw(catPortrait, redCatRect.x, redCatRect.y,
					redCatRect.width, redCatRect.height);
			batch.setColor(0.25f, 0.25f, 0.25f, 0.40f); // black
			batch.draw(catPortrait, blackCatRect.x, blackCatRect.y,
					blackCatRect.width, blackCatRect.height);
			batch.setColor(1, 0.68f, 0.68f, 0.40f); // pink
			batch.draw(catPortrait, pinkCatRect.x, pinkCatRect.y,
					pinkCatRect.width, pinkCatRect.height);
			batch.setColor(0, 0, 1, 0.40f); // blue
			batch.draw(catPortrait, blueCatRect.x, blueCatRect.y,
					blueCatRect.width * 0.5f, blueCatRect.height * 0.5f,
					blueCatRect.width, blueCatRect.height, -1.0f, 1.0f, 0.0f);
			batch.setColor(0, 1, 1, 0.40f); // cyan
			batch.draw(catPortrait, yellowCatRect.x, yellowCatRect.y,
					yellowCatRect.width * 0.5f, yellowCatRect.height * 0.5f,
					yellowCatRect.width, yellowCatRect.height, -1.0f, 1.0f,
					0.0f);
		}
		batch.setColor(KFNekko.worldColor);
	}

	@Override
	public void update() {
		// update scene rectangles
		for (int i = 0; i < rects.size; i++) {
			rects.get(i).x = baseRects.get(i).x + KFNekko.camera.rect.x;
			rects.get(i).y = baseRects.get(i).y + KFNekko.camera.rect.y;
		}
		topRight.x = topRightBase.x + KFNekko.camera.rect.x;
		topRight.y = topRightBase.y + KFNekko.camera.rect.y;
		topLeft.x = topLeftBase.x + KFNekko.camera.rect.x;
		topLeft.y = topLeftBase.y + KFNekko.camera.rect.y;

		// CatsInArms
		redCatRect.x = redCatRectBase.x + KFNekko.camera.rect.x;
		redCatRect.y = redCatRectBase.y + KFNekko.camera.rect.y;
		blackCatRect.x = blackCatRectBase.x + KFNekko.camera.rect.x;
		blackCatRect.y = blackCatRectBase.y + KFNekko.camera.rect.y;
		pinkCatRect.x = pinkCatRectBase.x + KFNekko.camera.rect.x;
		pinkCatRect.y = pinkCatRectBase.y + KFNekko.camera.rect.y;
		blueCatRect.x = blueCatRectBase.x + KFNekko.camera.rect.x;
		blueCatRect.y = blueCatRectBase.y + KFNekko.camera.rect.y;
		yellowCatRect.x = yellowCatRectBase.x + KFNekko.camera.rect.x;
		yellowCatRect.y = yellowCatRectBase.y + KFNekko.camera.rect.y;

	}

	/**
	 * <ul>
	 * <b>CombatStateManager Guide</b>
	 * <li>attack1 = INPUT_LEFT</li>
	 * <li>attack2 = INPUT_RIGHT</li>
	 * <li>attack3 = INPUT_UP</li>
	 * <li>attack4 = INPUT_DOWN</li>
	 * </ul>
	 */
	@Override
	public boolean onTouchDown(float x, float y) {
		// CatsInArms
		if (KFNekko.map.mode == Map.CATS_IN_ARMS) {
			if (redCatRect.contains(x, y)) {
				if (KFNekko.map.manager.getHelper().spawnCat(1) != null)
					KFNekko.audio.meow(x);
				return true;
			} else if (blackCatRect.contains(x, y)) {
				if (KFNekko.map.manager.getHelper().spawnCat(2) != null)
					KFNekko.audio.meow(x);
				return true;
			} else if (pinkCatRect.contains(x, y)) {
				if (KFNekko.map.manager.getHelper().spawnCat(4) != null)
					KFNekko.audio.meow(x);
				return true;
			} else if (blueCatRect.contains(x, y)) {
				if (KFNekko.map.manager.getHelper().spawnCat(3) != null)
					KFNekko.audio.meow(x);
				return true;
			} else if (yellowCatRect.contains(x, y)) {
				if (KFNekko.map.manager.getHelper().spawnCat(5) != null)
					KFNekko.audio.meow(x);
				return true;
			}

		}

		if (attack1.contains(x, y))
			player.setCombatState(processor.attackManager
					.input(ComboAttackManager.INPUT_LEFT));
		else if (attack2.contains(x, y))
			player.setCombatState(processor.attackManager
					.input(ComboAttackManager.INPUT_RIGHT));
		else if (attack3.contains(x, y))
			player.setCombatState(processor.attackManager
					.input(ComboAttackManager.INPUT_UP));
		else if (attack4.contains(x, y))
			player.setCombatState(processor.attackManager
					.input(ComboAttackManager.INPUT_DOWN));
		else if (topLeft.contains(x, y)) {
			// nothing?
			// if (++player.health >= player.maxHealth)
			// player.health = player.maxHealth;
		} else if (topRight.contains(x, y)) {
			// nothing?
			// if ((player.stamina += 2) >= player.maxStamina)
			// player.stamina = player.maxStamina;
			// manually check for overlap to enlarge the detection
			// without enlarging the texture or using another rectangle
			// same thing for Instructions.java
		} else if (x > pause.x && x < pause.x + pause.width
				&& y > pause.y - 10.0f && y < pause.y + pause.height + 10.0f) {
			KFNekko.view = KFNekko.VIEW_PAUSED;
			KFNekko.map.manager.pause();
			KFNekko.audio.touch();
		} else if (x > options.x && x < options.x + options.width
				&& y > options.y - 10.0f
				&& y < options.y + options.height + 10.0f) {
			KFNekko.view = KFNekko.VIEW_OPTIONS;
			KFNekko.map.manager.pause();
			KFNekko.audio.touch();
		} else
			player.jump();

		return true;
	}

	@Override
	public Array<Rectangle> getBaseRects() {
		Array<Rectangle> rects = new Array<Rectangle>();
		rects.add(attack1Base);
		rects.add(attack2Base);
		rects.add(attack3Base);
		rects.add(attack4Base);
		rects.add(pauseBase);
		rects.add(optionsBase);
		rects.add(topLeftBase);
		rects.add(topRightBase);
		return rects;
	}

	@Override
	public Array<Rectangle> getQuitResumeRects() {
		Array<Rectangle> rects = new Array<Rectangle>();
		rects.add(topLeftBase);
		rects.add(topRightBase);
		return rects;
	}
}
