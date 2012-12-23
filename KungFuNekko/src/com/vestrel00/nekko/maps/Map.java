package com.vestrel00.nekko.maps;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.CuteMonster;
import com.vestrel00.nekko.actors.Monster;
import com.vestrel00.nekko.actors.components.Location;
import com.vestrel00.nekko.actors.states.CombatState;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.actors.states.StatusState;
import com.vestrel00.nekko.actors.states.VerticalMotionState;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.components.MapPieceGenerator;
import com.vestrel00.nekko.maps.components.MapSection;
import com.vestrel00.nekko.maps.components.Platform;

public class Map implements Updatable, Drawable {

	private Array<MapSection> sections;
	public Platform platform;
	public float width, height;
	private static Random rand;

	public Map() {
		MapPieceGenerator.init();
		sections = new Array<MapSection>();

		sections.add(new MapSection(0.0f, 0.0f, 1024.0f, 232.0f));

		// section 1
		sections.get(0).pieces.add(MapPieceGenerator.getPiece(
				MapPieceGenerator.BRIDGE_1, 0.0f, 0.0f));
		sections.get(0).pieces.add(MapPieceGenerator.getPiece(
				MapPieceGenerator.BRIDGE_1, 512.0f, 0.0f));

		platform = new Platform(sections);

		width = sections.get(sections.size - 1).rect.x
				+ sections.get(sections.size - 1).rect.width;
		height = sections.get(sections.size - 1).rect.y
				+ sections.get(sections.size - 1).rect.height;

		rand = new Random();
		spawnMonsters(5);
	}

	private void spawnMonsters(int amount) {
		for (int i = 0; i < amount; i++) {
			Location location = new Location((float) rand.nextInt((int) width),
					500.0f, 4.0f, 22.0f, 80.0f, 18.0f);
			Monster monster = new CuteMonster(KFNekko.resource.atlas, location,
					KFNekko.allies, 20, 100.0f, 240.0f, genColor(), 1);
			monster.setState(FaceState.RIGHT, StatusState.ALIVE,
					CombatState.IDLE, HorizontalMotionState.IDLE,
					VerticalMotionState.FALLING);
			location.setActor(monster);
			KFNekko.enemies.add(monster);
		}
	}

	private static Color genColor() {
		return new Color((float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f,
				(float) rand.nextInt(255) / 255.0f, 1.0f);
	}

	@Override
	public void update() {
		for (int i = 0; i < KFNekko.enemies.size; i++)
			KFNekko.enemies.get(i).update();
	}

	@Override
	public void draw(SpriteBatch batch) {
		for (int i = 0; i < sections.size; i++)
			sections.get(i).draw(batch);
		for (int i = 0; i < KFNekko.enemies.size; i++)
			KFNekko.enemies.get(i).draw(batch);
	}

}
