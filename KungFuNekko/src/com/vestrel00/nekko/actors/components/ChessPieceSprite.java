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

package com.vestrel00.nekko.actors.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.Methods;
import com.vestrel00.nekko.actors.ChessPiece;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.Visibility;

public class ChessPieceSprite extends Sprite {

	private ChessPiece piece;
	public Color targetColor;
	public float colorSpeed;

	public ChessPieceSprite(ChessPiece piece, Color color, AtlasRegion region) {
		super(piece, color);
		this.piece = piece;
		currentTexture = region;
		targetColor = new Color(color);
		colorSpeed = 0.04f;
	}

	@Override
	public void update() {
		if (piece.statusState != StatusState.DEAD) {
			if (piece.statusState == StatusState.DYING && color.a < 0.1f)
				piece.statusState = StatusState.DEAD;
			Methods.updateColor(color, targetColor, colorSpeed);
		}
	}

	public void drawIcon(SpriteBatch batch, float x, float y, float scale) {
		batch.setColor(color);
		batch.draw(currentTexture, KFNekko.camera.rect.x + x,
				KFNekko.camera.rect.y + y,
				currentTexture.originalWidth * scale,
				currentTexture.originalHeight * scale);
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (actor.visibility == Visibility.VISIBLE) {
			batch.setColor(color);
			batch.draw(currentTexture, actor.location.rect.x,
					actor.location.rect.y, actor.location.rect.width * 0.5f,
					actor.location.rect.height * 0.5f,
					actor.location.rect.width, actor.location.rect.height,
					xScale, 1, rotation);
			// TODO draw a guide to this piece is attacking
		}

	}
}
