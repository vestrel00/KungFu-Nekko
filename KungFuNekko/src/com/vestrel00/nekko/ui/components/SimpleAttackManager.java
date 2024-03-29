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

		comboDown = new CombatState[5];
		comboUp = new CombatState[5];
		comboLeft = new CombatState[5];
		comboRight = new CombatState[5];

		comboDown[0] = CombatState.JAB;
		comboDown[1] = CombatState.ONETWOCOMBO;
		comboDown[2] = CombatState.UPPERCUT;
		comboDown[3] = CombatState.SUPERUPPERCUT;
		comboDown[4] = CombatState.SPEEDYHANDS;

		comboUp[0] = CombatState.LOWMIDDLEKICK;
		comboUp[1] = CombatState.HIGHKICK;
		comboUp[2] = CombatState.DOWNWARDKICK;
		comboUp[3] = CombatState.ROUNDKICK;
		comboUp[4] = CombatState.LIGHTNINGKICKS;
		// Projectile attacks
		comboLeft[0] = CombatState.FASTSHOT;
		comboLeft[1] = CombatState.POWERSHOT;
		comboLeft[2] = CombatState.JUMPSHOT;
		comboLeft[3] = CombatState.SUPERSHOT;
		comboLeft[4] = CombatState.GATTLINGSHOT;

		comboRight[0] = CombatState.FLYINGKICK;
		comboRight[1] = CombatState.SPIN;
		comboRight[2] = CombatState.HIGHKICK;
		comboRight[3] = CombatState.TWOSIDEDATTACK;
		comboRight[4] = CombatState.BEATDOWN;
	}

	/**
	 * <p>
	 * Only expects input left, right, up, down.
	 * </p>
	 * <ol>
	 * <b>INPUT_DOWN</b>
	 * <li>JAB</li>
	 * <li>ONETWOCOMBO</li>
	 * <li>UPPERCUT</li>
	 * <li>SUPERUPPERCUT</li>
	 * <li>SPEEDYHANDS</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_UP</b>
	 * <li>LOWMIDDLEKICK</li>
	 * <li>HIGHKICK</li>
	 * <li>DOWNWARDKICK</li>
	 * <li>ROUNDKICK</li>
	 * <li>LIGHTNINGKICKS</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_LEFT</b>
	 * <li>JAB</li>
	 * <li>ONETWOCOMBO</li>
	 * <li>ROUNDKICK</li>
	 * <li>TWOSIDEDATTACK</li>
	 * <li>GATTLINGSHOT</li>
	 * </ol>
	 * <ol>
	 * <b>INPUT_RIGHT</b>
	 * <li>FLYINGKICK</li>
	 * <li>SPIN</li>
	 * <li>HIGHKICK</li>
	 * <li>TWOSIDEDATTACK</li>
	 * <li>BEATDOWN</li>
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
