package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.components.CuteMonsterSprite;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.StatusState;

public class CuteMonster extends Monster {

	private static long lastGrowlTime;
	private CuteMonsterSprite cuteSprite;
	private long lastjumpTime, lastAttackTime;
	public int damage;
	public float knockBackDistance;

	public CuteMonster(TextureAtlas atlas, Location location,
			Array<Actor> targets, int maxHealth, float aggroRange,
			float motionRange, Color color, int level) {
		super(targets, location, maxHealth, aggroRange, motionRange);
		cuteSprite = new CuteMonsterSprite(this, atlas, color);
		sprite = cuteSprite;

		knockBackDistance = 20.0f + (float) level;
		damage = 1 + level / 2;
		maxHealth = 4 + level * 2;
		health = maxHealth;
	}

	@Override
	protected void updateMotionState() {
		super.updateMotionState();
		if (target == null && TimeUtils.nanoTime() - lastjumpTime > 5000000000L) {
			lastjumpTime = TimeUtils.nanoTime();
			jump();
		}
	}

	@Override
	public void attack(int damage, boolean aoe, float knockBackDistance,
			Actor actor) {
		if (TimeUtils.nanoTime() - lastAttackTime > 2000000000L) {
			lastAttackTime = TimeUtils.nanoTime();
			for (int i = 0; i < targets.size; i++)
				if (targets.get(i).statusState == StatusState.ALIVE
						&& location.rect.overlaps(targets.get(i).location.rect)) {
					targets.get(i).receiveDamage(damage);
					targets.get(i).location.knockBack(knockBackDistance,
							(actor.faceState == FaceState.LEFT) ? -1.0f : 1.0f);
					if (!aoe)
						break;
				}
			if (TimeUtils.nanoTime() - lastGrowlTime > 500000000L) {
				lastGrowlTime = TimeUtils.nanoTime();
				KFNekko.audio.growl(location.x);
			}
		}
	}

}
