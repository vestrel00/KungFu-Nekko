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
import com.vestrel00.nekko.IntroMenuManager;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.Methods;
import com.vestrel00.nekko.actors.CuteMonster;
import com.vestrel00.nekko.actors.Monster;
import com.vestrel00.nekko.actors.SkullMonster;
import com.vestrel00.nekko.actors.TongueMonster;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.PowerUp;
import com.vestrel00.nekko.actors.components.Soda;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.interf.LevelHelper;
import com.vestrel00.nekko.interf.LevelManager;
import com.vestrel00.nekko.interf.PickUp;
import com.vestrel00.nekko.maps.components.MapSection;

public class LastStand implements LevelManager {

	private final long WAVE_DURATION = 30000000000L,
			POWER_UP_DROP_DELAY = 5000000000L, SODA_DROP_DELAY = 1000000000L;
	private final CharSequence scoreStr = "Score : ", waveStr = "Wave : ",
			highScoreStr = "HighScore : ", gameOverStr = "Game Over";
	private final int MAX_MONSTER_COUNT = 120, POWER_UP_DROP_CHANCE = 30,
			SODA_DROP_CHANCE = 30;

	public int score = 0, wave = 0, batchCount = 0, maxBatchCount, highscore,
			mapId;
	private float portalScale, portalRotation, portalScaleDir = 0.005f;
	private StringBuilder builder;
	private long waveStartTime, lastMonsterSpawnTime, lastInterSpawn,
			pauseTime, lastPowerUpDrop, lastSodaDrop, lastSpawnSound;
	public LastStandInst instruction;
	public LastStandHelper helper;
	private Color gameOverColor, targetGameOverColor;
	private Vector2 scoreStrVec, scoreVec, waveStrVec, waveVec,
			highScoreStrVec, highScoreVec;

	// cache to prevent creating new monster object at run time
	private Array<PickUp> pickUps;
	private Array<PowerUp> powerUps;
	private Array<Soda> sodas;
	private Array<CuteMonster> cuteMonsters;
	private Array<SkullMonster> skullMonsters;
	private Array<TongueMonster> tongueMonsters;

	// indices
	private int cuteMonsterIndex = 0, skullMonsterIndex = 0,
			tongueMonsterIndex = 0;

	@Override
	public void pause() {
		pauseTime = TimeUtils.nanoTime();
	}

	@Override
	public void saveHighScores() {
		switch (mapId) {
		case Map.MAP_1:
			KFNekko.settings.lastStand_1 = highscore;
			break;
		case Map.MAP_2:
			KFNekko.settings.lastStand_2 = highscore;
			break;
		case Map.MAP_3:
			KFNekko.settings.lastStand_3 = highscore;
			break;
		case Map.MAP_4:
			KFNekko.settings.lastStand_4 = highscore;
			break;
		}
		// commit settings (this may be a duplicate call)
		KFNekko.settings.commit();
	}

	@Override
	public void resume() {
		long resumeTime = TimeUtils.nanoTime();
		long timePassed = resumeTime - pauseTime;
		lastMonsterSpawnTime += timePassed;
		lastInterSpawn += timePassed;
		waveStartTime += timePassed;
	}

	public LastStand(Array<MapSection> sections, int mapId) {
		this.mapId = mapId;
		switch (mapId) {
		case Map.MAP_1:
			helper = new LSFinalFortress(sections, this);
			highscore = KFNekko.settings.lastStand_1;
			break;
		case Map.MAP_2:
			helper = new LSRoadOfMartyrs(sections, this);
			highscore = KFNekko.settings.lastStand_2;
			break;
		case Map.MAP_3:
			helper = new LSKingsAndQueens(sections, this);
			highscore = KFNekko.settings.lastStand_3;
			break;
		case Map.MAP_4:
			// TODO
			break;
		}

		// draw points
		scoreStrVec = new Vector2();
		scoreVec = new Vector2();
		waveStrVec = new Vector2();
		waveVec = new Vector2();
		highScoreStrVec = new Vector2();
		highScoreVec = new Vector2();

		// init the instruction manager
		instruction = new LastStandInst(this);

		// other
		KFNekko.targetWorldColor.set(0.7f, 0.7f, 0.7f, 1.0f);
		portalScale = 1.0f;
		portalRotation = 0.0f;
		builder = new StringBuilder();
		gameOverColor = new Color(Color.CLEAR);
		targetGameOverColor = new Color(Color.WHITE);

		// initialize the player
		KFNekko.player.nekkoSprite.attackAOE = true;

		// clear the list of enemies
		KFNekko.enemies.clear();
		// initialize reusable monsters
		initMonsters();
		// init pickups
		initPickUps();
		// attempt to clean up first
		System.gc();
	}

	private void initPickUps() {
		pickUps = new Array<PickUp>();
		powerUps = new Array<PowerUp>();
		sodas = new Array<Soda>();
		for (int i = 0; i < 10; i++) {
			powerUps.add(new PowerUp(PowerUp.INVISIBILITY, 20000000000L, false));
			powerUps.add(new PowerUp(PowerUp.QUICKFEET, 20000000000L, false));
			powerUps.add(new PowerUp(PowerUp.RAGE, 20000000000L, false));
			powerUps.add(new PowerUp(PowerUp.KUNGFU_MASTER, 20000000000L, false));
			powerUps.add(new PowerUp(PowerUp.ENDURANCE, 20000000000L, false));
			sodas.add(new Soda(Soda.HEALTH));
			sodas.add(new Soda(Soda.STAMINA));
			sodas.add(new Soda(Soda.MIX));
			sodas.add(new Soda(Soda.HEALTH));
			sodas.add(new Soda(Soda.STAMINA));
			sodas.add(new Soda(Soda.MIX));
		}
	}

	@Override
	public void update() {
		updatePortals();
		for (int i = 0; i < pickUps.size; i++)
			pickUps.get(i).update();
		for (int i = 0; i < helper.chessPieces.size; i++)
			helper.chessPieces.get(i).update();

		// update text positions
		scoreStrVec.set(KFNekko.camera.rect.x + 256.0f,
				KFNekko.camera.rect.y + 308.0f);
		scoreVec.set(KFNekko.camera.rect.x + 310.0f,
				KFNekko.camera.rect.y + 308.0f);
		waveStrVec.set(KFNekko.camera.rect.x + 156.0f,
				KFNekko.camera.rect.y + 308.0f);
		waveVec.set(KFNekko.camera.rect.x + 208.0f,
				KFNekko.camera.rect.y + 308.0f);
		highScoreStrVec.set(KFNekko.camera.rect.x + 180.0f,
				KFNekko.camera.rect.y + 54.0f);
		highScoreVec.set(KFNekko.camera.rect.x + 270.0f,
				KFNekko.camera.rect.y + 54.0f);

		// update high score
		if (score > highscore)
			highscore = score;

		// case game over
		if (KFNekko.view == KFNekko.VIEW_GAME_OVER) {
			Methods.updateColor(gameOverColor, targetGameOverColor, 0.01f);
			return;
		}

		if (KFNekko.view == KFNekko.VIEW_LEVEL_INTRO) {
			instruction.update();
			return;
		}

		if (TimeUtils.nanoTime() - waveStartTime > WAVE_DURATION) {
			waveStartTime = TimeUtils.nanoTime();
			wave++;
			helper.setMonsterSpawnDelay(wave);
			// drop soda in front of each piece if alive
			for (int i = 0; i < helper.chessPieces.size; i++)
				if (helper.chessPieces.get(i).statusState == StatusState.ALIVE)
					dropSoda(helper.chessPieces.get(i).location);
		}

		if (TimeUtils.nanoTime() - lastMonsterSpawnTime > helper.monsterSpawnDelay) {
			lastMonsterSpawnTime = TimeUtils.nanoTime();
			lastInterSpawn = TimeUtils.nanoTime() - helper.interSpawnDelay;
			batchCount = 0;

			// signal start spawn stream of monsters (3 per batch)
			// set the chosen target piece and chosen spawn location
			helper.signalSpawn();
		}

		if (TimeUtils.nanoTime() - lastInterSpawn > helper.interSpawnDelay
				&& batchCount < helper.maxBatchCount) {
			lastInterSpawn = TimeUtils.nanoTime();
			spawnMonster(helper.rand.nextInt(3));
			batchCount++;
		}

		// update enemies
		for (int i = 0; i < KFNekko.enemies.size; i++)
			KFNekko.enemies.get(i).update();
	}

	private void updatePortals() {
		portalScale += portalScaleDir;
		if (portalScale > 1.2f || portalScale < 0.9f)
			portalScaleDir = -portalScaleDir;
		if ((portalRotation -= 1.0f) < 0.0f)
			portalRotation += 360.0f;
	}

	private void spawnMonster(int type) {
		Monster monster = null;
		switch (type) {
		case 0:
			monster = cuteMonsters.get(cuteMonsterIndex);
			monster.location.speed.maxXSpeed = 4.0f + (float) helper.difficulty
					* wave * 0.15f;
			if (++cuteMonsterIndex == cuteMonsters.size)
				cuteMonsterIndex = 0;
			break;
		case 1:
			monster = skullMonsters.get(skullMonsterIndex);
			monster.location.speed.maxXSpeed = 1.0f + (float) helper.difficulty
					* wave * 0.1f;
			if (++skullMonsterIndex == skullMonsters.size)
				skullMonsterIndex = 0;
			break;
		case 2:
			monster = tongueMonsters.get(tongueMonsterIndex);
			monster.location.speed.maxXSpeed = 2.0f + (float) helper.difficulty
					* wave * 0.15f;
			if (++tongueMonsterIndex == tongueMonsters.size)
				tongueMonsterIndex = 0;
			break;
		}
		monster.setState(FaceState.RIGHT, StatusState.ALIVE, CombatState.IDLE,
				HorizontalMotionState.IDLE, VerticalMotionState.FALLING);
		// reset
		monster.reset(helper.difficulty * wave);
		monster.reset(helper.chosenSpawnLoc.x, helper.chosenSpawnLoc.y);
		// set speed
		genColor(monster.sprite.color);
		monster.setPrimeTarget(helper.chosenPiece);
		// do not exceed maximum monster count (LIFO - Queue)
		if (KFNekko.enemies.size >= MAX_MONSTER_COUNT)
			KFNekko.enemies.removeIndex(0);
		KFNekko.enemies.add(monster); // add to size - 1 (end of list)
		if (TimeUtils.nanoTime() - lastSpawnSound > 200000000L
				&& KFNekko.camera.rect.contains(helper.chosenSpawnLoc.x,
						helper.chosenSpawnLoc.y)) {
			lastSpawnSound = TimeUtils.nanoTime();
			KFNekko.audio.spawn(helper.chosenSpawnLoc.x);
		}
	}

	private void initMonsters() {
		cuteMonsters = new Array<CuteMonster>();
		skullMonsters = new Array<SkullMonster>();
		tongueMonsters = new Array<TongueMonster>();
		for (int i = 0; i < 150; i++) {
			CuteMonster cuteMon = new CuteMonster(KFNekko.resource.atlas,
					new Location(0, 0, 4.0f, 22.0f, 80.0f, 18.0f),
					KFNekko.allies, KFNekko.map.width, KFNekko.map.width,
					new Color(Color.WHITE), 1, true);
			cuteMon.setState(FaceState.RIGHT, StatusState.DEAD,
					CombatState.IDLE, HorizontalMotionState.IDLE,
					VerticalMotionState.FALLING);
			cuteMonsters.add(cuteMon);
			SkullMonster skullMon = new SkullMonster(KFNekko.resource.atlas,
					new Location(0, 0, 4.0f, 22.0f, 80.0f, 18.0f),
					KFNekko.allies, KFNekko.map.width, KFNekko.map.width,
					new Color(Color.WHITE), 1, true);
			skullMon.setState(FaceState.RIGHT, StatusState.DEAD,
					CombatState.IDLE, HorizontalMotionState.IDLE,
					VerticalMotionState.FALLING);
			skullMonsters.add(skullMon);
			// flying so ySpeed should be less than normal
			TongueMonster tongueMon = new TongueMonster(KFNekko.resource.atlas,
					new Location(0, 0, 4.0f, 8.0f, 80.0f, 18.0f),
					KFNekko.allies, KFNekko.map.width, KFNekko.map.width,
					new Color(Color.WHITE), 1, true);
			tongueMon.setState(FaceState.RIGHT, StatusState.DEAD,
					CombatState.IDLE, HorizontalMotionState.IDLE,
					VerticalMotionState.FALLING);
			tongueMonsters.add(tongueMon);
		}
	}

	private void genColor(Color color) {
		switch (helper.rand.nextInt(3)) {
		case 0: // no red
			color.set(0.0f, (float) helper.rand.nextInt(255) / 255.0f,
					(float) helper.rand.nextInt(255) / 255.0f, 1.0f);
		case 1: // no green
			color.set((float) helper.rand.nextInt(255) / 255.0f, 0.0f,
					(float) helper.rand.nextInt(255) / 255.0f, 1.0f);
		default: // no blue
			color.set((float) helper.rand.nextInt(255) / 255.0f,
					(float) helper.rand.nextInt(255) / 255.0f, 0.0f, 1.0f);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// draw the spawn locations
		for (int i = 0; i < helper.spawnPortals.length; i++)
			if (helper.spawnPortals[i].overlaps(KFNekko.camera.rect))
				batch.draw(helper.spawnRegion, helper.spawnPortals[i].x,
						helper.spawnPortals[i].y,
						helper.spawnPortals[i].width * 0.5f,
						helper.spawnPortals[i].height * .5f,
						helper.spawnPortals[i].width,
						helper.spawnPortals[i].height, portalScale,
						portalScale, portalRotation);

		// draw the chess pieces
		for (int i = 0; i < helper.chessPieces.size; i++)
			helper.chessPieces.get(i).draw(batch);
		batch.setColor(Color.WHITE);
		// draw pickups
		for (int i = 0; i < pickUps.size; i++)
			pickUps.get(i).draw(batch);
		// draw the chess pieces as icons from right side of screen to left
		helper.drawChessPieceIcons(batch);
	}

	@Override
	public void drawText(SpriteBatch batch) {
		switch (KFNekko.view) {
		case KFNekko.VIEW_GAME_OVER:
			batch.begin();
			drawScores(batch);
			KFNekko.resource.chunkFive.setColor(gameOverColor);
			KFNekko.resource.chunkFive.setScale(1.0f);
			KFNekko.resource.chunkFive.draw(batch, gameOverStr,
					KFNekko.camera.rect.x + 154.0f,
					KFNekko.camera.rect.y + 280.0f);
			batch.end();
			break;
		case KFNekko.VIEW_GAME:
			batch.begin();
			drawScores(batch);
			batch.end();
			break;
		case KFNekko.VIEW_LEVEL_INTRO:
			instruction.draw(batch);
			break;
		}
	}

	private void drawScores(SpriteBatch batch) {
		// DRAW SCORE
		KFNekko.resource.arial.setColor(Color.WHITE);
		KFNekko.resource.arial.setScale(1.0f);
		KFNekko.resource.arial.draw(batch, scoreStr, scoreStrVec.x,
				scoreStrVec.y);
		// to avoid heap allocation - no String.valueOf()
		builder.delete(0, builder.length());
		builder.append(score);
		KFNekko.resource.arial.draw(batch, builder, scoreVec.x, scoreVec.y);
		// DRAW WAVE
		KFNekko.resource.arial.draw(batch, waveStr, waveStrVec.x, waveStrVec.y);
		// to avoid heap allocation - no String.valueOf()
		builder.delete(0, builder.length());
		builder.append(wave);
		KFNekko.resource.arial.draw(batch, builder, waveVec.x, waveVec.y);
		// draw high score
		KFNekko.resource.arial.draw(batch, highScoreStr, highScoreStrVec.x,
				highScoreStrVec.y);
		builder.delete(0, builder.length());
		builder.append(highscore);
		KFNekko.resource.arial.draw(batch, builder, highScoreVec.x,
				highScoreVec.y);
	}

	@Override
	public void monsterDown(Monster monster) {
		score += monster.level;
		// power up
		if (pickUps.size == 30)
			pickUps.removeIndex(0);
		if (TimeUtils.nanoTime() - lastPowerUpDrop > POWER_UP_DROP_DELAY
				&& helper.rand.nextInt(100) < POWER_UP_DROP_CHANCE) {
			lastPowerUpDrop = TimeUtils.nanoTime();
			PowerUp power = powerUps.get(helper.rand.nextInt(powerUps.size));
			power.drop(monster.location);
			pickUps.add(power);
		}
		// soda
		if (pickUps.size == 30)
			pickUps.removeIndex(0);
		if ((monster.location.onPlatform || monster.location.onSlope)
				&& TimeUtils.nanoTime() - lastSodaDrop > SODA_DROP_DELAY
				&& helper.rand.nextInt(100) < SODA_DROP_CHANCE) {
			lastSodaDrop = TimeUtils.nanoTime();
			dropSoda(monster.location);

		}

	}

	private void dropSoda(Location location) {
		Soda soda = sodas.get(helper.rand.nextInt(sodas.size));
		soda.drop(location);
		pickUps.add(soda);
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		switch (KFNekko.view) {
		case KFNekko.VIEW_LEVEL_INTRO:
			return instruction.onTouchDown(x, y);
		case KFNekko.VIEW_GAME_OVER:
			if (gameOverColor.a > 0.9f) {
				// reset previous views
				KFNekko.view = KFNekko.VIEW_INTRO;
				KFNekko.intro.menu.view = IntroMenuManager.VIEW_MENU;
				KFNekko.intro.menu.nextView = IntroMenuManager.VIEW_MENU;
				KFNekko.audio.touch();
				KFNekko.map.manager.resume();
				// move camera back to origin
				KFNekko.camera.targetActor = null;
				KFNekko.camera.targetLoc.x = -1.0f;
				KFNekko.camera.reset();
				// reset the intro states!
				KFNekko.intro.reset();
				// reset the player!
				KFNekko.player.reset(0, 0);
				return true;

			}
			return true;
		}
		return false;
	}

	@Override
	public LevelHelper getHelper() {
		return helper;
	}

	@Override
	public void drawSecondary(SpriteBatch batch) {
		// TODO Auto-generated method stub
		
	}
}
