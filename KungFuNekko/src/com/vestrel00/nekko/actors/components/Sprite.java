package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.states.Visibility;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public abstract class Sprite implements Updatable, Drawable {

	protected Actor actor;
	public Color color;
	protected AtlasRegion currentTexture;
	protected float xScale, rotation;
	protected long animationDelay, lastAnimationTime;
	public int combatIndex = 0;

	public Sprite(Actor actor, Color color) {
		this.actor = actor;
		this.color = color;
		lastAnimationTime = 0L;
		rotation = 0;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (actor.visibility == Visibility.VISIBLE) {
			batch.setColor(color);
			batch.draw(currentTexture, actor.location.rect.x,
					actor.location.rect.y, actor.location.rect.width * 0.5f,
					actor.location.rect.height * 0.5f,
					actor.location.rect.width, actor.location.rect.height,
					xScale, 1.0f, rotation);
			batch.setColor(KFNekko.worldColor);
		}
	}
}
