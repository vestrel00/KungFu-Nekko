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
public class LSRoadOfMartyrs extends LastStandHelper {

	public LSRoadOfMartyrs(Array<MapSection> sections, LastStand ls) {
		super(sections, ls);
		// all map sections will start at (0, 0)
		MapSection section1 = new MapSection(0.0f, 0.0f, 832.0f, 576.0f);
		MapSection section2 = new MapSection(0.0f, 0.0f, 768.0f, 576.0f);
		sections.add(section1);
		sections.add(section2);

		// SECTION 1
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 7, 2, 0, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 448, 0));
		section1.pieces.add(MapPieceGenerator.genSingle(10, 0, 128));
		section1.pieces.add(MapPieceGenerator.genSingle(9, 64, 128));
		section1.pieces.add(MapPieceGenerator.genSingle(11, 128, 128));
		for (int i = 0, x = 256; i < 3; i++, x += 192)
			section1.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		// SECTION 2
		section2.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 0, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 384, 0));
		for (int i = 0, x = 0; i < 4; i++, x += 192)
			section2.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		section2.translate(832, 0);

		KFNekko.map.width = 1600.0f;
		KFNekko.map.height = 576.0f;
		KFNekko.background.setOffsetRatio();

		// initialize the player
		KFNekko.player.maxHealth = 1000;
		KFNekko.player.reset(358.0f, 384.0f);
		KFNekko.player.setState(FaceState.LEFT, StatusState.ALIVE,
				CombatState.IDLE, HorizontalMotionState.IDLE,
				VerticalMotionState.FALLING);

		// camera work
		KFNekko.camera.targetActor = KFNekko.player;
		KFNekko.camera.mode = Camera.MODE_SCROLL;
		KFNekko.camera.normalizeXSpeed = 6;
		KFNekko.camera.normalizeYSpeed = 1;

		// init the pieces
		initChessPieces();

		// initialize monster spawn locations
		monsterLocs = new Vector2[1];
		monsterLocs[0] = new Vector2(96.0f, 307.0f);
		// only have 1 so
		chosenSpawnLoc = monsterLocs[0];

		//
		maxBatchCount = 4;
		interSpawnDelay = 300000000L;
		difficulty = 2;

		// init spawn rectangles for checking visibility
		initSpawnPortals();
	}

	private void initChessPieces() {
		chessPieces = new Array<ChessPiece>();
		chessPieces.add(new ChessPiece(KFNekko.enemies, ChessPiece.PAWN,
				new Location(552.0f, 128.0f, 0, 0, 0, 0), FaceState.LEFT, 800));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.BISHOP,
						new Location(744.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 1000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.KNIGHT,
						new Location(940.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 1400));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.ROOK,
						new Location(1127.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 2000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
						new Location(1322.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 3000));
		chessPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.KING,
						new Location(1514.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 6000));
		KFNekko.allies.clear();
		KFNekko.allies.add(KFNekko.player);
		KFNekko.allies.addAll(chessPieces);
	}

	@Override
	public void signalSpawn() {
		for (int i = 0; i < chessPieces.size; i++) {
			if (chessPieces.get(i).statusState != StatusState.DEAD) {
				chosenPiece = chessPieces.get(i);
				return;
			}
		}
		gameOver();
	}

	@Override
	public void drawChessPieceIcons(SpriteBatch batch) {
		for (int i = 0, x = 180; i < chessPieces.size; x += chessPieces.get(i).chessSprite.currentTexture.originalWidth * 0.25f, i++)
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
			KFNekko.camera.normalizeXSpeed = 10;
			// reset
			chessPieces.get(0).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(3);
			chessPieces.get(3).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 4: // "Queen"
			// reset
			chessPieces.get(3).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(4);
			chessPieces.get(4).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 5: // "Bishop"
			KFNekko.camera.normalizeXSpeed = 14;
			// reset
			chessPieces.get(4).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(1);
			chessPieces.get(1).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 6: // "Knight"
			KFNekko.camera.normalizeXSpeed = 6;
			// reset
			chessPieces.get(1).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(2);
			chessPieces.get(2).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 7: // "King"
			KFNekko.camera.normalizeXSpeed = 18;
			// reset
			chessPieces.get(2).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = chessPieces.get(5);
			chessPieces.get(5).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 8: // "Black Hole"
			// reset
			chessPieces.get(5).chessSprite.targetColor.set(Color.WHITE);

			KFNekko.camera.targetActor = null;
			KFNekko.camera.targetLoc.set(monsterLocs[0]);
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
