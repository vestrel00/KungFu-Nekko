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

package com.vestrel00.nekko;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class Credits implements Updatable, Drawable {

	private static final CharSequence[] authors = { "Vandolf Estrellado",
			"LibGDX", "The League of Moveable Type", "Dogchicken", "Buch",
			"PowStudios", "Lourme & Gabovitch", "Qubodup", "CGEffex",
			"Movingplaid", "NoiseCollector", "Soundscalpel.com",
			"Shaynecantly", "Thebondman", "Morgantj", "FreqMan",
			"Special Thanks", "KungFu Nekko" };
	private static final CharSequence[] links = {
			"@IndieDB.com/members/vestrel00", "@Libgdx.badlogicgames.com/",
			"@Theleagueofmoveabletype.com",
			"@Opengameart.org/users/dogchicken", "@Opengameart.org/users/buch",
			"@PowStudios.com", "@Jamendo.com/en/artist/560/gregoire-lourme",
			"@Opengameart.org/users/qubodup",
			"@Freesound.org/people/CGEffex/sounds",
			"@Freesound.org/people/movingplaid/sounds",
			"@Freesound.org/people/NoiseCollector/sounds",
			"@Freesound.org/people/soundscalpel.com/sounds",
			"@Freesound.org/people/shaynecantly/sounds",
			"@Freesound.org/people/thebondman/sounds",
			"@Freesound.org/people/morgantj/sounds",
			"@Freesound.org/people/FreqMan/sounds", " ", " " };
	private static final CharSequence[] works = {
			"KungFu Nekko Developer\nCheck out my other project Heroes of a Lost World\n"
					+ "at vestrel00.wix.com/hoalw\nSee all of my games at IndieDB.com/members/vestrel00\n"
					+ "and my artwork at Opengameart.org/users/vestrel00",
			"The libgdx project is a cross-platform game\n"
					+ "development library written in Java, with some\n"
					+ "JNI code for performance hungry sections.\n"
					+ "It abstracts away the differences between\n"
					+ "writing desktop, Android and HTML5 games\n"
					+ "based on standards like OpenGL ES/WebGL.",
			"Chunkfive Font",
			"Cat Fighter Sprite Sheet\nFlying Tongue Monster Sprite Sheet\n"
					+ "Cute Monster Sprite Sheet\nCat Fighter Addon1 [ Energy Force Master Kit ]\n"
					+ "Skull Monster Sprite Sheet\nSpring Demon\nDance Music",
			"Minimal sidescroller tileset\nDwarf Portrait\nPixel art user interface\nChess pieces set",
			"Smoke animation pack 1",
			"Commando Team (Action)",
			"RPG Coins Set",
			"Punches\nFist Punch",
			"Concrete Steps",
			"Cat 1",
			"Foley cable woosh air",
			"Miners Explosion",
			"Indiana jones style punch",
			"Coin",
			"Empty soda can drop 1",
			"To my mother.\nWithout her I would not have had the time to make this\n"
					+ "game as well as focus on my studies.\n"
					+ "From me to her with love XD\n\n"
					+ "To the OpenGameArt community.\n"
					+ "Without them I would not have had the resources\n"
					+ "to even make a game.\n"
					+ "No amount of thanks can show my appreciation <3",
			"Copyright (C) 2012 by Vandolf Estrellado\n\n"
					+ "The code is licensed under the GNU GPL.\n"
					+ "It is free software and you may modify it\n"
					+ "and/or redistribute it under the terms of this license.\n"
					+ "See http://www.gnu.org/copyleft/gpl.html for details.", };

	private Color color;
	private StringTyper authorTyper, linkTyper, workTyper;
	private float[] authorWidths;
	private int phase = 0, index = 0;
	private long phaseEndTime;

	public Credits() {
		color = new Color(Color.CLEAR);
		authorTyper = new StringTyper(authors[index], 100000000L);
		linkTyper = new StringTyper(links[index], 50000000L);
		workTyper = new StringTyper(works[index], 10000000L);
		authorWidths = new float[authors.length];
		for (int i = 0; i < authorWidths.length; i++)
			authorWidths[i] = KFNekko.resource.chunkFive.getBounds(authors[i]).width;
	}

	@Override
	public void draw(SpriteBatch batch) {
		// draw author
		KFNekko.resource.chunkFive.setColor(1.0f, 1.0f, 1.0f, color.a);
		KFNekko.resource.chunkFive.draw(batch, authorTyper.getTypedStr(),
				KFNekko.camera.camera.position.x - authorWidths[index] * 0.5f,
				KFNekko.settings.viewHeight - 7.0f);
		// draw link
		KFNekko.resource.arial.setColor(0.0f, 0.0f, 0.0f, color.a);
		KFNekko.resource.arial.setScale(1.2f);
		KFNekko.resource.arial.draw(batch, linkTyper.getTypedStr(), 20.0f,
				40.0f);
		// draw works
		KFNekko.resource.arial.drawMultiLine(batch, workTyper.getTypedStr(),
				20.0f, 260.0f);
	}

	@Override
	public void update() {
		authorTyper.update();
		linkTyper.update();
		workTyper.update();
		switch (phase) {
		case 0:
		case 1:
			updateColor(0.03f);
			break;
		case 2:
			if (updateColor(0.04f)) {
				phase = 0;
				if (++index == authors.length)
					index = 0;
				authorTyper.reset(authors[index]);
				linkTyper.reset(links[index]);
				workTyper.reset(works[index]);
			}
			break;
		}
	}

	private boolean updateColor(float colorSpeed) {
		switch (phase) {
		case 0:
			if ((color.r += colorSpeed) > 1.0f)
				color.r = 1.0f;
			if ((color.g += colorSpeed) > 1.0f)
				color.g = 1.0f;
			if ((color.b += colorSpeed) > 1.0f)
				color.b = 1.0f;
			if ((color.a += colorSpeed) > 1.0f)
				color.a = 1.0f;
			if (color.a > 0.9f && workTyper.finished) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 1;
				color.a = 1.0f;
				return true;
			}
			break;
		case 1:
			if (TimeUtils.nanoTime() - phaseEndTime > 2000000000L) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 2;
				return true;
			}
			break;
		case 2:
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

}
