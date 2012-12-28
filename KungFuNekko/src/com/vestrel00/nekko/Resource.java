/***************************************************************************
 *  Copyright (C) 2012 by Vandolf Estrellado
 *  All Rights Reserved
 * 
 *  This file is part of KungFu Nekko.
 *  KungFu Nekko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KungFu Nekko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KungFu Nekko.  If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/

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
		// arial = new BitmapFont(); GWT gives error if this is used
		arial = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"),
				Gdx.files.internal("fonts/arial.png"), false);
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
