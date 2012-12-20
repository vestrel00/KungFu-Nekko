package com.vestrel00.nekko.actors;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.NekkoSprite;

public class Nekko extends Actor {

	private NekkoSprite nekkoSprite;

	public Nekko(TextureAtlas atlas, Location location) {
		super(location);
		nekkoSprite = new NekkoSprite(this, atlas);
		sprite = nekkoSprite;
	}

}
