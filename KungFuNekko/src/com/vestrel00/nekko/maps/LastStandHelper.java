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

package com.vestrel00.nekko.maps;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.ChessPiece;
import com.vestrel00.nekko.interf.LevelHelper;
import com.vestrel00.nekko.maps.components.MapSection;

public abstract class LastStandHelper implements LevelHelper {

	public AtlasRegion spawnRegion;
	public Array<ChessPiece> chessPieces;
	public ChessPiece chosenPiece;
	public Rectangle[] spawnPortals;
	public Vector2[] monsterLocs;
	public Vector2 chosenSpawnLoc;
	public Random rand;
	public long monsterSpawnDelay, interSpawnDelay;
	public int maxBatchCount, difficulty;
	protected LastStand ls;

	public LastStandHelper(Array<MapSection> sections, LastStand ls) {
		this.ls = ls;
		rand = new Random();
		spawnRegion = KFNekko.resource.atlas.findRegion("blackHole");
	}

	protected void initSpawnPortals() {
		float halfWidth = (float) spawnRegion.originalWidth * 0.5f, halfHeight = (float) spawnRegion.originalHeight * 0.5f;
		spawnPortals = new Rectangle[monsterLocs.length];
		for (int i = 0; i < monsterLocs.length; i++)
			spawnPortals[i] = new Rectangle(monsterLocs[i].x - halfWidth,
					monsterLocs[i].y - halfHeight,
					(float) spawnRegion.originalWidth,
					(float) spawnRegion.originalHeight);
	}

	@Override
	public void gameWon() {
	}

	@Override
	public void gameOver() {
		if (KFNekko.view != KFNekko.VIEW_GAME_OVER) {
			ls.saveHighScores();
			KFNekko.view = KFNekko.VIEW_GAME_OVER;
			KFNekko.targetWorldColor.set(Color.GRAY);
			if (KFNekko.audio.music.isPlaying())
				KFNekko.audio.music.stop();
			if (KFNekko.settings.soundOn)
				KFNekko.audio.gameOver.play();
		}
	}

	public abstract void signalSpawn();

	public abstract void drawChessPieceIcons(SpriteBatch batch);

	public abstract void setCamera(int instructionIndex);

	public abstract void setMonsterSpawnDelay(int wave);

}
