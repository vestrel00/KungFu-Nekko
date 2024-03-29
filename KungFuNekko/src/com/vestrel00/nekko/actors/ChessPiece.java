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

package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.ChessPieceSprite;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;

public class ChessPiece extends Actor {

	public static final int PAWN = 0, KNIGHT = 1, BISHOP = 2, ROOK = 3,
			QUEEN = 4, KING = 5;
	public static final long DEFAULT_VIBRATE_DURATION = 300000000L;

	public ChessPieceSprite chessSprite;
	public long lastAttackTime, vibrationDuration;
	private long attackDelay;
	private int damage;
	private float knockBackDistance, vibrateDirection = 1.0f;
	public float vibrationSpeed = 10.0f, vibrationDist = 5.0f;
	private boolean aoe;
	public boolean hit = false, forceVibrate = false, attackAnytime = false;

	public ChessPiece(Array<Actor> targets, int type, Location location,
			FaceState face, int maxHealth) {
		super(targets, location, 0);
		vibrationDuration = DEFAULT_VIBRATE_DURATION;
		AtlasRegion region = null;
		switch (type) {
		case PAWN:
			region = KFNekko.resource.atlas.findRegion("pawn");
			attackDelay = 1000000000L;
			damage = 1;
			knockBackDistance = 80.0f;
			aoe = false;
			// maxHealth = 300;
			this.maxHealth = maxHealth;
			break;
		case KNIGHT:
			region = KFNekko.resource.atlas.findRegion("knight");
			attackDelay = 200000000L;
			damage = 2;
			knockBackDistance = 140.0f;
			aoe = false;
			// maxHealth = 600;
			this.maxHealth = maxHealth;
			break;
		case BISHOP:
			region = KFNekko.resource.atlas.findRegion("bishop");
			attackDelay = 500000000L;
			damage = 2;
			knockBackDistance = 100.0f;
			aoe = true;
			// maxHealth = 400;
			this.maxHealth = maxHealth;
			break;
		case ROOK:
			region = KFNekko.resource.atlas.findRegion("rook");
			attackDelay = 2000000000L;
			damage = 5;
			knockBackDistance = 150.0f;
			aoe = true;
			// maxHealth = 1000;
			this.maxHealth = maxHealth;
			break;
		case QUEEN:
			region = KFNekko.resource.atlas.findRegion("queen");
			attackDelay = 500000000L;
			damage = 4;
			knockBackDistance = 240.0f;
			aoe = true;
			// maxHealth = 3000;
			this.maxHealth = maxHealth;
			break;
		case KING:
			region = KFNekko.resource.atlas.findRegion("king");
			attackDelay = 1000000000L;
			damage = 6;
			knockBackDistance = 260.0f;
			aoe = true;
			// maxHealth = 5000;
			this.maxHealth = maxHealth;
			break;
		}
		health = maxHealth;
		chessSprite = new ChessPieceSprite(this, new Color(Color.WHITE), region);
		sprite = chessSprite;
		location.setActor(this);
		// we are passing in the bottom (feet) y value not the center so...
		location.y += (float) sprite.currentTexture.originalHeight * 0.5f;
		location.updateRect();
		setState(face, StatusState.ALIVE, CombatState.IDLE,
				HorizontalMotionState.IDLE, VerticalMotionState.FALLING);
		// will not change face
		chessSprite.xScale = (faceState == FaceState.LEFT) ? -1.0f : 1.0f;
	}

	@Override
	public void onDeath() {
		chessSprite.targetColor.a = 0.0f;
		super.onDeath();
	}

	public void resetVibrationState() {
		hit = false;
		vibrationDuration = ChessPiece.DEFAULT_VIBRATE_DURATION;
		vibrationSpeed = 10.0f;
		vibrationDist = 5.0f;
		forceVibrate = false;
	}

	@Override
	public boolean jump() {
		return false;
	}

	@Override
	public void update() {
		if (statusState != StatusState.DEAD) {
			// no ai like monster - just keep attacking
			// cannot attack if not visible!
			if (!forceVibrate)
				if ((visibility == Visibility.VISIBLE || attackAnytime)
						&& TimeUtils.nanoTime() - lastAttackTime > attackDelay) {
					lastAttackTime = TimeUtils.nanoTime();
					attack(damage, aoe, knockBackDistance);
				}

			// imitate vibration
			if (hit
					&& TimeUtils.nanoTime() - lastAttackTime < vibrationDuration) {
				location.x += vibrationSpeed * vibrateDirection;
				if (location.x < location.spawnX - vibrationDist
						|| location.x > location.spawnX + vibrationDist)
					vibrateDirection *= -1.0f;
			} else {
				// revert to original position
				location.x = location.spawnX;
				hit = false;
			}

			// update rectangle
			location.updateRect();

			// set visibility ourself since location is not being updated
			visibility = (KFNekko.camera.rect.overlaps(location.rect)) ? Visibility.VISIBLE
					: Visibility.NOT_VISIBLE;

			sprite.update();
		}
	}

	@Override
	public void receiveDamage(int damage) {
		super.receiveDamage(damage);
		// 75% because start at 100% color and end at 25% (dark_gray)
		float c = ((float) damage / (float) maxHealth) * 0.75f;

		if ((chessSprite.targetColor.r -= c) < 0.25f)
			chessSprite.targetColor.r = 0.25f;
		if ((chessSprite.targetColor.g -= c) < 0.25f)
			chessSprite.targetColor.g = 0.25f;
		if ((chessSprite.targetColor.b -= c) < 0.25f)
			chessSprite.targetColor.b = 0.25f;
		// TODO SOUND
		chessSprite.color.set(Color.RED);
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		hit = false;
		for (int i = 0; i < targets.size; i++)
			if (targets.get(i).statusState == StatusState.ALIVE
					&& location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				targets.get(i).location.knockBack(knockBackDistance,
						(faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				hit = true;
				if (!aoe)
					break;
			}
		// TODO SOUND
		if (hit && visibility == Visibility.VISIBLE)
			KFNekko.camera.setEffect(Camera.EFFECT_SHAKE, -1.0f, 3.0f,
					200000000L);
	}

}
