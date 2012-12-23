package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.actors.components.CuteMonsterSprite;
import com.vestrel00.nekko.actors.components.Location;

public class CuteMonster extends Monster {

	private CuteMonsterSprite cuteSprite;
	private long lastjumpTime;
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
	public void onLanding() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeath() {
		// TODO Auto-generated method stub

	}

}
