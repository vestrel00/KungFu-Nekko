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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.CuteMonster;
import com.vestrel00.nekko.actors.Monster;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.components.PowerUp;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.interf.LevelManager;
import com.vestrel00.nekko.interf.PickUp;
import com.vestrel00.nekko.maps.components.MapPieceGenerator;
import com.vestrel00.nekko.maps.components.MapSection;

public class LastStand implements LevelManager {

	private static final long WAVE_DURATION = 60000000000L,
			INTER_SPAWN = 1000000000L, POWER_UP_DROP_DELAY = 5000000000L;

	private final CharSequence scoreStr = "Score : ", waveStr = "Wave : ";
	private final int MAX_MONSTER_COUNT = 150, POWER_UP_DROP_CHANCE = 10;
	private int score = 0, wave = 0, batchCount = 0;
	private StringBuilder builder;
	private Vector2 monsterLoc1, monsterLoc2, monsterLoc3, defenseLoc1,
			defenseLoc2, defenseLoc3, scoreStrVec, scoreVec, waveStrVec,
			waveVec, chosenSpawnLoc, chosenTargetLoc;
	private long waveStartTime, monsterSpawnDelay = 1000000000L,
			lastMonsterSpawnTime, lastInterSpawn, pauseTime, lastPowerUpDrop;
	private Random rand;

	// cache to prevent creating new monster object at run time
	private Array<PickUp> pickUps;
	private Array<PowerUp> powerUps;
	private Array<CuteMonster> cuteMonsters;

	// indices
	private int cuteMonsterIndex = 0;

	// TODO TOUNGE, SKULL
	// TODO POWER UPS AND SUPPLIES

	@Override
	public void pause() {
		pauseTime = TimeUtils.nanoTime();
	}

	@Override
	public void resume() {
		long resumeTime = TimeUtils.nanoTime();
		long timePassed = resumeTime - pauseTime;
		lastMonsterSpawnTime += timePassed;
		lastInterSpawn += timePassed;
		waveStartTime += timePassed;
	}

	public LastStand(Array<MapSection> sections) {

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
		KFNekko.map.height = 1664.0f;

		// set player spawn location
		KFNekko.player.location.x = 1234.0f;
		KFNekko.player.location.y = 892.0f;

		// other
		rand = new Random();
		builder = new StringBuilder();

		// initialize monster spawn locations
		monsterLoc1 = new Vector2(140.0f, 244.0f);
		monsterLoc2 = new Vector2(1851.0f, 1308.0f);
		monsterLoc3 = new Vector2(1851.0f, 1576.0f);

		// init defense points
		defenseLoc1 = new Vector2(1851.0f, 572.0f);
		defenseLoc2 = new Vector2(1077.0f, 1277.0f);
		defenseLoc3 = new Vector2(884.0f, 1514.0f);

		// draw points
		scoreStrVec = new Vector2();
		scoreVec = new Vector2();
		waveStrVec = new Vector2();
		waveVec = new Vector2();

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
		for (int i = 0; i < 6; i++) {
			powerUps.add(new PowerUp(PowerUp.INVISIBILITY));
			powerUps.add(new PowerUp(PowerUp.QUICKFEET));
			powerUps.add(new PowerUp(PowerUp.RAGE));
			powerUps.add(new PowerUp(PowerUp.KUNGFU_MASTER));
			powerUps.add(new PowerUp(PowerUp.ENDURANCE));
		}
		// TODO FOOD
	}

	@Override
	public void update() {
		for (int i = 0; i < pickUps.size; i++)
			pickUps.get(i).update();

		if (TimeUtils.nanoTime() - waveStartTime > WAVE_DURATION) {
			waveStartTime = TimeUtils.nanoTime();
			wave++;
			if ((monsterSpawnDelay = 20000000000L - (long) (wave / 5) * 1000000000L) < 1000000000L)
				monsterSpawnDelay = 1000000000L;
			// TODO spawn some supplies and power ups
		}

		if (TimeUtils.nanoTime() - lastMonsterSpawnTime > monsterSpawnDelay) {
			lastMonsterSpawnTime = TimeUtils.nanoTime();
			lastInterSpawn = TimeUtils.nanoTime() - INTER_SPAWN;
			batchCount = 0;
			// signal start spawn stream of monsters (3 per batch)
			switch (rand.nextInt(3)) {
			case 0:
				chosenSpawnLoc = monsterLoc1;
				chosenTargetLoc = defenseLoc1;
				break;
			case 1:
				chosenSpawnLoc = monsterLoc2;
				chosenTargetLoc = defenseLoc2;
				break;
			case 2:
				chosenSpawnLoc = monsterLoc3;
				chosenTargetLoc = defenseLoc3;
				break;
			}

		}

		if (TimeUtils.nanoTime() - lastInterSpawn > INTER_SPAWN
				&& batchCount < 3) {
			lastInterSpawn = TimeUtils.nanoTime();
			spawnMonster(rand.nextInt(3), wave, chosenSpawnLoc, chosenTargetLoc);
			batchCount++;
		}

		scoreStrVec.set(KFNekko.camera.rect.x + 256.0f,
				KFNekko.camera.rect.y + 308.0f);
		scoreVec.set(KFNekko.camera.rect.x + 310.0f,
				KFNekko.camera.rect.y + 308.0f);
		waveStrVec.set(KFNekko.camera.rect.x + 156.0f,
				KFNekko.camera.rect.y + 308.0f);
		waveVec.set(KFNekko.camera.rect.x + 208.0f,
				KFNekko.camera.rect.y + 308.0f);
	}

	private void spawnMonster(int type, int level, Vector2 spawnLoc,
			Vector2 targetLoc) {
		Monster monster = null;
		switch (type) {
		case 0:
			monster = cuteMonsters.get(cuteMonsterIndex);
			monster.location.speed.maxXSpeed = 2.0f + (float) wave * 0.08f;
			if (++cuteMonsterIndex == cuteMonsters.size)
				cuteMonsterIndex = 0;
			break;
		case 1:
			// TODO CHANGE TO TOUNGE
			monster = cuteMonsters.get(cuteMonsterIndex);
			monster.location.speed.maxXSpeed = 2.0f + (float) wave * 0.08f;
			if (++cuteMonsterIndex == cuteMonsters.size)
				cuteMonsterIndex = 0;
			break;
		case 2:
			// TODO CHANGE TO SKULL
			monster = cuteMonsters.get(cuteMonsterIndex);
			monster.location.speed.maxXSpeed = 2.0f + (float) wave * 0.08f;
			if (++cuteMonsterIndex == cuteMonsters.size)
				cuteMonsterIndex = 0;
			break;
		}
		monster.reset(level);
		monster.setState(FaceState.RIGHT, StatusState.ALIVE, CombatState.IDLE,
				HorizontalMotionState.IDLE, VerticalMotionState.FALLING);
		// set spawn location
		monster.location.spawnX = spawnLoc.x;
		monster.location.spawnY = spawnLoc.y;
		// set current location
		monster.location.x = spawnLoc.x;
		monster.location.y = spawnLoc.y;
		// set speed
		genColor(monster.sprite.color);
		monster.setAbsoluteTargetLoc(targetLoc);
		// do not exceed maximum monster count (LIFO - Queue)
		if (KFNekko.enemies.size >= MAX_MONSTER_COUNT)
			KFNekko.enemies.removeIndex(0);
		KFNekko.enemies.add(monster); // add to size - 1 (end of list)
	}

	private void initMonsters() {
		cuteMonsters = new Array<CuteMonster>();
		for (int i = 0; i < 150; i++) {
			Location location = null;
			// Monster.MONSTER_CUTE:
			location = new Location(0, 0, 4.0f, 22.0f, 80.0f, 18.0f);
			CuteMonster monster = new CuteMonster(KFNekko.resource.atlas,
					location, KFNekko.allies, 100.0f, KFNekko.map.width,
					new Color(Color.WHITE), 1);
			monster.setState(FaceState.RIGHT, StatusState.DEAD,
					CombatState.IDLE, HorizontalMotionState.IDLE,
					VerticalMotionState.FALLING);
			location.setActor(monster);
			cuteMonsters.add(monster);
			// TODO Monster.MONSTER_SKULL:
			// TODO Monster.MONSTER_TOUNGE:
		}
	}

	private void genColor(Color color) {
		switch (rand.nextInt(3)) {
		case 0: // no red
			color.set(0.0f, (float) rand.nextInt(255) / 255.0f,
					(float) rand.nextInt(255) / 255.0f, 1.0f);
		case 1: // no green
			color.set((float) rand.nextInt(255) / 255.0f, 0.0f,
					(float) rand.nextInt(255) / 255.0f, 1.0f);
		default: // no blue
			color.set((float) rand.nextInt(255) / 255.0f,
					(float) rand.nextInt(255) / 255.0f, 0.0f, 1.0f);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		// DRAW SCORE
		KFNekko.resource.arial.setColor(Color.WHITE);
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
		batch.end();
		// draw pickups
		batch.begin();
		for (int i = 0; i < pickUps.size; i++)
			pickUps.get(i).draw(batch);
		batch.end();
	}

	@Override
	public void monsterDown(Monster monster) {
		score += monster.level;
		// power up
		if (pickUps.size == 20)
			pickUps.removeIndex(0);
		if (TimeUtils.nanoTime() - lastPowerUpDrop > POWER_UP_DROP_DELAY
				&& rand.nextInt(100) < POWER_UP_DROP_CHANCE) {
			PowerUp power = powerUps.get(rand.nextInt(powerUps.size));
			power.drop(monster.location);
			pickUps.add(power);

		}
		// TODO FOOD pickup
	}
}
