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

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.vestrel00.nekko.KFNekko;

public class MapPieceGenerator {

	private static AtlasRegion[] tiles;

	public static void init() {
		tiles = new AtlasRegion[15];
		for (int i = 0; i < tiles.length; i++)
			tiles[i] = KFNekko.resource.atlas.findRegion("piece"
					+ String.valueOf(i));
	}

	public static MapPiece genBridge(boolean padLeft, boolean padRight,
			int size, float offsetX, float offsetY) {
		// 32 corresponds to piece14
		MapPiece piece = new MapPiece(tiles[1].originalWidth * size + 64,
				tiles[1].originalHeight);

		// platform info
		piece.horizontal = new float[3];
		piece.horizontal[0] = (padLeft) ? -1 : 31;
		piece.horizontal[1] = piece.rect.height;
		piece.horizontal[2] = piece.rect.width - ((padRight) ? -1 : 31);

		piece.regions = new AtlasRegion[size + ((padLeft) ? 1 : 0)
				+ ((padRight) ? 1 : 0)];
		piece.regionRects = new Rectangle[piece.regions.length];

		// bridges
		for (int i = 0, x = 32; i < size; i++, x += tiles[1].originalWidth) {
			piece.regions[i] = tiles[1];
			piece.regionRects[i] = new Rectangle(x, 0,
					piece.regions[i].originalWidth,
					piece.regions[i].originalHeight);
		}
		int c = size;
		// paddings
		if (padLeft) {
			piece.regions[c] = tiles[14];
			piece.regionRects[c] = new Rectangle(0, 0, 32, 64);
			c++;
		}
		if (padRight) {
			piece.regions[c] = tiles[14];
			piece.regionRects[c] = new Rectangle(piece.rect.width - 32, 0,
					32, 64);
		}

		piece.translate(offsetX, offsetY);
		return piece;
	}

	public static MapPiece genSingle(int tileId, float offsetX, float offsetY) {
		MapPiece piece = new MapPiece(tiles[tileId].originalWidth,
				tiles[tileId].originalHeight);
		piece.regions = new AtlasRegion[1];
		piece.regions[0] = tiles[tileId];
		piece.regionRects = new Rectangle[piece.regions.length];
		piece.regionRects[0] = new Rectangle(0, 0, tiles[tileId].originalWidth,
				tiles[tileId].originalHeight);
		piece.translate(offsetX, offsetY);
		return piece;
	}

	public static MapPiece genRailing(int size, float offsetX, float offsetY) {
		MapPiece piece = new MapPiece(64 * size, tiles[9].originalHeight);

		piece.regions = new AtlasRegion[size];
		piece.regionRects = new Rectangle[piece.regions.length];

		for (int i = 0, x = 0; i < size; i++, x += 64) {
			piece.regions[i] = tiles[9];
			piece.regionRects[i] = new Rectangle(x, 0, tiles[9].originalWidth,
					tiles[9].originalHeight);
		}

		piece.translate(offsetX, offsetY);
		return piece;
	}

	/**
	 * 
	 * @param type
	 *            0 is piece 7||8, 1 is 3||4
	 * @param orientation
	 *            0 is negative slope, 1 is positive slope
	 * 
	 */
	public static MapPiece genStairs(int type, int orientation, int size,
			float offsetX, float offsetY) {
		MapPiece piece = new MapPiece(64 * size, 64 * size
				+ ((type == 0) ? 96 : 168));

		// slope info
		piece.slope = new float[4];
		piece.slope[0] = 0.0f; // x1
		piece.slope[1] = (orientation == 0) ? 64 * size : 0.0f; // y1
		piece.slope[2] = piece.rect.width; // x2
		piece.slope[3] = (orientation == 0) ? 0.0f : 64 * size; // y2

		piece.regions = new AtlasRegion[size];
		piece.regionRects = new Rectangle[piece.regions.length];

		AtlasRegion region = null;
		if (type == 0)
			region = (orientation == 0) ? tiles[8] : tiles[7];
		else
			region = (orientation == 0) ? tiles[3] : tiles[4];

		// from left to right
		int startY = (orientation == 0) ? (64 * size - 64) : 0;
		int incrementY = (orientation == 0) ? -64 : 64;
		for (int i = 0, x = 0, y = startY; i < size; i++, x += 64, y += incrementY) {
			piece.regions[i] = region;
			piece.regionRects[i] = new Rectangle(x, y, region.originalWidth,
					region.originalHeight);
		}

		piece.translate(offsetX, offsetY);
		return piece;
	}

	/**
	 * 
	 * @param filler
	 *            save platform info?
	 * @param widthType
	 *            0 is 64 (piece0), 1 is 32 (piece14)
	 * 
	 */
	public static MapPiece genQuads(boolean filler, int widthType, int columns,
			int rows, float offsetX, float offsetY) {
		int width = (widthType == 0) ? 64 : 32;
		MapPiece piece = new MapPiece(width * columns, 64 * rows);

		// platform info
		if (!filler) {
			piece.horizontal = new float[3];
			piece.horizontal[0] = -4.0f; // x
			piece.horizontal[1] = piece.rect.height; // y2
			piece.horizontal[2] = piece.rect.width + 4.0f; // x2
		}

		piece.regions = new AtlasRegion[columns * rows];
		piece.regionRects = new Rectangle[piece.regions.length];

		AtlasRegion region = (widthType == 0) ? tiles[0] : tiles[14];
		for (int i = 0, x = 0; i < columns; i++, x += width)
			for (int j = 0, y = 0; j < rows; j++, y += 64) {
				int c = i * rows + j;
				piece.regions[c] = region;
				piece.regionRects[c] = new Rectangle(x, y,
						region.originalWidth, region.originalHeight);
			}

		piece.translate(offsetX, offsetY);
		return piece;
	}

}
