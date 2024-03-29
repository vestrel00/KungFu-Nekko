package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.Methods;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class NekkoAISprite extends NekkoSprite {

	public NekkoAISprite(Nekko nekko, TextureAtlas atlas, Color color) {
		super(nekko, atlas, color);
	}

	@Override
	protected void initProjectiles(TextureAtlas atlas) {
		projectiles = new Projectile[10];
		for (int i = 0; i < projectiles.length; i++)
			projectiles[i] = new Projectile(KFNekko.enemies);
		fastShotRegions = new AtlasRegion[7];
		jumpShotRegions = new AtlasRegion[7];
		powerShotRegions = new AtlasRegion[7];
		superShotRegions = new AtlasRegion[7];
		for (int i = 0; i < 7; i++) {
			String iStr = String.valueOf(i);
			fastShotRegions[i] = atlas.findRegion("shot" + iStr);
			jumpShotRegions[i] = atlas.findRegion("shotDown" + iStr);
			powerShotRegions[i] = atlas.findRegion("chargedShot" + iStr);
			superShotRegions[i] = atlas.findRegion("superShot" + iStr);
		}
	}

	@Override
	protected void activateEffect() {
		// do not exceed a certain value (maybe 20?) since it might cause you to
		// fall of the map!
		switch (nekko.combatState) {
		// step back for projectile attacks
		case FASTSHOT:
		case JUMPSHOT:
			stepForward(-8.0f);
			break;
		case POWERSHOT:
			stepForward(-14.0f);
			stepForward(-14.0f);
			break;
		case SUPERSHOT:
			stepForward(-14.0f);
			stepForward(-14.0f);
			stepForward(-14.0f);
			stepForward(-14.0f);
			break;
		case GATTLINGSHOT:
			stepForward(-6.0f);
			break;
		default: // forward for melee
			stepForward(14.0f);
			break;
		}
	}

	@Override
	public void update() {
		Methods.updateColor(color, targetColor, colorSpeed);
		// update projectiles
		for (int i = 0; i < projectiles.length; i++)
			projectiles[i].update();

		if (TimeUtils.nanoTime() - lastAnimationTime < animationDelay)
			return;
		lastAnimationTime = TimeUtils.nanoTime();

		xScale = (nekko.faceState == FaceState.LEFT) ? -1.0f : 1.0f;

		switchCombatImages();

		if (switchStatusState())
			return;

		if (nekko.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK
				&& switchCombatState())
			return;
		if (nekko.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK
				&& switchVerticalMotionState())
			return;

		switchHorizontalMotionState();
	}

	protected void switchCombatImages() {
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (actor.visibility == Visibility.VISIBLE) {
			batch.setColor(color);
			// draw currentTexture
			batch.draw(currentTexture, actor.location.rect.x,
					actor.location.rect.y, actor.location.rect.width * 0.5f,
					actor.location.rect.height * 0.5f,
					actor.location.rect.width, actor.location.rect.height,
					xScale, 1.0f, rotation);
			batch.setColor(color);
			// draw projectiles
			for (int i = 0; i < projectiles.length; i++)
				projectiles[i].draw(batch);
			if (cameraEffect)
				batch.setColor(KFNekko.worldColor);
		}
	}

	public void activateSmoke() {

	}

	@Override
	protected void initEffects(TextureAtlas atlas) {
	}

	@Override
	protected boolean switchStatusState() {
		switch (nekko.statusState) {
		case DYING:
			if (++dyingIndex == dying.length) {
				dyingIndex = dying.length - 2;
				nekko.statusState = StatusState.DEAD;
				// no game over
			}
			currentTexture = dying[dyingIndex];
			animationDelay = 80000000L;
			return true;
		default:
			return false;
		}
	}

}
