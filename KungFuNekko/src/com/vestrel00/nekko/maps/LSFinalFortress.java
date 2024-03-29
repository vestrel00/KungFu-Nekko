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

/**
 * <p>
 * P.S. I know I should not hard code these in but use a parsing method instead
 * - like JSON or xml or hell even a .txt file (similar to the way the
 * TextureAtlas parses things). If I do, There will be no need for these
 * subclasses and will result in less lines of code overall. However, I rushed
 * things and this is just a small game so what the hell, hard code away!
 * </p>
 * 
 * <p>
 * I will use a parsing method later on so you guys can make maps without
 * looking at code. Then I will also make a Tiled-like map editor custom made
 * for my game(s)! But that is for a later time.
 * </p>
 * 
 * @author Estrellado, Vandolf
 * 
 */
public class LSFinalFortress extends LastStandHelper {

	public LSFinalFortress(Array<MapSection> sections, LastStand ls) {
		super(sections, ls);
		// all map sections will start at (0, 0)
		MapSection section1 = new MapSection(0.0f, 0.0f, 1504.0f, 832.0f);
		MapSection section2 = new MapSection(0.0f, 0.0f, 1504.0f, 768.0f);
		MapSection section3 = new MapSection(0.0f, 0.0f, 448.0f, 320.0f);
		sections.add(section1);
		sections.add(section2);
		sections.add(section3);

		// SECTION 1
		// Columns
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 12, 64, 64));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 11, 128, 128));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 10, 192, 192));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 9, 256, 256));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 8, 320, 320));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 5, 512, 512));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 4, 704, 576));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 1, 4, 896, 576));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 4, 1088, 576));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 6, 1280, 448));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 1, 1, 6, 1472, 448));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 1, 1, 3, 1472, 192));
		section1.pieces.add(MapPieceGenerator
				.genQuads(true, 0, 1, 4, 1216, 448));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 2, 4, 960, 576));
		// inner filler quads
		for (int i = 0, x = 64, y = 0; i < 8; i++, x += 64, y += 64)
			section1.pieces
					.add(MapPieceGenerator.genQuads(true, 0, 4, 1, x, y));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 7, 1, 768, 448));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 7, 1, 768, 448));
		section1.pieces
				.add(MapPieceGenerator.genQuads(true, 0, 9, 1, 704, 384));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 3, 3, 320, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(true, 0, 4, 2, 64.0f,
				-64.0f));
		// stairs
		section1.pieces.add(MapPieceGenerator.genStairs(1, 1, 8, 64, 64));
		section1.pieces.add(MapPieceGenerator.genStairs(1, 0, 2, 1152, 448));
		section1.pieces.add(MapPieceGenerator.genStairs(0, 1, 1, 1216, 704));
		// platform
		section1.pieces.add(MapPieceGenerator
				.genQuads(false, 0, 9, 1, 576, 512));
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 1, 1, 0, 0));
		// railing
		section1.pieces.add(MapPieceGenerator.genRailing(9, 574, 576));
		// special
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 1, 1, 1280,
				384));
		section1.pieces.add(MapPieceGenerator.genSingle(11, 1280, 448));
		// bridges
		section1.pieces.add(MapPieceGenerator.genBridge(false, false, 1, 1280,
				384));
		section1.pieces.add(MapPieceGenerator.genBridge(true, false, 1, 1280,
				704));
		// ornaments
		for (int i = 0, y1 = 448, y2 = 768, x = 1344; i < 2; i++, x += 96) {
			section1.pieces.add(MapPieceGenerator.genSingle(5, x, y1));
			section1.pieces.add(MapPieceGenerator.genSingle(6, x, y2));
		}
		section1.pieces.add(MapPieceGenerator.genSingle(10, 6, 64));

		// translate section1
		section1.translate(448.0f, 64.0f);

		// SECTION 2
		// Columns
		for (int i = 0, x = 128; i < 7; i++, x += 192)
			section2.pieces
					.add(MapPieceGenerator.genQuads(true, 0, 1, 8, x, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(true, 1, 1, 8, 1472, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 1, 960, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 7, 64, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 6, 192, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(true, 0, 1, 6, 256, 0));
		// bridges
		section2.pieces.add(MapPieceGenerator.genBridge(true, false, 7, 128,
				512));
		section2.pieces
				.add(MapPieceGenerator.genBridge(true, true, 3, 320, 64));
		section2.pieces.add(MapPieceGenerator.genBridge(true, false, 5, 512,
				258));
		// stairs
		section2.pieces.add(MapPieceGenerator.genStairs(0, 0, 2, 64, 192));
		section2.pieces.add(MapPieceGenerator.genStairs(0, 0, 1, 64, 448));
		section2.pieces.add(MapPieceGenerator.genStairs(0, 1, 1, 192, 384));
		section2.pieces.add(MapPieceGenerator.genStairs(0, 0, 2, 960, 0));
		section2.pieces.add(MapPieceGenerator.genStairs(0, 0, 2, 256, 320));
		// ornaments
		for (int i = 0, x = 576; i < 4; i++, x += 96)
			section2.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		for (int i = 0, x = 576; i < 10; i++, x += 96)
			section2.pieces.add(MapPieceGenerator.genSingle(5, x, 320));
		for (int i = 0, x = 320; i < 7; i++, x += 192)
			section2.pieces.add(MapPieceGenerator.genSingle(6, x, 576));
		// translate section2
		section2.translate(448.0f, 896.0f);

		// SECTION 3
		section3.pieces.add(MapPieceGenerator.genQuads(false, 0, 7, 2, 0, 0));
		// ornaments
		for (int i = 0, x = 64; i < 3; i++, x += 128)
			section3.pieces.add(MapPieceGenerator.genSingle(6, x, 128.0f));

		// set the map width and height
		KFNekko.map.width = 1952.0f;
		KFNekko.map.height = 1700.0f;
		KFNekko.background.setOffsetRatio();

		// initialize the player
		KFNekko.player.maxHealth = 400;
		KFNekko.player.reset(1234.0f, 892.0f);
		KFNekko.player.setState(FaceState.RIGHT, StatusState.ALIVE,
				CombatState.IDLE, HorizontalMotionState.IDLE,
				VerticalMotionState.FALLING);

		// camera work
		KFNekko.camera.targetActor = KFNekko.player;
		KFNekko.camera.mode = Camera.MODE_SCROLL;
		KFNekko.camera.normalizeXSpeed = 10;
		KFNekko.camera.normalizeYSpeed = 5;

		// init the pieces
		initChessPieces();

		// initialize monster spawn locations
		monsterLocs = new Vector2[3];
		monsterLocs[0] = new Vector2(140.0f, 244.0f);
		monsterLocs[1] = new Vector2(1851.0f, 1308.0f);
		monsterLocs[2] = new Vector2(1851.0f, 1576.0f);

		//
		maxBatchCount = 3;
		interSpawnDelay = 800000000L;
		difficulty = 1;

		// init spawn rectangles for checking visibility
		initSpawnPortals();
	}

	private boolean spawnAtLoc(int loc) {
		int i = 0, j = 0;
		chosenSpawnLoc = monsterLocs[loc];
		switch (loc) {
		case 0:
			i = 0;
			j = 4;
			break;
		case 1:
			i = 4;
			j = 7;
			break;
		case 2:
			i = 7;
			j = 11;
			break;
		}
		while (i < j) {
			if (chessPieces.get(i).statusState != StatusState.DEAD) {
				chosenPiece = chessPieces.get(i);
				return true;
			}
			i++;
		}
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

	private void initChessPieces() {
		chessPieces = new Array<ChessPiece>();
		// first level
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.PAWN,
				new Location(403.0f, 128.0f, 0, 0, 0, 0), FaceState.LEFT, 300));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.PAWN,
						new Location(1087.0f, 640.0f, 0, 0, 0, 0),
						FaceState.LEFT, 300));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.ROOK,
						new Location(1282.0f, 640.0f, 0, 0, 0, 0),
						FaceState.LEFT, 1000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
						new Location(1852.0f, 512.0f, 0, 0, 0, 0),
						FaceState.LEFT, 3000));
		// second level
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.ROOK,
				new Location(1472.0f, 1218.0f, 0, 0, 0, 0), FaceState.RIGHT,
				1000));
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.BISHOP,
				new Location(1284.0f, 1218.0f, 0, 0, 0, 0), FaceState.RIGHT,
				400));
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
				new Location(1084.0f, 1218.0f, 0, 0, 0, 0), FaceState.RIGHT,
				3000));
		// third level
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.KNIGHT,
				new Location(1467.0f, 1472.0f, 0, 0, 0, 0), FaceState.RIGHT,
				600));
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.BISHOP,
				new Location(1282.0f, 1472.0f, 0, 0, 0, 0), FaceState.RIGHT,
				400));
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.ROOK,
				new Location(1082.0f, 1472.0f, 0, 0, 0, 0), FaceState.RIGHT,
				1000));
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.KING,
				new Location(892.0f, 1472.0f, 0, 0, 0, 0), FaceState.RIGHT,
				5000));
		KFNekko.allies.clear();
		KFNekko.allies.add(KFNekko.player);
		KFNekko.allies.addAll(chessPieces);
	}

	@Override
	public void drawChessPieceIcons(SpriteBatch batch) {
		for (int i = 0, x = 130; i < chessPieces.size; x += chessPieces.get(i).chessSprite.currentTexture.originalWidth * 0.25f, i++)
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
		case 2: // "Pawn"
			KFNekko.camera.normalizeYSpeed = 6;
			// undo changes
			KFNekko.player.nekkoSprite.targetColor.set(Color.WHITE);
			for (int i = 0; i < chessPieces.size; i++) {
				chessPieces.get(i).chessSprite.targetColor.set(Color.WHITE);
				chessPieces.get(i).resetVibrationState();
			}
			KFNekko.camera.targetActor = chessPieces.get(0);
			chessPieces.get(0).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 3: // "Rook"
			KFNekko.camera.normalizeYSpeed = 7;
			// reset
			chessPieces.get(0).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(2);
			chessPieces.get(2).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 4: // "Queen"
			KFNekko.camera.normalizeYSpeed = 3;
			// reset
			chessPieces.get(2).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(3);
			chessPieces.get(3).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 5: // "Bishop"
			KFNekko.camera.normalizeYSpeed = 13;
			// reset
			chessPieces.get(3).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(5);
			chessPieces.get(5).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 6: // "Knight"
			KFNekko.camera.normalizeYSpeed = 14;
			// reset
			chessPieces.get(5).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(7);
			chessPieces.get(7).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 7: // "King"
			KFNekko.camera.normalizeYSpeed = 0;
			// reset
			chessPieces.get(7).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(10);
			chessPieces.get(10).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 8: // "Black Hole"
			KFNekko.camera.normalizeYSpeed = 17;
			// reset
			chessPieces.get(10).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = null;
			KFNekko.camera.targetLoc.set(monsterLocs[0]);
			break;
		}

	}

	@Override
	public void setMonsterSpawnDelay(int wave) {
		if ((monsterSpawnDelay = 20000000000L - (long) (wave / 2) * 1000000000L) < 1000000000L)
			monsterSpawnDelay = 1000000000L;
	}

	@Override
	public NekkoAI spawnCat(int catType) {
		// TODO Auto-generated method stub
		return null;
	}

}
