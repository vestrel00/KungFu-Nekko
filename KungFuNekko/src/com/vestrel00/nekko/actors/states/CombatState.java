package com.vestrel00.nekko.actors.states;

public enum CombatState {
	IDLE, // out of combat
	// normal default attack
	FASTSHOT,
	// double hit attacks 1 damage * 2
	ONETWOCOMBO, LOWMIDDLEKICK,
	// power attacks 2 damage
	POWERSHOT, UPPERCUT,
	// aoe attacks 1 damage
	TWOSIDEDATTACK,
	// aoe power attacks 2 damage
	ROUNDCKICK, DOWNWARDKICK, HIGHKICK,
	// ultimate attack aoe 3 damage
	SUPERUPPERCUT,
	// jump attack 1 damage
	SPIN,
	// aoe jump attack 1 damage
	FLYINGKICK;
}
