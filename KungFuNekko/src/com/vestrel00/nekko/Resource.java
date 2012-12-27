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
