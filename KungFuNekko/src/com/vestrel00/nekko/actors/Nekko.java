package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.NekkoSprite;
import com.vestrel00.nekko.actors.components.Speed;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;

public class Nekko extends Actor {

	private NekkoSprite nekkoSprite;
	public int stamina, maxStamina;

	public Nekko(TextureAtlas atlas, Location location, Array<Actor> targets,
			int maxHealth, Color color, int maxStamina) {
		super(targets, location, maxHealth);
		nekkoSprite = new NekkoSprite(this, atlas, color);
		sprite = nekkoSprite;
		this.maxStamina = maxStamina;
		stamina = maxStamina;
	}

	private void hit(Actor target) {
		switch (combatState) {
		case SUPERUPPERCUT:
		case UPPERCUT:
		case SPIN:
		case FLYINGKICK:
		case ROUNDKICK:
			target.location.onPlatform = true;
			target.jump();
			break;
		}
	}

	@Override
	public void update() {
		// cannot attack and walk/move/run at the same time while on
		// platform
		if (combatState != CombatState.IDLE && location.onPlatform) {
			location.speed.xSpeed = 0.0f;
			horizontalMotionState = HorizontalMotionState.IDLE;
		}
		super.update();
	}

	private void executeCombatMove() {
		switch (combatState) {
		case SUPERUPPERCUT:
			nekkoSprite.activateSmoke();
		case SPIN:
			jump();
			horizontalMotionState = HorizontalMotionState.FORCED_MOVING;
			location.speed.xDirection = (faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
					: Speed.DIRECTION_RIGHT;
			location.speed.xSpeed = 6.0f;
			break;
		case FASTSHOT:

			break;
		case POWERSHOT:

			break;
		case FLYINGKICK:
			jump();
			horizontalMotionState = HorizontalMotionState.FORCED_MOVING;
			location.speed.xDirection = (faceState == FaceState.LEFT) ? Speed.DIRECTION_LEFT
					: Speed.DIRECTION_RIGHT;
			location.speed.xSpeed = 20.0f;
			break;
		case ONETWOCOMBO:

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
	public void onDeactivateCombat() {
		switch (combatState) {
		case SPIN:
		case SUPERUPPERCUT:
		case FLYINGKICK:
			horizontalMotionState = HorizontalMotionState.IDLE;
			location.speed.xSpeed = 0.0f;
			break;
		case FASTSHOT:

			break;
		case POWERSHOT:

			break;
		case ONETWOCOMBO:

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

		super.onDeactivateCombat();
	}

	@Override
	public void setCombatState(CombatState combatState) {
		// wait for the combat move in execution to finish
		if (nekkoSprite.combatIndex == 0 && combatState != CombatState.IDLE) {
			if (enoughStamina(combatState)) {
				this.combatState = combatState;
				executeCombatMove();
			}
		} else if (nekkoSprite.combatIndex == 0
				&& combatState == CombatState.IDLE)
			this.combatState = combatState;
	}

	private boolean enoughStamina(CombatState combat) {
		int staminaCost = 0;
		switch (combat) {
		case FASTSHOT:
		case LOWMIDDLEKICK:
		case ONETWOCOMBO:
			staminaCost = 1;
			break;
		case SPIN:
		case UPPERCUT:
		case ROUNDKICK:
			staminaCost = 3;
			break;
		case FLYINGKICK:
			staminaCost = 4;
			break;
		case POWERSHOT:
		case HIGHKICK:
		case DOWNWARDKICK:
			staminaCost = 2;
			break;
		case TWOSIDEDATTACK:
		case SUPERUPPERCUT:
			staminaCost = 6;
			break;
		}
		if (stamina >= staminaCost) {
			stamina -= staminaCost;
			return true;
		}
		return false;
	}

	@Override
	public boolean jump() {
		if (super.jump()) {
			nekkoSprite.jumpIndex = 0;
			return true;
		} else {
			if (!location.doubleJumped) {
				location.onPlatform = true;
				location.doubleJumped = true;
				nekkoSprite.doubleJumpIndex = 0;
				location.jump();
			}
		}
		return false;
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance,
			Actor actor) {
		boolean hit = false;
		for (int i = 0; i < targets.size; i++)
			if (targets.get(i).statusState == StatusState.ALIVE
					&& location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				hit(targets.get(i));
				targets.get(i).location.knockBack(knockBackDistance,
						(actor.faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				hit = true;
				if (!aoe)
					break;
			}
		KFNekko.audio.punch(location.x);
		if (hit) {
			if (combatState == CombatState.SUPERUPPERCUT
					|| combatState == CombatState.FLYINGKICK)
				KFNekko.audio.superSmack(location.x);
			else
				KFNekko.audio.smack(location.x);
		}
	}

}
