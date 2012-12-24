package com.vestrel00.nekko.actors;

import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;

public abstract class Monster extends Actor {

	protected Actor target;
	protected float aggroRange, motionRange, originX;

	public Monster(Array<Actor> targets, Location location, int maxHealth,
			float aggroRange, float motionRange) {
		super(targets, location, maxHealth);
		this.aggroRange = aggroRange;
		this.motionRange = motionRange;
		originX = location.x;
	}

	@Override
	protected void receiveDamage(int damage) {
		super.receiveDamage(damage);
		float c = (float) damage / (float) maxHealth;

		if ((sprite.color.r += c) > 1.0f)
			sprite.color.r = 1.0f;
		if ((sprite.color.g += c) > 1.0f)
			sprite.color.g = 1.0f;
		if ((sprite.color.b += c) > 1.0f)
			sprite.color.b = 1.0f;
	}

	@Override
	public void update() {
		// make sure that target is still alive and within the aggro range
		if (target != null
				&& (target.statusState == StatusState.DEAD || !withinAggroRange(target)))
			target = null;
		// target selection
		if (target == null)
			for (int i = 0; i < targets.size; i++)
				if (withinAggroRange(targets.get(i))) {
					target = targets.get(i);
					break;
				}

		// update combatState
		updateCombatState();

		// update motionState
		updateMotionState();

		super.update();
	}

	protected void updateCombatState() {
		if (target != null && sprite.combatIndex == 0
				&& location.rect.overlaps(target.location.rect)) {
			setCombatState(CombatState.ATTACK);
			// TODO SOUND
		}
	}

	protected void updateMotionState() {
		if (target != null) {
			faceState = (target.location.x >= location.x) ? FaceState.RIGHT
					: FaceState.LEFT;
			setHorizontalMotionState(HorizontalMotionState.MOVING);
		}

		if (location.x < 30.0f || location.x < originX - motionRange)
			faceState = FaceState.RIGHT;
		else if (location.x > KFNekko.map.width - 30.0f
				|| location.x > originX + motionRange)
			faceState = FaceState.LEFT;

	}

	protected boolean withinAggroRange(Actor targ) {
		return targ.location.x >= location.x - aggroRange
				&& targ.location.x <= location.x + aggroRange
				&& targ.location.y >= location.y - aggroRange
				&& targ.location.y <= location.y + aggroRange;
	}

}