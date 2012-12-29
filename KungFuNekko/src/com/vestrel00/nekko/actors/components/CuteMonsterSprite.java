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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.CuteMonster;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class CuteMonsterSprite extends Sprite {

	// animation frames
	private AtlasRegion[] idle, move, attack, hurt, jump, dying;

	public int idleIndex = 0, walkIndex = 0, jumpIndex = 0, hurtIndex = 0,
			dyingIndex = 0;

	private CuteMonster monster;

	public CuteMonsterSprite(CuteMonster monster, TextureAtlas atlas,
			Color color) {
		super(monster, color);
		this.monster = monster;
		initTextures(atlas);
	}

	private void initTextures(TextureAtlas atlas) {
		hurt = new AtlasRegion[2];
		idle = new AtlasRegion[4];
		move = new AtlasRegion[5];
		attack = new AtlasRegion[6];
		dying = new AtlasRegion[7];
		jump = new AtlasRegion[8];

		int i = 0;
		for (i = 0; i < idle.length; i++)
			idle[i] = atlas.findRegion("cuteMonsterIdle" + String.valueOf(i));
		for (i = 0; i < move.length; i++)
			move[i] = atlas.findRegion("cuteMonsterMove" + String.valueOf(i));
		for (i = 0; i < attack.length; i++)
			attack[i] = atlas.findRegion("cuteMonsterAttack"
					+ String.valueOf(i));
		for (i = 0; i < dying.length; i++)
			dying[i] = atlas.findRegion("cuteMonsterHurt" + String.valueOf(i));
		for (i = 0; i < jump.length; i++)
			jump[i] = atlas.findRegion("cuteMonsterJump" + String.valueOf(i));
		hurt[0] = dying[1];
		hurt[1] = dying[2];

		currentTexture = idle[0];
	}

	@Override
	public void update() {
		if (TimeUtils.nanoTime() - lastAnimationTime < animationDelay)
			return;
		lastAnimationTime = TimeUtils.nanoTime();

		xScale = (monster.faceState == FaceState.LEFT) ? -1.0f : 1.0f;

		if (switchStatusState())
			return;

		// animate
		if (monster.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK
				&& switchCombatState())
			return;
		if (monster.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK
				&& switchVerticalMotionState())
			return;

		switchHorizontalMotionState();
	}

	private boolean switchStatusState() {
		switch (monster.statusState) {
		case DYING:
			if (++dyingIndex == dying.length) {
				dyingIndex = 0;
				monster.statusState = StatusState.DEAD;
			}
			currentTexture = dying[dyingIndex];
			animationDelay = 60000000L;
			return true;
		default:
			return false;
		}
	}

	private boolean switchCombatState() {
		switch (monster.combatState) {
		case ATTACK:
			if (++combatIndex == attack.length) {
				combatIndex = 0;
				monster.onDeactivateCombat();
			}
			if (combatIndex == 3)
				monster.attack(monster.damage, true, monster.knockBackDistance);

			currentTexture = attack[combatIndex];
			animationDelay = 65000000L;
			return true;
		default:
			return false;
		}
	}

	private boolean switchVerticalMotionState() {
		switch (monster.verticalMotionState) {
		case JUMPING:
			if (++jumpIndex >= jump.length)
				return false;
			currentTexture = jump[jumpIndex];
			animationDelay = 100000000L;
			return true;
		default:
			return false;
		}
	}

	private boolean switchHorizontalMotionState() {
		if (monster.location.onPlatform || monster.location.onSlope)
			switch (monster.horizontalMotionState) {
			case IDLE:
				if (++idleIndex == idle.length)
					idleIndex = 0;
				currentTexture = idle[idleIndex];
				animationDelay = 100000000L;
				return true;
			case KNOCKED_BACK:
				if (++hurtIndex == hurt.length)
					hurtIndex = 0;
				currentTexture = hurt[hurtIndex];
				animationDelay = 100000000L;
				return true;
			case MOVING:
				if (++walkIndex == move.length)
					walkIndex = 0;

				if (walkIndex == 1 && monster.visibility == Visibility.VISIBLE
						&& TimeUtils.nanoTime() - lastStepTime > 300000000L) {
					lastStepTime = TimeUtils.nanoTime();
					KFNekko.audio.footStep(monster.location.x);
				}

				currentTexture = move[walkIndex];
				if (monster.location.onSlope)
					animationDelay = 100000000L;
				else
					animationDelay = 50000000L;
				return true;
			default:
				return false;
			}
		else
			return false;

	}

}
