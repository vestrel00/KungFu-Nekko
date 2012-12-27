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

package com.vestrel00.nekko.ui.components;

import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.interf.CombatStateManager;

public class SimpleAttackManager implements CombatStateManager {

	private CombatState[] comboDown, comboUp, comboRight, comboLeft;
	private int inputIndex;
	private long lastInputTime;
	private Actor actor;

	public SimpleAttackManager(Actor actor) {
		this.actor = actor;
		inputIndex = -1;

		comboDown = new CombatState[4];
		comboUp = new CombatState[4];
		comboLeft = new CombatState[4];
		comboRight = new CombatState[4];

		comboDown[0] = CombatState.FASTSHOT;
		comboDown[1] = CombatState.ONETWOCOMBO;
		comboDown[2] = CombatState.UPPERCUT;
		comboDown[3] = CombatState.SUPERUPPERCUT;

		comboUp[0] = CombatState.LOWMIDDLEKICK;
		comboUp[1] = CombatState.HIGHKICK;
		comboUp[2] = CombatState.DOWNWARDKICK;
		comboUp[3] = CombatState.ROUNDKICK;

		// TODO INPUT_LEFT MAKE INTO PROJECTILE ATTACKS
		comboLeft[0] = CombatState.FASTSHOT;
		comboLeft[1] = CombatState.ONETWOCOMBO;
		comboLeft[2] = CombatState.ROUNDKICK;
		comboLeft[3] = CombatState.TWOSIDEDATTACK;
		// //////////

		comboRight[0] = CombatState.FLYINGKICK;
		comboRight[1] = CombatState.SPIN;
		comboRight[2] = CombatState.HIGHKICK;
		comboRight[3] = CombatState.TWOSIDEDATTACK;
	}

	/**
	 * <p>
	 * Only expects input left, right, up, down.
	 * </p>
	 * <ol>
	 * <b>INPUT_DOWN</b>
	 * <li>FASTSHOT</li>
	 * <li>ONETWOCOMBO</li>
	 * <li>UPPERCUT</li>
	 * <li>SUPERUPPERCUT</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_UP</b>
	 * <li>LOWMIDDLEKICK</li>
	 * <li>HIGHKICK</li>
	 * <li>DOWNWARDKICK</li>
	 * <li>ROUNDKICK</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_LEFT</b>
	 * <li>FASTSHOT</li>
	 * <li>ONETWOCOMBO</li>
	 * <li>ROUNDKICK</li>
	 * <li>TWOSIDEDATTACK</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_RIGHT</b>
	 * <li>FLYINGKICK</li>
	 * <li>SPIN</li>
	 * <li>HIGHKICK</li>
	 * <li>TWOSIDEDATTACK</li>
	 * </ol>
	 */
	@Override
	public CombatState input(int input) {
		if (TimeUtils.nanoTime() - lastInputTime > CombatStateManager.RESET_DELAY)
			inputIndex = -1;

		lastInputTime = TimeUtils.nanoTime();

		if (actor.combatState != CombatState.IDLE)
			return actor.combatState;

		switch (input) {
		case INPUT_LEFT:
			if (++inputIndex >= comboLeft.length)
				inputIndex = 0;
			return comboLeft[inputIndex];
		case INPUT_RIGHT:
			if (++inputIndex >= comboRight.length)
				inputIndex = 0;
			return comboRight[inputIndex];
		case INPUT_UP:
			if (++inputIndex >= comboUp.length)
				inputIndex = 0;
			return comboUp[inputIndex];
		case INPUT_DOWN:
			if (++inputIndex >= comboDown.length)
				inputIndex = 0;
			return comboDown[inputIndex];
		}
		return CombatState.IDLE;
	}
}
