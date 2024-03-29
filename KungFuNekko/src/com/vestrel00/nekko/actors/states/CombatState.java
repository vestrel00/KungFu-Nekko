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

package com.vestrel00.nekko.actors.states;

public enum CombatState {
	ATTACK, SPECIAL, // for monsters
	IDLE, // out of combat
	// normal default attack aoe
	JAB,
	// double hit attacks 1 damage * 2 aoe
	ONETWOCOMBO, LOWMIDDLEKICK,
	// power attacks 2 damage aoe
	UPPERCUT,
	// aoe attacks 1 damage
	TWOSIDEDATTACK,
	// aoe power attacks 2 damage
	ROUNDKICK, DOWNWARDKICK, HIGHKICK,
	// ultimate attack aoe 3 damage
	SUPERUPPERCUT,
	// jump attack 1 damage aoe
	SPIN,
	// aoe jump attack 2 damage
	FLYINGKICK,
	// projectile attacks 1 - 2 - 3 - 4 damage aoe
	FASTSHOT, JUMPSHOT, POWERSHOT, SUPERSHOT, 
	// special mixed combo attacks (ender moves)
	BEATDOWN, LIGHTNINGKICKS, SPEEDYHANDS, GATTLINGSHOT;
}
