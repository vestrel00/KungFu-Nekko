/*******************************************************************************
 * Copyright 2012 Vandolf Estrellado
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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

	public abstract void onDeath();
}
