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

package com.vestrel00.nekko.maps.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.interf.Drawable;

public class MapSection implements Drawable {

	public Array<MapPiece> pieces;
	public Rectangle rect;

	public MapSection(float x, float y, float width, float height) {
		rect = new Rectangle(x, y, width, height);
		pieces = new Array<MapPiece>();
	}

	public MapSection(Rectangle rect) {
		this.rect = rect;
		pieces = new Array<MapPiece>();
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (KFNekko.camera.rect.overlaps(rect))
			for (int i = 0; i < pieces.size; i++)
				pieces.get(i).draw(batch);
	}

	public void translate(float offsetX, float offsetY) {
		rect.x += offsetX;
		rect.y += offsetY;
		for (int i = 0; i < pieces.size; i++)
			pieces.get(i).translate(offsetX, offsetY);
	}

}
