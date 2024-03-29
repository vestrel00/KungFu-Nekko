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

package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.Methods;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.components.effects.AfterImage;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;

public class NekkoSprite extends Sprite {

	public static long lastCombatSound, lastStepSound;

	// animation frames
	protected AtlasRegion[] idle, walk, jump, dying, hurt, doubleJump,
			spinKick, powerShot, fastShot, flyingKick, superUppercut,
			oneTwoCombo, lowMiddleKick, highKick, downwardKick, twoSidedAttack,
			roundKick, uppercut, beatdown, lightningKicks, speedyHands,
			gattlingShot, jumpShot, superShot;
	public int idleIndex = 0, walkIndex = 0, jumpIndex = 0, hurtIndex = 0,
			doubleJumpIndex = 0, dyingIndex;

	// projectiles
	protected Projectile[] projectiles;
	protected AtlasRegion[] fastShotRegions, jumpShotRegions, powerShotRegions,
			superShotRegions;
	// afterImages
	protected AfterImage[] combatImages, effectImages;
	protected int combatImgIndex = 0, effectImgIndex = 0, projectileIndex = 0;
	protected AtlasRegion attackEffect1, smokeEffect;

	protected Nekko nekko;
	public long speedUp, lastCombatImage;
	public int damageMultiplier;
	public Color targetColor;
	public float colorSpeed;
	public boolean cameraEffect = false;

	public NekkoSprite(Nekko nekko, TextureAtlas atlas, Color color) {
		super(nekko, color);
		this.nekko = nekko;
		initTextures(atlas);
		initEffects(atlas);
		initProjectiles(atlas);
		speedUp = 0L;
		damageMultiplier = 1;
		colorSpeed = 0.04f;
		targetColor = new Color(color);
	}

	protected void initProjectiles(TextureAtlas atlas) {
		projectiles = new Projectile[30];
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

	protected void initEffects(TextureAtlas atlas) {
		combatImages = new AfterImage[10];
		effectImages = new AfterImage[10];
		for (int i = 0; i < combatImages.length; i++)
			combatImages[i] = new AfterImage();
		for (int i = 0; i < effectImages.length; i++)
			effectImages[i] = new AfterImage();
		attackEffect1 = atlas.findRegion("attackEffect1");
		smokeEffect = atlas.findRegion("smokeEffect");
	}

	protected void initTextures(TextureAtlas atlas) {
		hurt = new AtlasRegion[2];
		idle = new AtlasRegion[4];
		doubleJump = new AtlasRegion[4];
		highKick = new AtlasRegion[6];
		fastShot = new AtlasRegion[6];
		jumpShot = new AtlasRegion[6];
		powerShot = new AtlasRegion[7];
		dying = new AtlasRegion[7];
		uppercut = new AtlasRegion[7]; // added 1 frame
		flyingKick = new AtlasRegion[9];
		walk = new AtlasRegion[8];
		jump = new AtlasRegion[8];
		downwardKick = new AtlasRegion[9]; // added 1 frame
		twoSidedAttack = new AtlasRegion[9]; // added 1 frame
		roundKick = new AtlasRegion[9]; // added 1 frame
		spinKick = new AtlasRegion[10];
		oneTwoCombo = new AtlasRegion[10];
		lowMiddleKick = new AtlasRegion[12];
		superUppercut = new AtlasRegion[13];
		superShot = new AtlasRegion[13];
		// ender moves
		beatdown = new AtlasRegion[9];
		lightningKicks = new AtlasRegion[9];
		speedyHands = new AtlasRegion[9];
		gattlingShot = new AtlasRegion[9];
		// ///

		int i = 0;
		for (i = 0; i < 4; i++) {
			idle[i] = atlas.findRegion("nekkoIdle" + String.valueOf(i));
			doubleJump[i] = atlas.findRegion("nekkoDoubleJump"
					+ String.valueOf(i));
		}
		for (i = 0; i < 6; i++) {
			highKick[i] = atlas.findRegion("nekkoHighKick" + String.valueOf(i));
			fastShot[i] = atlas.findRegion("nekkoFastShot" + String.valueOf(i));
			jumpShot[i] = atlas.findRegion("nekkoJumpShot" + String.valueOf(i));
		}
		for (i = 0; i < 7; i++) {
			powerShot[i] = atlas.findRegion("nekkoPowerShot"
					+ String.valueOf(i));
			uppercut[i] = atlas.findRegion("nekkoUppercut" + String.valueOf(i));
			dying[i] = atlas.findRegion("nekkoHurt" + String.valueOf(i));
		}
		for (i = 0; i < 8; i++) {
			walk[i] = atlas.findRegion("nekkoWalk" + String.valueOf(i));
			jump[i] = atlas.findRegion("nekkoJump" + String.valueOf(i));
		}
		for (i = 0; i < 9; i++) {
			flyingKick[i] = atlas.findRegion("nekkoFlyingKick"
					+ String.valueOf(i));
			downwardKick[i] = atlas.findRegion("nekkoDownwardKick"
					+ String.valueOf(i));
			twoSidedAttack[i] = atlas.findRegion("nekkoTwoSidedAttack"
					+ String.valueOf(i));
			roundKick[i] = atlas.findRegion("nekkoRoundKick"
					+ String.valueOf(i));
		}
		for (i = 0; i < 10; i++) {
			spinKick[i] = atlas.findRegion("nekkoSpin" + String.valueOf(i));
			oneTwoCombo[i] = atlas.findRegion("nekkoOneTwoCombo"
					+ String.valueOf(i));
		}
		for (i = 0; i < 12; i++)
			lowMiddleKick[i] = atlas.findRegion("nekkoLowMiddleKick"
					+ String.valueOf(i));
		for (i = 0; i < 13; i++) {
			superUppercut[i] = atlas.findRegion("nekkoSuperUppercut"
					+ String.valueOf(i));
			superShot[i] = atlas.findRegion("nekkoSuperShot"
					+ String.valueOf(i));
		}

		// special moves
		for (int x = 0; x < 3; x++) {
			beatdown[x] = flyingKick[x + 1];
			lightningKicks[x] = flyingKick[x + 1];
			speedyHands[x] = flyingKick[x + 1];
			gattlingShot[x] = flyingKick[x + 1];
		}
		beatdown[3] = oneTwoCombo[5];
		beatdown[4] = oneTwoCombo[8];
		beatdown[5] = lowMiddleKick[2];
		beatdown[6] = lowMiddleKick[9];
		beatdown[7] = spinKick[7];
		beatdown[8] = spinKick[8];
		lightningKicks[3] = lowMiddleKick[2];
		lightningKicks[4] = lowMiddleKick[9];
		lightningKicks[5] = highKick[3];
		lightningKicks[6] = roundKick[5];
		lightningKicks[7] = downwardKick[6];
		lightningKicks[8] = downwardKick[7];
		speedyHands[3] = oneTwoCombo[5];
		speedyHands[4] = oneTwoCombo[8];
		speedyHands[5] = uppercut[4];
		speedyHands[6] = powerShot[5];
		speedyHands[7] = twoSidedAttack[5];
		speedyHands[8] = twoSidedAttack[7];
		gattlingShot[3] = oneTwoCombo[5];
		gattlingShot[4] = oneTwoCombo[8];
		gattlingShot[5] = oneTwoCombo[5];
		gattlingShot[6] = oneTwoCombo[8];
		gattlingShot[7] = superShot[6];
		gattlingShot[8] = superShot[7];
		// /////////

		hurt[0] = dying[1];
		hurt[1] = dying[2];

		currentTexture = idle[0];
	}

	@Override
	public void update() {
		Methods.updateColor(color, targetColor, colorSpeed);
		// update projectiles
		for (int i = 0; i < projectiles.length; i++)
			projectiles[i].update();
		// update after images
		for (int i = 0; i < combatImages.length; i++)
			combatImages[i].update();
		// update after images
		for (int i = 0; i < effectImages.length; i++)
			effectImages[i].update();

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

	protected boolean switchStatusState() {
		switch (nekko.statusState) {
		case DYING:
			if (++dyingIndex == dying.length) {
				dyingIndex = dying.length - 2;
				KFNekko.map.manager.getHelper().gameOver();
				// nekko.statusState = StatusState.DEAD;
			}
			currentTexture = dying[dyingIndex];
			animationDelay = 160000000L;
			return true;
		default:
			return false;
		}
	}

	protected void stepForward(float amount) {
		// step forward
		nekko.location.speed.xSpeed = amount;
		nekko.location.speed.xDirection = (nekko.faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
				: Speed.DIRECTION_RIGHT;
		nekko.location.update();
		nekko.location.speed.xSpeed = nekko.location.speed.maxXSpeed;

		if ((nekko.location.onSlope || nekko.location.onPlatform)
				&& TimeUtils.nanoTime() - lastStepSound > 100000000L) {
			lastStepSound = TimeUtils.nanoTime();
			KFNekko.audio.footStep(nekko.location.x);
		}
	}

	protected void activateEffect() {
		if (cameraEffect)
			KFNekko.bumpWC(0.1f, 0.1f, 0.1f);
		float zoomAmount = 0.0f;
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
			stepForward(14.0f);
			break;
		}

		switch (nekko.combatState) {
		case SPIN:
			zoomAmount = 0.07f;
			if (combatIndex == 7) {
				if (nekko.faceState == FaceState.LEFT)
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x - 25.0f, nekko.location.y - 8.0f,
							0.08f, -0.3f, false, 0.0f, color);
				else
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x + 25.0f, nekko.location.y - 8.0f,
							0.08f, 0.3f, true, 0.0f, color);
			}
			if (nekko.location.onPlatform || nekko.location.onSlope) {
				if (cameraEffect)
					KFNekko.camera.setEffect(Camera.EFFECT_SHAKE, 1.0f, 4.0f,
							300000000L);
				activateSmoke();
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.groundBoom(nekko.location.x);
				}
			}
			break;
		case JAB:
		case ONETWOCOMBO:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x + 10.0f, nekko.location.y - 5.0f,
						0.08f, -0.3f, false, 0.0f, color);
			else
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x + nekko.location.rect.width
								- 10.0f, nekko.location.y - 5.0f, 0.08f, 0.3f,
						true, 0.0f, color);
			zoomAmount = 0.035f;
			break;
		case FLYINGKICK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (combatIndex == 6) {
				if (nekko.faceState == FaceState.LEFT)
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x - 19.0f, nekko.location.y - 19.0f,
							0.08f, -0.3f, false, 0.0f, color);
				else
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x + 19.0f, nekko.location.y - 19.0f,
							0.08f, 0.3f, true, 0.0f, color);
			}
			zoomAmount = 0.05f;
			break;
		case SUPERUPPERCUT:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (combatIndex == 5) {
				if (nekko.faceState == FaceState.LEFT)
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x - 13.0f, nekko.location.y + 16.0f,
							0.08f, 0.0f, false, 0.0f, color);
				else
					effectImages[effectImgIndex].activate(attackEffect1,
							nekko.location.x + 13.0f, nekko.location.y + 16.0f,
							0.08f, 0.0f, true, 0.0f, color);
			}
			zoomAmount = 0.09f;
			break;
		case LOWMIDDLEKICK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex]
						.activate(attackEffect1, nekko.location.x
								- ((combatIndex == 2) ? 18.0f : 20.0f),
								nekko.location.y
										- ((combatIndex == 2) ? 25.0f : 14.0f),
								0.08f, -0.3f, false, 0.0f, color);
			else
				effectImages[effectImgIndex]
						.activate(attackEffect1, nekko.location.x
								+ ((combatIndex == 2) ? 18.0f : 20.0f),
								nekko.location.y
										- ((combatIndex == 2) ? 25.0f : 14.0f),
								0.08f, 0.3f, true, 0.0f, color);
			zoomAmount = 0.035f;
			stepForward(12.0f);
			break;
		case HIGHKICK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.x - 27.0f, nekko.location.y, 0.08f,
						0.0f, false, 0.0f, color);
			else
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.x + 27.0f, nekko.location.y, 0.08f,
						0.0f, true, 0.0f, color);
			zoomAmount = 0.035f;
			stepForward(12.0f);
			break;
		case DOWNWARDKICK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.x - 21.0f, nekko.location.y - 17.0f,
						0.08f, 0.0f, false, 0.0f, color);
			else
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.x + 21.0f, nekko.location.y - 17.0f,
						0.08f, 0.0f, true, 0.0f, color);
			zoomAmount = 0.05f;
			stepForward(12.0f);
			break;
		case TWOSIDEDATTACK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			effectImages[effectImgIndex].activate(
					attackEffect1, // left
					nekko.location.x - 22.0f, nekko.location.y - 7.0f, 0.08f,
					-12.0f, false, 2.0f, color);
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			effectImages[effectImgIndex].activate(
					attackEffect1,// right
					nekko.location.x + 22.0f, nekko.location.y - 7.0f, 0.08f,
					12.0f, true, 2.0f, color);

			combatImgIndex = Methods.incrementIndex(combatImgIndex,
					combatImages.length);
			combatImages[combatImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, 0.08f, -12.0f, true,
					2.0f, color);
			combatImgIndex = Methods.incrementIndex(combatImgIndex,
					combatImages.length);
			combatImages[combatImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, 0.08f, 12.0f, false,
					2.0f, color);
			zoomAmount = 0.08f;
			break;
		case ROUNDKICK:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x + 4.0f, nekko.location.y + 14.0f,
						0.08f, 0.0f, false, 0.0f, color);
			else
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x + nekko.location.rect.width
								- 4.0f, nekko.location.y + 14.0f, 0.08f, 0.0f,
						true, 0.0f, color);
			zoomAmount = 0.06f;
			break;
		case UPPERCUT:
			effectImgIndex = Methods.incrementIndex(effectImgIndex,
					effectImages.length);
			if (nekko.faceState == FaceState.LEFT)
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x, nekko.location.y + 20.0f, 0.08f,
						0.0f, false, 0.0f, color);
			else
				effectImages[effectImgIndex].activate(attackEffect1,
						nekko.location.rect.x + nekko.location.rect.width,
						nekko.location.y + 21.0f, 0.08f, 0.0f, true, 0.0f,
						color);
			zoomAmount = 0.065f;
			break;
		case BEATDOWN:
		case LIGHTNINGKICKS:
		case SPEEDYHANDS:
		case GATTLINGSHOT:
			zoomAmount = 0.2f;
			break;
		// projectile attacks:
		case FASTSHOT:
		case JUMPSHOT:
			zoomAmount = -0.07f;
			break;
		case SUPERSHOT:
		case POWERSHOT:
			zoomAmount = 0.07f;
			break;
		}
		if (cameraEffect)
			KFNekko.camera.setEffect(Camera.EFFECT_ZOOM, zoomAmount, 0.0063f,
					0L);
	}

	protected void switchCombatImages() {
		float alphaFade = 0.04f;
		switch (nekko.combatState) {
		case BEATDOWN:
		case LIGHTNINGKICKS:
		case SPEEDYHANDS:
		case GATTLINGSHOT:
			alphaFade = 0.01f;
			break;
		}
		if (nekko.combatState != CombatState.IDLE
				&& (TimeUtils.nanoTime() - lastCombatImage > 60000000L
						|| nekko.combatState == CombatState.BEATDOWN
						|| nekko.combatState == CombatState.SPEEDYHANDS
						|| nekko.combatState == CombatState.LIGHTNINGKICKS || nekko.combatState == CombatState.GATTLINGSHOT)) {
			lastCombatImage = TimeUtils.nanoTime();
			combatImgIndex = Methods.incrementIndex(combatImgIndex,
					combatImages.length);
			combatImages[combatImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, alphaFade, 0.0f,
					(nekko.faceState == FaceState.LEFT) ? true : false, 0.0f,
					color);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (actor.visibility == Visibility.VISIBLE) {
			// draw after images
			for (int i = 0; i < combatImages.length; i++)
				combatImages[i].draw(batch);
			batch.setColor(color);
			// draw currentTexture
			batch.draw(currentTexture, actor.location.rect.x,
					actor.location.rect.y, actor.location.rect.width * 0.5f,
					actor.location.rect.height * 0.5f,
					actor.location.rect.width, actor.location.rect.height,
					xScale, 1.0f, rotation);
			// draw after images
			for (int i = 0; i < effectImages.length; i++)
				effectImages[i].draw(batch);
			batch.setColor(color);
			// draw projectiles
			for (int i = 0; i < projectiles.length; i++)
				projectiles[i].draw(batch);
			if (cameraEffect)
				batch.setColor(KFNekko.worldColor);
		}
	}

	protected boolean switchHorizontalMotionState() {
		if (nekko.location.onPlatform || nekko.location.onSlope)
			switch (nekko.horizontalMotionState) {
			case IDLE:
				if (++idleIndex == idle.length)
					idleIndex = 0;
				currentTexture = idle[idleIndex];
				animationDelay = 120000000L;
				return true;
			case KNOCKED_BACK:
				if (++hurtIndex == hurt.length) {
					hurtIndex = 0;
					if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
						lastCombatSound = TimeUtils.nanoTime();
						KFNekko.audio.meow(nekko.location.x);
					}
				}
				currentTexture = hurt[hurtIndex];
				animationDelay = 100000000L;
				return true;
			case MOVING:
				if (++walkIndex == walk.length)
					walkIndex = 0;

				// footstep
				if (walkIndex == 0 || walkIndex == 4)
					if (TimeUtils.nanoTime() - lastStepSound > 100000000L) {
						lastStepSound = TimeUtils.nanoTime();
						KFNekko.audio.footStep(nekko.location.x);
					}

				currentTexture = walk[walkIndex];
				if (nekko.location.onSlope)
					animationDelay = 40000000L;
				else
					animationDelay = 20000000L;
				return true;
			default:
				return false;
			}
		else
			return false;
	}

	protected boolean switchVerticalMotionState() {
		if (nekko.location.doubleJumped) {
			if (++doubleJumpIndex >= doubleJump.length)
				doubleJumpIndex = 0;
			currentTexture = doubleJump[doubleJumpIndex];
			animationDelay = 50000000L;
			return true;
		}

		switch (nekko.verticalMotionState) {
		case JUMPING:
			if (++jumpIndex >= jump.length)
				return false;
			currentTexture = jump[jumpIndex];
			animationDelay = 60000000L;
			return true;
		default:
			return false;
		}
	}

	protected boolean switchCombatState() {
		switch (nekko.combatState) {
		case SPIN:
			if (++combatIndex == spinKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 7 || combatIndex == spinKick.length - 1) {
				nekko.attack(2 * damageMultiplier, attackAOE, 10.0f);
				activateEffect();
			}
			currentTexture = spinKick[combatIndex];
			animationDelay = 40000000L;
			return true;
		case JAB:
			if (++combatIndex == fastShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4) {
				nekko.attack(1 * damageMultiplier, attackAOE, 20.0f);
				activateEffect();
			}
			currentTexture = fastShot[combatIndex];
			animationDelay = 30000000L - speedUp;
			return true;
		case FLYINGKICK:
			if (++combatIndex == flyingKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 0 || combatIndex == 3 || combatIndex == 6
					|| combatIndex == 7) {
				nekko.attack(2 * damageMultiplier, attackAOE, 150.0f);
				activateEffect();
			}
			currentTexture = flyingKick[combatIndex];
			animationDelay = 30000000L - speedUp;
			return true;
		case SUPERUPPERCUT:
			if (++combatIndex == superUppercut.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 0 || combatIndex == 2 || combatIndex == 5) {
				nekko.attack(4 * damageMultiplier, attackAOE, 40.0f);
				activateEffect();
			}
			currentTexture = superUppercut[combatIndex];
			animationDelay = 20000000L - speedUp;
			return true;
		case ONETWOCOMBO:
			if (++combatIndex == oneTwoCombo.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4 || combatIndex == 7) {
				nekko.attack(1 * damageMultiplier, attackAOE, 20.0f);
				activateEffect();
			}
			currentTexture = oneTwoCombo[combatIndex];
			animationDelay = 40000000L - speedUp;
			return true;
		case LOWMIDDLEKICK:
			if (++combatIndex == lowMiddleKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 2 || combatIndex == 8) {
				nekko.attack(1 * damageMultiplier, attackAOE, 40.0f);
				activateEffect();
			}
			currentTexture = lowMiddleKick[combatIndex];
			animationDelay = 45000000L - speedUp;
			return true;
		case HIGHKICK:
			if (++combatIndex == highKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 2) {
				nekko.attack(2 * damageMultiplier, attackAOE, 40.0f);
				activateEffect();
			}
			currentTexture = highKick[combatIndex];
			animationDelay = 60000000L - speedUp;
			return true;
		case DOWNWARDKICK:
			if (++combatIndex == downwardKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 7) {
				nekko.attack(2 * damageMultiplier, attackAOE, 20.0f);
				activateEffect();
			}
			currentTexture = downwardKick[combatIndex];
			animationDelay = 50000000L - speedUp;
			return true;
		case TWOSIDEDATTACK:
			if (++combatIndex == twoSidedAttack.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			stepForward(14.0f);
			if (combatIndex == 6) {
				nekko.attack(5 * damageMultiplier, attackAOE, 100.0f);
				activateEffect();
			}
			currentTexture = twoSidedAttack[combatIndex];
			animationDelay = 50000000L - speedUp;
			return true;
		case ROUNDKICK:
			if (++combatIndex == roundKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 5) {
				nekko.attack(3 * damageMultiplier, attackAOE, 100.0f);
				activateEffect();
			}
			currentTexture = roundKick[combatIndex];
			animationDelay = 50000000L - speedUp;
			return true;
		case UPPERCUT:
			if (++combatIndex == uppercut.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4) {
				nekko.attack(3 * damageMultiplier, attackAOE, 20.0f);
				activateEffect();
			}
			currentTexture = uppercut[combatIndex];
			animationDelay = 70000000L - speedUp;
			return true;

			// Projectile attacks
		case FASTSHOT:
			if (++combatIndex == fastShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(fastShotRegions, 4,
						nekko.location.x, nekko.location.y - 6.0f,
						(nekko.faceState == FaceState.LEFT) ? -20.0f : 20.0f,
						0, 10000000000L, 30000000L, 1 * damageMultiplier,
						20.0f, attackAOE);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punch(nekko.location.x);
				}
				activateEffect();
			}
			currentTexture = fastShot[combatIndex];
			animationDelay = 30000000L - speedUp;
			return true;
		case POWERSHOT:
			if (++combatIndex == powerShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 5) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(powerShotRegions, 4,
						nekko.location.x, nekko.location.y - 6.0f,
						(nekko.faceState == FaceState.LEFT) ? -15.0f : 15.0f,
						0, 10000000000L, 30000000L, 3 * damageMultiplier,
						100.0f, attackAOE);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punchSpecial(nekko.location.x, 0.75f);
				}
				activateEffect();
			}
			currentTexture = powerShot[combatIndex];
			animationDelay = 60000000L - speedUp;
			return true;
		case JUMPSHOT:
			if (++combatIndex == jumpShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 3) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(jumpShotRegions, 4,
						nekko.location.x, nekko.location.y,
						(nekko.faceState == FaceState.LEFT) ? -20.0f : 20.0f,
						-10.0f, 10000000000L, 30000000L, 1 * damageMultiplier,
						20.0f, attackAOE);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punch(nekko.location.x);
				}
				activateEffect();
			}
			currentTexture = jumpShot[combatIndex];
			animationDelay = 50000000L - speedUp;
			return true;
		case SUPERSHOT:
			if (++combatIndex == superShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 7) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(superShotRegions, 4,
						nekko.location.x, nekko.location.y,
						(nekko.faceState == FaceState.LEFT) ? -15.0f : 15.0f,
						0, 10000000000L, 30000000L, 5 * damageMultiplier,
						200.0f, attackAOE);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punchSpecial(nekko.location.x, 0.5f);
				}
				activateEffect();
			}
			currentTexture = superShot[combatIndex];
			animationDelay = 60000000L - speedUp;
			return true;

			// /// special moves
		case GATTLINGSHOT:
			if (++combatIndex == gattlingShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex > 2 && combatIndex < 7) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(fastShotRegions, 4,
						nekko.location.x, nekko.location.y - 6.0f,
						(nekko.faceState == FaceState.LEFT) ? -20.0f : 20.0f,
						0, 10000000000L, 30000000L, 1 * damageMultiplier,
						20.0f, attackAOE);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punchSpecial(nekko.location.x, 2.0f);
				}
				activateEffect();
			} else if (combatIndex == 8) {
				projectileIndex = Methods.incrementIndex(projectileIndex,
						projectiles.length);
				projectiles[projectileIndex].launch(superShotRegions, 4,
						nekko.location.x, nekko.location.y,
						(nekko.faceState == FaceState.LEFT) ? -15.0f : 15.0f,
						0, 10000000000L, 30000000L, 5 * damageMultiplier,
						200.0f, attackAOE);
				stepForward(-14.0f);
				stepForward(-14.0f);
				stepForward(-14.0f);
				if (TimeUtils.nanoTime() - lastCombatSound > 100000000L) {
					lastCombatSound = TimeUtils.nanoTime();
					KFNekko.audio.punchSpecial(nekko.location.x, 0.5f);
				}
				activateEffect();
			}
			currentTexture = gattlingShot[combatIndex];
			animationDelay = 100000000L - speedUp;
			return true;
		case BEATDOWN:
			if (++combatIndex == beatdown.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			currentTexture = beatdown[combatIndex];
			break;
		case LIGHTNINGKICKS:
			if (++combatIndex == lightningKicks.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			currentTexture = lightningKicks[combatIndex];
			break;
		case SPEEDYHANDS:
			if (++combatIndex == speedyHands.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			currentTexture = speedyHands[combatIndex];
			break;
		}

		switch (nekko.combatState) {
		case BEATDOWN:
		case LIGHTNINGKICKS:
		case SPEEDYHANDS:
			if (combatIndex > 2) {
				nekko.attack(3 * damageMultiplier, attackAOE, 100.0f);
				activateEffect();
				stepForward(12.0f);
				stepForward(12.0f);
			}
			animationDelay = 100000000L - speedUp;
			if (cameraEffect)
				switch (nekko.powerUp) {
				case PowerUp.RAGE:
					KFNekko.worldColor.set(Color.BLUE);
					break;
				case PowerUp.QUICKFEET:
					KFNekko.worldColor.set(Color.RED);
					break;
				case PowerUp.KUNGFU_MASTER:
					KFNekko.worldColor.set(Color.WHITE);
					break;
				case PowerUp.ENDURANCE:
					KFNekko.worldColor.set(Color.GREEN);
					break;
				default: // None & INVISIBILITY
					KFNekko.worldColor.set(Color.BLACK);
				}

			return true;
		}
		return false;
	}

	public void activateSmoke() {
		effectImgIndex = Methods.incrementIndex(effectImgIndex,
				effectImages.length);
		effectImages[effectImgIndex].activate(smokeEffect, nekko.location.x,
				nekko.location.rect.y, 0.08f, -12.0f, true, 2.0f, Color.WHITE);
		effectImgIndex = Methods.incrementIndex(effectImgIndex,
				effectImages.length);
		effectImages[effectImgIndex].activate(smokeEffect, nekko.location.x,
				nekko.location.rect.y, 0.08f, 12.0f, false, 2.0f, Color.WHITE);
	}

}
