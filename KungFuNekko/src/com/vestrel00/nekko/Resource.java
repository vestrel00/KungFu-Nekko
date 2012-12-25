package com.vestrel00.nekko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Resource implements Disposable {

	public TextureAtlas atlas;
	public BitmapFont chunkFive, arial;

	public Resource() {
		atlas = new TextureAtlas(Gdx.files.internal("texture/main.pack"));
		arial = new BitmapFont();
		chunkFive = new BitmapFont(Gdx.files.internal("fonts/chunkFive.fnt"),
				Gdx.files.internal("fonts/chunkFive.png"), false);
	}

	@Override
	public void dispose() {
		atlas.dispose();
		arial.dispose();
		chunkFive.dispose();
	}

}
