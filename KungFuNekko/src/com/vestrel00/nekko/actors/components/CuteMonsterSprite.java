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

		hurt[0] = atlas.findRegion("cuteMonsterHurt1");
		hurt[1] = atlas.findRegion("cuteMonsterHurt2");
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
				monster.attack(monster.damage, false,
						monster.knockBackDistance, monster);

			currentTexture = attack[combatIndex];
			animationDelay = 80000000L;
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

				if (walkIndex == 1 && monster.visibility == Visibility.VISIBLE)
					KFNekko.audio.footStep(monster.location.x);

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

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

}
