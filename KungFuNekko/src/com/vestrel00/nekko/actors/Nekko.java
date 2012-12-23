package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.NekkoSprite;
import com.vestrel00.nekko.actors.components.Speed;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;

public class Nekko extends Actor {

	private NekkoSprite nekkoSprite;

	public Nekko(TextureAtlas atlas, Location location, Array<Actor> targets,
			int maxHealth, Color color) {
		super(targets, location, maxHealth);
		nekkoSprite = new NekkoSprite(this, atlas, color);
		sprite = nekkoSprite;
	}

	@Override
	protected void receiveDamage(int damage) {
		super.receiveDamage(damage);
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
			location.speed.xSpeed = 8.0f;
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
			this.combatState = combatState;
			executeCombatMove();
		} else if (nekkoSprite.combatIndex == 0
				&& combatState == CombatState.IDLE)
			this.combatState = combatState;
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
	public void onLanding() {
		switch (combatState) {
		case SPIN:
			KFNekko.camera.setEffect(Camera.EFFECT_SHAKE, 1.0f, 3.0f,
					200000000L);
			nekkoSprite.activateSmoke();
			break;
		}
	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

}
