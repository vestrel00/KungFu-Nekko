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

public class CIAFrontLines extends CatsInArmsHelper {

	private NekkoAI catTmp = null;

	public CIAFrontLines(Array<MapSection> sections, CatsInArms cia) {
		super(sections, cia);
		// all map sections will start at (0, 0)
		MapSection section1 = new MapSection(0.0f, 0.0f, 832.0f, 576.0f);
		MapSection section2 = new MapSection(0.0f, 0.0f, 768.0f, 576.0f);
		MapSection section3 = new MapSection(0.0f, 0.0f, 832.0f, 576.0f);
		MapSection section4 = new MapSection(0.0f, 0.0f, 768.0f, 576.0f);
		sections.add(section1);
		sections.add(section2);
		sections.add(section3);
		sections.add(section4);

		// SECTION 1
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 7, 2, 0, 0));
		section1.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 448, 0));
		for (int i = 0, x = 160; i < 4; i++, x += 192)
			section1.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		// SECTION 2
		section2.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 0, 0));
		section2.pieces.add(MapPieceGenerator.genQuads(false, 0, 7, 2, 384, 0));
		for (int i = 0, x = 96; i < 3; i++, x += 192)
			section2.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		section2.pieces.add(MapPieceGenerator.genSingle(10, 576, 128));
		section2.pieces.add(MapPieceGenerator.genSingle(9, 640, 128));
		section2.pieces.add(MapPieceGenerator.genSingle(11, 704, 128));
		section2.translate(832, 0);

		// SECTION 3
		section3.pieces.add(MapPieceGenerator.genQuads(false, 0, 7, 2, 0, 0));
		section3.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 448, 0));
		section3.pieces.add(MapPieceGenerator.genSingle(10, 0, 128));
		section3.pieces.add(MapPieceGenerator.genSingle(9, 64, 128));
		section3.pieces.add(MapPieceGenerator.genSingle(11, 128, 128));
		for (int i = 0, x = 256; i < 3; i++, x += 192)
			section3.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		section3.translate(1600, 0);
		// SECTION 4
		section4.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 0, 0));
		section4.pieces.add(MapPieceGenerator.genQuads(false, 0, 6, 2, 384, 0));
		for (int i = 0, x = 0; i < 4; i++, x += 192)
			section4.pieces.add(MapPieceGenerator.genSingle(5, x, 128));
		section4.translate(2432, 0);

		KFNekko.map.width = 3200.0f;
		KFNekko.map.height = 576.0f;
		KFNekko.background.setOffsetRatio();

		// initialize the player
		KFNekko.player.maxHealth = 1000;
		KFNekko.player.reset(1300.0f, 500.0f);
		KFNekko.player.setState(FaceState.LEFT, StatusState.ALIVE,
				CombatState.IDLE, HorizontalMotionState.IDLE,
				VerticalMotionState.FALLING);

		// camera work
		KFNekko.camera.targetActor = KFNekko.player;
		KFNekko.camera.mode = Camera.MODE_SCROLL;
		KFNekko.camera.normalizeXSpeed = 6;
		KFNekko.camera.normalizeYSpeed = 1;

		// init the pieces
		initAllyChessPieces();
		initEnemyChessPieces();
		// init the cats!
		initCats();

		// initialize the spawn locations
		spawnLocs = new Vector2[2];
		spawnLocs[0] = new Vector2(160.0f, 307.0f);
		spawnLocs[1] = new Vector2(3038.0f, 307.0f);
		// only have 1 so (this is for monsters)
		chosenSpawnLoc = spawnLocs[1];

		//
		difficulty = 20;

		// init spawn rectangles for checking visibility
		initSpawnPortals();
	}

	private void initEnemyChessPieces() {
		enemyPieces = new Array<ChessPiece>();
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.PAWN,
						new Location(2152.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 1000));
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.BISHOP,
						new Location(2344.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 1500));
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.KNIGHT,
						new Location(2540.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 2000));
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.ROOK,
						new Location(2727.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 3000));
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.QUEEN,
						new Location(2922.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 3000));
		enemyPieces
				.add(new ChessPiece(KFNekko.allies, ChessPiece.KING,
						new Location(3114.0f, 128.0f, 0, 0, 0, 0),
						FaceState.LEFT, 5000));
		for (int i = 0; i < enemyPieces.size; i++)
			enemyPieces.get(i).attackAnytime = true;
		KFNekko.enemies.clear();
		KFNekko.enemies.addAll(enemyPieces);
		// save the staticEnemyCount
		staticEnemyCount = KFNekko.enemies.size;
	}

	private void initAllyChessPieces() {
		allyPieces = new Array<ChessPiece>();
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.PAWN,
						new Location(1050.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 400));
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.BISHOP,
						new Location(856.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 600));
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.KNIGHT,
						new Location(660.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 1000));
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.ROOK,
						new Location(472.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 2000));
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.QUEEN,
						new Location(278.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 3000));
		allyPieces
				.add(new ChessPiece(KFNekko.enemies, ChessPiece.KING,
						new Location(88.0f, 128.0f, 0, 0, 0, 0),
						FaceState.RIGHT, 5000));
		KFNekko.allies.clear();
		KFNekko.allies.add(KFNekko.player);
		KFNekko.allies.addAll(allyPieces);
		// save the staticAllyCount
		staticAllyCount = KFNekko.allies.size;
	}

	@Override
	public void drawChessPieceIcons(SpriteBatch batch) {
		for (int i = 0, x = 180; i < allyPieces.size; x += allyPieces.get(i).chessSprite.currentTexture.originalWidth * 0.25f, i++)
			allyPieces.get(i).chessSprite.drawIcon(batch, x, 260.0f, 0.15f);
	}

	@Override
	public void setCamera(int instructionIndex) {
		switch (instructionIndex) {
		case 1: // "Your Structures"
			KFNekko.camera.normalizeXSpeed = 18;
			KFNekko.camera.targetActor = allyPieces.get(4);
			for (int i = 0; i < allyPieces.size; i++)
				allyPieces.get(i).chessSprite.targetColor.set(Color.BLUE);
			break;
		case 2: // "Enemy Structures"
			KFNekko.camera.targetActor = KFNekko.enemies.get(3);
			for (int i = 0; i < allyPieces.size; i++)
				allyPieces.get(i).chessSprite.targetColor.set(Color.WHITE);

			for (int i = 0; i < enemyPieces.size; i++)
				enemyPieces.get(i).chessSprite.targetColor.set(Color.RED);

			break;
		case 3: // "Red Cat"
			for (int i = 0; i < enemyPieces.size; i++)
				enemyPieces.get(i).chessSprite.targetColor.set(Color.WHITE);
			catTmp = spawnCat(1);
			catTmp.location.speed.maxXSpeed = 0;
			cia.catnip = 10;
			KFNekko.camera.targetActor = catTmp;
			break;
		case 4: // "Black Cat"
			catTmp.onDeath();
			catTmp = spawnCat(2);
			catTmp.location.speed.maxXSpeed = 0;
			cia.catnip = 10;
			KFNekko.camera.targetActor = catTmp;
			break;
		case 5: // "White Cat"
			catTmp.onDeath();
			catTmp = spawnCat(4);
			catTmp.location.speed.maxXSpeed = 0;
			cia.catnip = 10;
			KFNekko.camera.targetActor = catTmp;
			break;
		case 6: // "Blue Cat"
			catTmp.onDeath();
			catTmp = spawnCat(3);
			catTmp.location.speed.maxXSpeed = 0;
			cia.catnip = 10;
			KFNekko.camera.targetActor = catTmp;
			break;
		case 7: // "Cyan Cat"
			catTmp.onDeath();
			catTmp = spawnCat(5);
			catTmp.location.speed.maxXSpeed = 0;
			cia.catnip = 10;
			KFNekko.camera.targetActor = catTmp;
			break;
		case 8: // "Black Hole"
			catTmp.onDeath();
			KFNekko.camera.targetActor = null;
			KFNekko.camera.targetLoc.set(spawnLocs[0]);
			break;
		}

	}

}
