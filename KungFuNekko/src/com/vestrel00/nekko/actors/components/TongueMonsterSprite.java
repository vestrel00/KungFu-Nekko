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
import com.vestrel00.nekko.actors.TongueMonster;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class TongueMonsterSprite extends Sprite {

	// animation frames
	private AtlasRegion[] move, attack, hurt, dying;

	public int moveIndex = 0, hurtIndex = 0, dyingIndex = 0;

	private TongueMonster monster;

	public TongueMonsterSprite(TongueMonster monster, TextureAtlas atlas,
			Color color, boolean attackAOE) {
		super(monster, color);
		this.monster = monster;
		this.attackAOE = attackAOE;
		initTextures(atlas);
	}

	private void initTextures(TextureAtlas atlas) {
		hurt = new AtlasRegion[2];
		move = new AtlasRegion[5];
		attack = new AtlasRegion[5];
		dying = new AtlasRegion[8];

		int i = 0;
		for (i = 0; i < move.length; i++)
			move[i] = atlas.findRegion("toungeMonsterMove" + String.valueOf(i));
		for (i = 0; i < dying.length; i++)
			dying[i] = atlas
					.findRegion("toungeMonsterHurt" + String.valueOf(i));
		for (i = 0; i < attack.length; i++)
			attack[i] = atlas.findRegion("toungeMonsterAttack"
					+ String.valueOf(i));

		hurt[0] = dying[1];
		hurt[1] = dying[2];

		currentTexture = move[0];
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

		switchHorizontalMotionState();
	}

	private boolean switchHorizontalMotionState() {
		switch (monster.horizontalMotionState) {
		case IDLE:
		case MOVING:
			if (++moveIndex == move.length)
				moveIndex = 0;
			currentTexture = move[moveIndex];
			animationDelay = (monster.horizontalMotionState == HorizontalMotionState.IDLE) ? 100000000L
					: 50000000L;
			if (moveIndex == 2 && monster.visibility == Visibility.VISIBLE
					&& TimeUtils.nanoTime() - lastStepTime > STEP_DELAY) {
				lastStepTime = TimeUtils.nanoTime();
				KFNekko.audio.wingFlap(monster.location.x);
			}
			return true;
		case KNOCKED_BACK:
			if (++hurtIndex == hurt.length)
				hurtIndex = 0;
			currentTexture = hurt[hurtIndex];
			animationDelay = 100000000L;
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
			if (combatIndex == 2)
				monster.attack(monster.damage, attackAOE, monster.knockBackDistance);

			currentTexture = attack[combatIndex];
			animationDelay = 45000000L;
			return true;
		default:
			return false;
		}
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

}
