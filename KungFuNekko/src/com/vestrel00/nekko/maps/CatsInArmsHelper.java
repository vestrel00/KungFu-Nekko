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
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.ChessPiece;
import com.vestrel00.nekko.actors.NekkoAI;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.interf.CombatStateManager;
import com.vestrel00.nekko.interf.LevelHelper;
import com.vestrel00.nekko.maps.components.MapSection;

public abstract class CatsInArmsHelper implements LevelHelper {

	public AtlasRegion spawnRegion;
	public Array<ChessPiece> allyPieces, enemyPieces;
	public ChessPiece chosenPiece;
	public Rectangle[] spawnPortals;
	public Vector2[] spawnLocs;
	public Vector2 chosenSpawnLoc;
	public Random rand;
	public long monsterSpawnDelay, lastSpawnTime;
	public int difficulty, staticAllyCount, staticEnemyCount, catsIndex = 0;
	protected CatsInArms cia;
	protected Array<NekkoAI> cats;

	public CatsInArmsHelper(Array<MapSection> sections, CatsInArms cia) {
		this.cia = cia;
		rand = new Random();
		spawnRegion = KFNekko.resource.atlas.findRegion("blackHole");
		monsterSpawnDelay = 4000000000L;
	}

	protected void initSpawnPortals() {
		float halfWidth = (float) spawnRegion.originalWidth * 0.5f, halfHeight = (float) spawnRegion.originalHeight * 0.5f;
		spawnPortals = new Rectangle[spawnLocs.length];
		for (int i = 0; i < spawnLocs.length; i++)
			spawnPortals[i] = new Rectangle(spawnLocs[i].x - halfWidth,
					spawnLocs[i].y - halfHeight,
					(float) spawnRegion.originalWidth,
					(float) spawnRegion.originalHeight);
	}

	protected void initCats() {
		cats = new Array<NekkoAI>();
		for (int i = 0; i < 50; i++) {
			NekkoAI cat = new NekkoAI(KFNekko.resource.atlas, new Location(0,
					0, 4.0f, 22.0f, 80.0f, 18.0f), KFNekko.enemies, 10,
					new Color(Color.WHITE), 20.0f, KFNekko.map.width);
			cat.primaryFaceState = FaceState.RIGHT;
			cats.add(cat);
		}
	}

	/**
	 * <ul>
	 * <b>catType</b>
	 * <li>red (LIGHTNINGKICKS) : 1</li>
	 * <li>black (SPEEDYHANDS) : 2</li>
	 * <li>blue (BEATDOWN) : 3</li>
	 * <li>pink (GATTLINGSHOT) : 4</li>
	 * <li>cyan (SPEEDYHANDS) : 5</li>
	 * </ul>
	 */
	@Override
	public NekkoAI spawnCat(int catType) {
		// only allow to spawn a cat every second
		if (KFNekko.view != KFNekko.VIEW_LEVEL_INTRO)
			if (TimeUtils.nanoTime() - lastSpawnTime < 1000000000L)
				return null;
		lastSpawnTime = TimeUtils.nanoTime();
		// check if enough catnip
		switch (catType) {
		case 1:
		case 4:
			if (cia.catnip < 3)
				return null;
			cia.catnip -= 3;
			break;
		case 2:
		case 5:
			if (cia.catnip < 2)
				return null;
			cia.catnip -= 2;
			break;
		case 3:
			if (cia.catnip < 1)
				return null;
			cia.catnip -= 1;
			break;
		}

		if (++catsIndex == cats.size)
			catsIndex = 0;
		NekkoAI cat = cats.get(catsIndex);

		// do a reset of everything first
		cat.nekkoSprite.damageMultiplier = 1;
		cat.location.speed.maxXSpeed = 2.0f;
		cat.nekkoSprite.speedUp = 0L;
		cat.aggroRange = 20.0f;
		cat.nekkoSprite.color.set(Color.WHITE);
		cat.setState(FaceState.RIGHT, StatusState.ALIVE, CombatState.IDLE,
				HorizontalMotionState.MOVING, VerticalMotionState.FALLING);
		switch (catType) {
		case 1:
			cat.maxHealth = 60;
			cat.nekkoSprite.damageMultiplier = 2;
			cat.primaryCombo = CombatStateManager.INPUT_UP;
			cat.nekkoSprite.targetColor.set(Color.RED);
			break;
		case 2:
			cat.maxHealth = 80;
			cat.location.speed.maxXSpeed = 3.0f;
			cat.nekkoSprite.speedUp = 20000000L;
			cat.primaryCombo = CombatStateManager.INPUT_DOWN;
			cat.nekkoSprite.targetColor.set(Color.DARK_GRAY);
			break;
		case 3:
			cat.maxHealth = 30;
			cat.location.speed.maxXSpeed = 4.0f;
			cat.primaryCombo = CombatStateManager.INPUT_RIGHT;
			cat.nekkoSprite.targetColor.set(Color.BLUE);
			break;
		case 4:
			cat.maxHealth = 60;
			cat.primaryCombo = CombatStateManager.INPUT_LEFT;
			cat.nekkoSprite.targetColor.set(Color.PINK);
			cat.aggroRange = 140.0f;
			break;
		case 5:
			cat.maxHealth = 120;
			cat.health = cat.maxHealth;
			cat.primaryCombo = CombatStateManager.INPUT_DOWN;
			cat.nekkoSprite.targetColor.set(Color.CYAN);
			cat.nekkoSprite.speedUp = -20000000L;
			cat.location.speed.maxXSpeed = 1.0f;
			break;
		}
		cat.health = cat.maxHealth;
		cat.reset(spawnLocs[0].x, spawnLocs[0].y);
		KFNekko.allies.add(cat);
		if (KFNekko.allies.size > 60)
			KFNekko.allies.removeIndex(staticAllyCount);
		return cat;
	}

	@Override
	public void gameWon() {
		if (KFNekko.view != KFNekko.VIEW_GAME_VICTORY) {
			cia.saveHighScores();
			KFNekko.view = KFNekko.VIEW_GAME_VICTORY;
			KFNekko.targetWorldColor.set(Color.GRAY);
			if (KFNekko.audio.music.isPlaying())
				KFNekko.audio.music.stop();
			if (KFNekko.settings.soundOn)
				KFNekko.audio.victory.play();
		}
	}

	@Override
	public void gameOver() {
		if (KFNekko.view != KFNekko.VIEW_GAME_OVER) {
			cia.saveHighScores();
			KFNekko.view = KFNekko.VIEW_GAME_OVER;
			KFNekko.targetWorldColor.set(Color.GRAY);
			if (KFNekko.audio.music.isPlaying())
				KFNekko.audio.music.stop();
			if (KFNekko.settings.soundOn)
				KFNekko.audio.gameOver.play();
		}
	}

	public abstract void drawChessPieceIcons(SpriteBatch batch);

	public abstract void setCamera(int instructionIndex);

}
