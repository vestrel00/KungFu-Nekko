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

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.interf.CombatStateManager;

/**
 * <p>
 * Detects combos based on collected user input.
 * </p>
 * 
 * <p>
 * <b>Nekko Combo sheet</b>
 * </p>
 * 
 * <ul>
 * <b> Spin</b>
 * <li>down + jump + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Power Shot</b>
 * <li>down + right + right + attack or</li>
 * <li>down + left + left + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Flying Kick</b>
 * <li>down + right + jump + attack or</li>
 * <li>down + left + jump + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Super Uppercut</b>
 * <li>down + down + up + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> One Two Combo</b>
 * <li>down + right + attack or</li>
 * <li>down + left + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Low-Middle Kick</b>
 * <li>down + down + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> High Kick</b>
 * <li>down + up + up + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Downward Kick</b>
 * <li>down + attack</li>
 * </ul>
 * 
 * <ul>
 * <b> Two-Sided Attack</b>
 * <li>down + up + left + right + attack or</li>
 * <li>down + up + right + left + attack</li>
 * </ul>
 * 
 * <ul>
 * <b>Round Kick</b>
 * <li>down + left + right + attack or</li>
 * <li>down + right + left + attack</li>
 * </ul>
 * 
 * <ul>
 * <b>Uppercut</b>
 * <li>down + up + attack</li>
 * </ul>
 * 
 * @author Estrellado, Vandolf
 * 
 */
public class ComboAttackManager implements CombatStateManager {

	private Array<Integer> history;
	private long lastInputTime;

	public ComboAttackManager() {
		history = new Array<Integer>();
	}

	/**
	 * This will determine the combatState that it will return based on the
	 * input concatenated with the history.
	 */
	@Override
	public CombatState input(int input) {
		// reset history if input past the recording time
		if (TimeUtils.nanoTime() - lastInputTime > RESET_DELAY
				&& history.size > 0)
			history.clear();

		// wait for INPUT_DOWN to start recording history
		if (history.size == 0 && input != INPUT_DOWN)
			// regular attack
			return (input == INPUT_ATTACK) ? CombatState.FASTSHOT
					: CombatState.IDLE;

		lastInputTime = TimeUtils.nanoTime();
		history.add(input);
		if (input == INPUT_ATTACK) {
			CombatState state = recognize();
			history.clear();
			return state;
		} else
			return CombatState.IDLE;

	}

	private CombatState recognize() {
		switch (history.size) {
		case 2:
			return CombatState.DOWNWARDKICK;
		case 3:
			return recognize3();
		case 4:
			return recognize4();
		case 5:
			if (history.get(1) == INPUT_UP
					&& ((history.get(3) == INPUT_RIGHT && history.get(2) == INPUT_LEFT) || (history
							.get(3) == INPUT_LEFT && history.get(2) == INPUT_RIGHT)))
				return CombatState.TWOSIDEDATTACK;
			else
				return CombatState.FASTSHOT;
		default:
			return CombatState.FASTSHOT;
		}
	}

	/**
	 * Receiving down + ? + ? + attack
	 */
	private CombatState recognize4() {
		switch (history.get(2)) {
		case INPUT_RIGHT:
			switch (history.get(1)) {
			case INPUT_RIGHT:
				return CombatState.POWERSHOT;
			case INPUT_LEFT:
				return CombatState.ROUNDKICK;
			default:
				return CombatState.FASTSHOT;
			}
		case INPUT_LEFT:
			switch (history.get(1)) {
			case INPUT_LEFT:
				return CombatState.POWERSHOT;
			case INPUT_RIGHT:
				return CombatState.ROUNDKICK;
			default:
				return CombatState.FASTSHOT;
			}
		case INPUT_JUMP:
			switch (history.get(1)) {
			case INPUT_RIGHT:
			case INPUT_LEFT:
				return CombatState.FLYINGKICK;
			default:
				return CombatState.FASTSHOT;
			}
		case INPUT_UP:
			switch (history.get(1)) {
			case INPUT_UP:
				return CombatState.HIGHKICK;
			case INPUT_DOWN:
				return CombatState.SUPERUPPERCUT;
			default:
				return CombatState.FASTSHOT;
			}
		default:
			return CombatState.FASTSHOT;
		}
	}

	/**
	 * Receiving down + ? + attack
	 */
	private CombatState recognize3() {
		switch (history.get(1)) {
		case INPUT_JUMP:
			return CombatState.SPIN;
		case INPUT_LEFT:
		case INPUT_RIGHT:
			return CombatState.ONETWOCOMBO;
		case INPUT_DOWN:
			return CombatState.LOWMIDDLEKICK;
		default: // up
			return CombatState.UPPERCUT;
		}
	}
}
