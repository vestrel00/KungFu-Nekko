package com.vestrel00.nekko.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.Sprite;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.actors.states.Visibility;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public abstract class Actor implements Updatable, Drawable {

	public Sprite sprite;
	public Location location;
	public Array<Actor> targets;

	public FaceState faceState;
	public StatusState statusState;
	public CombatState combatState;
	public HorizontalMotionState horizontalMotionState;
	public VerticalMotionState verticalMotionState;
	public Visibility visibility;

	public int maxHealth, health;

	public Actor(Array<Actor> targets, Location location, int maxHealth) {
		this.location = location;
		this.targets = targets;
		this.maxHealth = maxHealth;
	}

	public void setState(FaceState faceState, StatusState statusState,
			CombatState combatState,
			HorizontalMotionState horizontalMotionState,
			VerticalMotionState verticalMotionState) {
		this.faceState = faceState;
		this.statusState = statusState;
		this.combatState = combatState;
		this.horizontalMotionState = horizontalMotionState;
		this.verticalMotionState = verticalMotionState;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (statusState == StatusState.ALIVE
				&& visibility == Visibility.VISIBLE)
			sprite.draw(batch);
	}

	@Override
	public void update() {
		location.update();
		if (statusState == StatusState.ALIVE
				&& visibility == Visibility.VISIBLE)
			sprite.update();
	}

	public boolean jump() {
		return location.jump();
	}

	/**
	 * Attack the list of targets.
	 */
	public void attack(int damage, boolean aoe, float knockBackDistance,
			Actor actor) {
		boolean hit = false;
		for (int i = 0; i < targets.size; i++)
			if (location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				targets.get(i).location.knockBack(knockBackDistance,
						(actor.faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				hit = true;
				if (!aoe)
					break;
			}
		// TODO add hit sound if hit!
	}

	public void setHorizontalMotionState(
			HorizontalMotionState horizontalMotionState) {
		if (this.horizontalMotionState != HorizontalMotionState.KNOCKED_BACK)
			this.horizontalMotionState = horizontalMotionState;
	}

	protected void receiveDamage(int damage) {
		health -= damage;
		if (health <= 0)
			onDeath();
	}

	public void onDeactivateCombat() {
		combatState = CombatState.IDLE;
	}

	public void setCombatState(CombatState combatState) {
		this.combatState = combatState;
	}

	public abstract void onDeath();

	public abstract void onLanding();

}
