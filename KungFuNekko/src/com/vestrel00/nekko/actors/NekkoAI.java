package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.NekkoAISprite;
import com.vestrel00.nekko.actors.components.NekkoSprite;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.ui.components.SimpleAttackManager;

public class NekkoAI extends Nekko {

	private Actor target;
	public int primaryCombo;
	public float aggroRange, motionRange;
	private Rectangle aggroRect;
	public FaceState primaryFaceState;
	private SimpleAttackManager attackManager;

	public NekkoAI(TextureAtlas atlas, Location location, Array<Actor> targets,
			int maxHealth, Color color, float aggroRange, float motionRange) {
		super(atlas, location, targets, maxHealth, color, 0);
		this.aggroRange = aggroRange;
		this.motionRange = motionRange;
		nekkoSprite = new NekkoAISprite(this, atlas, color);
		sprite = nekkoSprite;
		location.setActor(this);
		aggroRect = new Rectangle();
		nekkoSprite.cameraEffect = false;
		nekkoSprite.attackAOE = false;
		attackManager = new SimpleAttackManager(this);
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance) {
		boolean hit = false;
		for (int i = 0; i < targets.size; i++)
			if (targets.get(i).statusState == StatusState.ALIVE
					&& location.rect.overlaps(targets.get(i).location.rect)) {
				targets.get(i).receiveDamage(damage);
				hit(targets.get(i));
				targets.get(i).location.knockBack(knockBackDistance,
						(faceState == FaceState.LEFT) ? -1.0f : 1.0f);
				hit = true;
				if (!aoe)
					break;
			}
		if (TimeUtils.nanoTime() - NekkoSprite.lastCombatSound > 200000000L) {
			NekkoSprite.lastCombatSound = TimeUtils.nanoTime();
			switch (combatState) {
			case BEATDOWN:
			case LIGHTNINGKICKS:
			case SPEEDYHANDS:
				KFNekko.audio.punchSpecial(location.x, 0.5f);
				break;
			default:
				KFNekko.audio.punch(location.x);
				break;
			}
			if (hit) {
				if (combatState == CombatState.SUPERUPPERCUT
						|| combatState == CombatState.FLYINGKICK)
					KFNekko.audio.superSmack(location.x);
				else
					KFNekko.audio.smack(location.x);
			}
		}
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
	public void onDeath() {
		if (statusState == StatusState.ALIVE) {
			statusState = StatusState.DYING;
		}
	}

	
	@Override
	public void receiveDamage(int damage) {
		if (health > 0) {
			health -= damage;
			if (health <= 0)
				onDeath();
		}
	}

	@Override
	public void update() {
		if (statusState != StatusState.DEAD) {
			if (target != null
					&& (target.statusState == StatusState.DEAD || !withinAggroRange(target)))
				target = null;
			// target selection
			if (target == null)
				for (int i = targets.size - 1; i > -1; i--)
					if (targets.get(i).statusState == StatusState.ALIVE
							&& withinAggroRange(targets.get(i))) {
						target = targets.get(i);
						break;
					}
			// update combatState
			updateCombatState();

			// update motionState
			updateMotionState();

			// cannot attack and walk/move/run at the same time while on
			// platform
			if (combatState != CombatState.IDLE
					&& (location.onPlatform || location.onSlope)) {
				location.speed.xSpeed = 0.0f;
				horizontalMotionState = HorizontalMotionState.IDLE;
			}
			super.update();
		}
	}

	protected void updateMotionState() {
		if (target != null) {
			faceState = (target.location.x >= location.x) ? FaceState.RIGHT
					: FaceState.LEFT;
			setHorizontalMotionState(HorizontalMotionState.MOVING);
			if (target.location.rect.y > location.rect.y + location.rect.height)
				jump();
		} else if (primaryFaceState != null) {
			faceState = primaryFaceState;
			setHorizontalMotionState(HorizontalMotionState.MOVING);
		}

		if (location.x < 30.0f || location.x < location.spawnX - motionRange)
			faceState = FaceState.RIGHT;
		else if (location.x > KFNekko.map.width - 30.0f
				|| location.x > location.spawnX + motionRange)
			faceState = FaceState.LEFT;

	}

	protected boolean withinAggroRange(Actor targ) {
		aggroRect.set(location.x - aggroRange, location.rect.y,
				aggroRange * 2.0f, location.rect.height);
		return aggroRect.overlaps(targ.location.rect);
	}

	private void updateCombatState() {
		if (target != null && sprite.combatIndex == 0
				&& location.rect.overlaps(target.location.rect)) {
			setCombatState(attackManager.input(primaryCombo));
		}
	}
}
