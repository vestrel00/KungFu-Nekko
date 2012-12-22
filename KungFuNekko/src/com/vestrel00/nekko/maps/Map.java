package com.vestrel00.nekko.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;
import com.vestrel00.nekko.maps.components.MapPieceGenerator;
import com.vestrel00.nekko.maps.components.MapSection;
import com.vestrel00.nekko.maps.components.Platform;

public class Map implements Updatable, Drawable {

	private Array<MapSection> sections;
	public Platform platform;
	public float width, height;

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
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(SpriteBatch batch) {
		for (int i = 0; i < sections.size; i++)
			sections.get(i).draw(batch);
	}

}
