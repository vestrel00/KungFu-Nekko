package com.vestrel00.nekko.ui.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.interf.HUDUI;

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
			if (++player.health >= player.maxHealth)
				player.health = player.maxHealth;
		} else if (topRight.contains(x, y)) {
			if (++player.stamina >= player.maxStamina)
				player.stamina = player.maxStamina;
		} else
			player.jump();

		return true;
	}
}