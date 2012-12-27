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
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.components.effects.AfterImage;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class NekkoSprite extends Sprite {

	// animation frames
	private AtlasRegion[] idle, walk, jump, dying, hurt, doubleJump, spinKick,
			powerShot, fastShot, flyingKick, superUppercut, oneTwoCombo,
			lowMiddleKick, highKick, downwardKick, twoSidedAttack, roundKick,
			uppercut;

	public int idleIndex = 0, walkIndex = 0, jumpIndex = 0, hurtIndex = 0,
			doubleJumpIndex = 0, dyingIndex;

	// afterImages
	private AfterImage[] combatImages, effectImages;
	private int combatImgIndex = 0, effectImgIndex = 0;
	private AtlasRegion attackEffect1, smokeEffect;

	private Nekko nekko;
	public long speedUp, lastCombatImage;
	public int damageMultiplier;

	public NekkoSprite(Nekko nekko, TextureAtlas atlas, Color color) {
		super(nekko, color);
		this.nekko = nekko;
		initTextures(atlas);
		initEffects(atlas);
		speedUp = 0L;
		damageMultiplier = 1;
	}

	private void initEffects(TextureAtlas atlas) {
		combatImages = new AfterImage[6];
		effectImages = new AfterImage[10];
		for (int i = 0; i < combatImages.length; i++)
			combatImages[i] = new AfterImage();
		for (int i = 0; i < effectImages.length; i++)
			effectImages[i] = new AfterImage();
		attackEffect1 = atlas.findRegion("attackEffect1");
		smokeEffect = atlas.findRegion("smokeEffect");
	}

	private void initTextures(TextureAtlas atlas) {
		hurt = new AtlasRegion[2];
		idle = new AtlasRegion[4];
		doubleJump = new AtlasRegion[4];
		highKick = new AtlasRegion[6];
		fastShot = new AtlasRegion[6];
		powerShot = new AtlasRegion[7];
		dying = new AtlasRegion[7];
		uppercut = new AtlasRegion[7]; // added 1 frame
		flyingKick = new AtlasRegion[8];
		walk = new AtlasRegion[8];
		jump = new AtlasRegion[8];
		downwardKick = new AtlasRegion[9]; // added 1 frame
		twoSidedAttack = new AtlasRegion[9]; // added 1 frame
		roundKick = new AtlasRegion[9]; // added 1 frame
		spinKick = new AtlasRegion[10];
		oneTwoCombo = new AtlasRegion[10];
		lowMiddleKick = new AtlasRegion[12];
		superUppercut = new AtlasRegion[13];

		hurt[0] = atlas.findRegion("nekkoHurt1");
		hurt[1] = atlas.findRegion("nekkoHurt2");
		int i = 0;
		for (i = 0; i < 4; i++) {
			idle[i] = atlas.findRegion("nekkoIdle" + String.valueOf(i));
			doubleJump[i] = atlas.findRegion("nekkoDoubleJump"
					+ String.valueOf(i));
		}
		for (i = 0; i < 6; i++) {
			highKick[i] = atlas.findRegion("nekkoHighKick" + String.valueOf(i));
			fastShot[i] = atlas.findRegion("nekkoFastShot" + String.valueOf(i));
		}
		for (i = 0; i < 7; i++) {
			powerShot[i] = atlas.findRegion("nekkoPowerShot"
					+ String.valueOf(i));
			uppercut[i] = atlas.findRegion("nekkoUppercut" + String.valueOf(i));
			dying[i] = atlas.findRegion("nekkoHurt" + String.valueOf(i));
		}
		for (i = 0; i < 8; i++) {
			flyingKick[i] = atlas.findRegion("nekkoFlyingKick"
					+ String.valueOf(i));
			walk[i] = atlas.findRegion("nekkoWalk" + String.valueOf(i));
			jump[i] = atlas.findRegion("nekkoJump" + String.valueOf(i));
		}
		for (i = 0; i < 9; i++) {
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
		for (i = 0; i < 13; i++)
			superUppercut[i] = atlas.findRegion("nekkoSuperUppercut"
					+ String.valueOf(i));

		currentTexture = idle[0];
	}

	@Override
	public void update() {
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

		// animate
		if (switchCombatState())
			return;
		if (nekko.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK
				&& switchVerticalMotionState())
			return;

		switchHorizontalMotionState();
	}

	private boolean switchStatusState() {
		switch (nekko.statusState) {
		case DYING:
			if (++dyingIndex == dying.length) {
				dyingIndex = 0;
				nekko.statusState = StatusState.DEAD;
			}
			currentTexture = dying[dyingIndex];
			animationDelay = 80000000L;
			return true;
		default:
			return false;
		}
	}

	private int incrementIndex(int index, int maxIndex) {
		if (++index == maxIndex)
			return 0;
		else
			return index;
	}

	private void stepForward(float amount) {
		// step forward
		nekko.location.speed.xSpeed = amount;
		nekko.location.speed.xDirection = (nekko.faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
				: Speed.DIRECTION_RIGHT;
		nekko.location.update();
		nekko.location.speed.xSpeed = nekko.location.speed.maxXSpeed;

		if (nekko.location.onSlope || nekko.location.onPlatform)
			KFNekko.audio.footStep(nekko.location.x);
	}

	private void activateEffect() {
		KFNekko.bumpWC(0.1f, 0.1f, 0.1f);
		float zoomAmount = 0.0f;
		stepForward(12.0f);
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
				KFNekko.camera.setEffect(Camera.EFFECT_SHAKE, 1.0f, 3.0f,
						200000000L);
				activateSmoke();
				KFNekko.audio.groundBoom(nekko.location.x);
			}
			break;
		case FASTSHOT:
		case ONETWOCOMBO:
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
		case POWERSHOT:
			zoomAmount = 0.07f;
			break;
		case FLYINGKICK:
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
			effectImages[effectImgIndex].activate(
					attackEffect1, // left
					nekko.location.x - 22.0f, nekko.location.y - 7.0f, 0.08f,
					-12.0f, false, 2.0f, color);
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
			effectImages[effectImgIndex].activate(
					attackEffect1,// right
					nekko.location.x + 22.0f, nekko.location.y - 7.0f, 0.08f,
					12.0f, true, 2.0f, color);

			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
			effectImages[effectImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, 0.08f, -12.0f, true,
					2.0f, color);
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
			effectImages[effectImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, 0.08f, 12.0f, false,
					2.0f, color);
			zoomAmount = 0.08f;
			break;
		case ROUNDKICK:
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
			effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
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
		}
		KFNekko.camera.setEffect(Camera.EFFECT_ZOOM, zoomAmount, 0.0063f, 0L);
	}

	private void switchCombatImages() {
		if (nekko.combatState != CombatState.IDLE
				&& TimeUtils.nanoTime() - lastCombatImage > 60000000L) {
			lastCombatImage = TimeUtils.nanoTime();
			combatImgIndex = incrementIndex(combatImgIndex, combatImages.length);
			combatImages[combatImgIndex].activate(currentTexture,
					nekko.location.x, nekko.location.y, 0.04f, 0.0f,
					(nekko.faceState == FaceState.LEFT) ? true : false, 0.0f,
					color);
		}
		switch (nekko.combatState) {
		case SPIN:
			break;
		case SUPERUPPERCUT:
			break;
		case FLYINGKICK:
			break;
		case ONETWOCOMBO:
			break;
		case FASTSHOT:

			break;
		case POWERSHOT:

			break;
		case LOWMIDDLEKICK:

			break;
		case HIGHKICK:

			break;
		case DOWNWARDKICK:

			break;
		case TWOSIDEDATTACK:

			break;
		case ROUNDKICK:

			break;
		case UPPERCUT:

			break;
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
			batch.setColor(KFNekko.worldColor);
		}
	}

	private boolean switchHorizontalMotionState() {
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
					KFNekko.audio.meow(nekko.location.x);
					hurtIndex = 0;
				}
				currentTexture = hurt[hurtIndex];
				animationDelay = 100000000L;
				return true;
			case MOVING:
				if (++walkIndex == walk.length)
					walkIndex = 0;

				// footstep
				if (walkIndex == 0 || walkIndex == 4)
					KFNekko.audio.footStep(nekko.location.x);

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

	private boolean switchVerticalMotionState() {
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

	private boolean switchCombatState() {
		switch (nekko.combatState) {
		case SPIN:
			if (++combatIndex == spinKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 7 || combatIndex == spinKick.length - 1) {
				nekko.attack(2 * damageMultiplier, true, 10.0f, nekko);
				activateEffect();
			}
			currentTexture = spinKick[combatIndex];
			animationDelay = 40000000L;
			return true;
		case FASTSHOT:
			if (++combatIndex == fastShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4) {
				nekko.attack(1 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = fastShot[combatIndex];
			animationDelay = 30000000 - speedUp;
			return true;
		case POWERSHOT:
			if (++combatIndex == powerShot.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 5) {
				// nekko.attack(3 * damageMultiplier, false);
				// TODO launch projectile
				activateEffect();
			}
			currentTexture = powerShot[combatIndex];
			animationDelay = 60000000 - speedUp;
			return true;
		case FLYINGKICK:
			if (++combatIndex == flyingKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 0 || combatIndex == 3 || combatIndex == 6) {
				nekko.attack(2 * damageMultiplier, true, 150.0f, nekko);
				activateEffect();
			}
			currentTexture = flyingKick[combatIndex];
			animationDelay = 30000000 - speedUp;
			return true;
		case SUPERUPPERCUT:
			if (++combatIndex == superUppercut.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 0 || combatIndex == 2 || combatIndex == 5) {
				nekko.attack(4 * damageMultiplier, true, 40.0f, nekko);
				activateEffect();
			}
			currentTexture = superUppercut[combatIndex];
			animationDelay = 20000000 - speedUp;
			return true;
		case ONETWOCOMBO:
			if (++combatIndex == oneTwoCombo.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4 || combatIndex == 7) {
				nekko.attack(1 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = oneTwoCombo[combatIndex];
			animationDelay = 40000000 - speedUp;
			return true;
		case LOWMIDDLEKICK:
			if (++combatIndex == lowMiddleKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 2 || combatIndex == 8) {
				nekko.attack(1 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = lowMiddleKick[combatIndex];
			animationDelay = 45000000 - speedUp;
			return true;
		case HIGHKICK:
			if (++combatIndex == highKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 2) {
				nekko.attack(2 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = highKick[combatIndex];
			animationDelay = 60000000 - speedUp;
			return true;
		case DOWNWARDKICK:
			if (++combatIndex == downwardKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 7) {
				nekko.attack(2 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = downwardKick[combatIndex];
			animationDelay = 50000000 - speedUp;
			return true;
		case TWOSIDEDATTACK:
			if (++combatIndex == twoSidedAttack.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			stepForward(16.0f);
			if (combatIndex == 6) {
				nekko.attack(5 * damageMultiplier, true, 100.0f, nekko);
				activateEffect();
			}
			currentTexture = twoSidedAttack[combatIndex];
			animationDelay = 50000000 - speedUp;
			return true;
		case ROUNDKICK:
			if (++combatIndex == roundKick.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 5) {
				nekko.attack(3 * damageMultiplier, true, 100.0f, nekko);
				activateEffect();
			}
			currentTexture = roundKick[combatIndex];
			animationDelay = 50000000 - speedUp;
			return true;
		case UPPERCUT:
			if (++combatIndex == uppercut.length) {
				combatIndex = 0;
				nekko.onDeactivateCombat();
			}
			if (combatIndex == 4) {
				nekko.attack(3 * damageMultiplier, true, 20.0f, nekko);
				activateEffect();
			}
			currentTexture = uppercut[combatIndex];
			animationDelay = 70000000 - speedUp;
			return true;
		default:
			return false;
		}
	}

	public void activateSmoke() {
		effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
		effectImages[effectImgIndex].activate(smokeEffect, nekko.location.x,
				nekko.location.rect.y, 0.08f, -12.0f, true, 2.0f, Color.WHITE);
		effectImgIndex = incrementIndex(effectImgIndex, effectImages.length);
		effectImages[effectImgIndex].activate(smokeEffect, nekko.location.x,
				nekko.location.rect.y, 0.08f, 12.0f, false, 2.0f, Color.WHITE);
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

}
