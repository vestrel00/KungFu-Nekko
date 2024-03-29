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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.ChessPiece;
import com.vestrel00.nekko.actors.NekkoAI;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.maps.components.MapPieceGenerator;
import com.vestrel00.nekko.maps.components.MapSection;

public class LSKingsAndQueens extends LastStandHelper {

	public LSKingsAndQueens(Array<MapSection> sections, LastStand ls) {
		super(sections, ls);
		// all map sections will start at (0, 0)
		MapSection section1 = new MapSection(0.0f, 0.0f, 832.0f, 576.0f);
		sections.add(section1);
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 384, 0));
		// fillers
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 4, 2, 128, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 4, 2, 768, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 4, 2, 0, 128));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 4, 2, 896, 128));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 2, 2, 0, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 2, 2, 0, 1024));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 3, 0, 256));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 3, 1088, 256));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 2, 64, 256));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 2, 1024, 256));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 1, 128, 256));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 1, 960, 256));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 1, 256, 128));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 1, 832, 128));
		// stairs
		section1.pieces.add(MapPieceGenerator.genStairs(1, 0, 6, 0, 128));
		section1.pieces.add(MapPieceGenerator.genStairs(1, 1, 6, 768, 128));

		KFNekko.map.width = 1152.0f;
		KFNekko.map.height = 960.0f;
		KFNekko.background.setOffsetRatio();

		// initialize the player
		KFNekko.player.maxHealth = 1000;
		KFNekko.player.reset(576.0f, 704.0f);
		KFNekko.player.setState(FaceState.LEFT, StatusState.ALIVE,
				CombatState.IDLE, HorizontalMotionState.IDLE,
				VerticalMotionState.FALLING);

		// camera work
		KFNekko.camera.targetActor = KFNekko.player;
		KFNekko.camera.mode = Camera.MODE_SCROLL;
		KFNekko.camera.normalizeXSpeed = 3;
		KFNekko.camera.normalizeYSpeed = 1;

		// init the pieces
		initChessPieces();

		// initialize monster spawn locations
		monsterLocs = new Vector2[3];
		monsterLocs[0] = new Vector2(192.0f, 576.0f);
		monsterLocs[1] = new Vector2(576.0f, 384.0f);
		monsterLocs[2] = new Vector2(960.0f, 576.0f);

		//
		maxBatchCount = 5;
		interSpawnDelay = 200000000L;
		difficulty = 1;

		// init spawn rectangles for checking visibility
		initSpawnPortals();
	}

	private void initChessPieces() {
		chessPieces = new Array<ChessPiece>();
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
						new Location(470.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 3000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.KING,
						new Location(576.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 6000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
						new Location(682.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 3000));
		KFNekko.allies.clear();
		KFNekko.allies.add(KFNekko.player);
		KFNekko.allies.addAll(chessPieces);
	}

	private boolean spawnAtLoc(int loc) {
		chosenSpawnLoc = monsterLocs[loc];
		if (chessPieces.get(loc).statusState != StatusState.DEAD) {
			chosenPiece = chessPieces.get(loc);
			return true;
		} else
			return false;
	}

	@Override
	public void signalSpawn() {
		switch (rand.nextInt(3)) {
		case 0:
			if (!(spawnAtLoc(0) || spawnAtLoc(1) || spawnAtLoc(2)))
				gameOver(); // all pieces dead
			break;
		case 1:
			if (!(spawnAtLoc(1) || spawnAtLoc(2) || spawnAtLoc(0)))
				gameOver(); // all pieces dead
			break;
		case 2:
			if (!(spawnAtLoc(2) || !spawnAtLoc(0) || spawnAtLoc(1)))
				gameOver(); // all pieces dead
			break;
		}
	}

	@Override
	public void drawChessPieceIcons(SpriteBatch batch) {
		for (int i = 0, x = 210; i < chessPieces.size; x += chessPieces.get(i).chessSprite.currentTexture.originalWidth * 0.25f, i++)
			chessPieces.get(i).chessSprite.drawIcon(batch, x, 260.0f, 0.15f);
	}

	@Override
	public void setCamera(int instructionIndex) {
		switch (instructionIndex) {
		case 1: // "The Pieces"
			KFNekko.player.nekkoSprite.targetColor.set(Color.DARK_GRAY);
			for (int i = 0; i < chessPieces.size; i++) {
				chessPieces.get(i).chessSprite.targetColor.set(Color.BLUE);
				// vibrate until next instruction
				chessPieces.get(i).hit = true;
				chessPieces.get(i).lastAttackTime = TimeUtils.nanoTime();
				chessPieces.get(i).vibrationDuration = 10000000000000000L;
				chessPieces.get(i).forceVibrate = true;
				chessPieces.get(i).vibrationSpeed = 1.0f;
				chessPieces.get(i).vibrationDist = 1.0f;
			}
			break;
		case 2: // skip to Queen
			ls.instruction.instructionIndex = 4;
			// undo changes
			KFNekko.player.nekkoSprite.targetColor.set(Color.WHITE);
			for (int i = 0; i < chessPieces.size; i++) {
				chessPieces.get(i).chessSprite.targetColor.set(Color.WHITE);
				chessPieces.get(i).resetVibrationState();
			}
			KFNekko.camera.targetActor = chessPieces.get(0);
			chessPieces.get(0).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 5: // Skip to KING
			ls.instruction.instructionIndex = 7;
			chessPieces.get(0).chessSprite.targetColor.set(Color.WHITE);
			KFNekko.camera.targetActor = chessPieces.get(1);
			chessPieces.get(1).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 8: // "Black Hole"
			// reset
			chessPieces.get(1).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = null;
			KFNekko.camera.targetLoc.set(monsterLocs[1]);
			break;
		}
	}

	@Override
	public void setMonsterSpawnDelay(int wave) {
		if ((monsterSpawnDelay = 5000000000L - (long) wave * 1000000000L) < 1000000000L)
			monsterSpawnDelay = 1000000000L;
	}

	@Override
	public NekkoAI spawnCat(int catType) {
		// TODO Auto-generated method stub
		return null;
	}

}
