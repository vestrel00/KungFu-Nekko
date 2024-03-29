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
