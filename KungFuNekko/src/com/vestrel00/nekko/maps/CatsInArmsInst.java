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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.Instructions;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class CatsInArmsInst implements Touchable, Updatable, Drawable {

	private static final CharSequence[] TITLES = { "Cats In Arms",
			"Your Structures", "Enemy Structures", "Red Cat", "Black Cat",
			"Pink Cat", "Blue Cat", "Cyan Cat", "Black Hole" };
	private static final CharSequence[] TEXT = {
			"Your cat and his cat buddies has been turned\n"
					+ "into chess pieces! Your cat is the general of\n"
					+ "his army. Help him lead his buddies in the fight\n"
					+ "against a horde of cute monsters. Their goal is to\n"
					+ "to destroy enemy structures!\n"
					+ "However, his cat buddies will NOT fight for FREE.\n"
					+ "Touch one of the cat icons to summon a cat\n"
					+ "into battle at the cost of some catnip!",
			"Protect your structures from the cute monsters.\n"
					+ "Losing all your structures means game over!\n"
					+ "However, do not hesitate to fall back\n"
					+ "and use your structures to defeat some\n"
					+ "monsters when your bretherens are down\n"
					+ "and you have no more catnip or stamina!",
			"You win by destroying all enemy structures.\n"
					+ "Win it for the epic music at then end!\n"
					+ "Unlike your structures, enemy structures\n"
					+ "fights back even if they are not within your sight!",
			"These cats do twice the damage of other cats.\n"
					+ "Cost : 3 catnips",
			"These cats are kungfu masters.\n"
					+ "They are the fastest attacking unit in the game!\n"
					+ "These also have twice the health of normal cats!\n"
					+ "Cost : 2 catnips",
			"These cats do damage at a distance.\n" + "Cost : 3 catnip",
			"These cats are the fastest moving units in the game!\n"
					+ "However, they have less health than other cats!\n"
					+ "Make some of these if you need immediate reinforcement!\n"
					+ "Cost : 1 catnip",
			"These cats are your tanks.\n"
					+ "They have 3 times the health of normal cats\n"
					+ "However, they move and attack slow.\n"
					+ "Cost : 2 catnips",
			"This is were your cats spawn at.\n"
					+ "The cute monsters also spawn at another black hole\n"
					+ "on the other side of the map.\n"
					+ "Enemies drop soda and power ups!\n"
					+ "Defeating enemies give you more catnip!" };
	private final CharSequence SKIP = "SKIP", NEXT = "NEXT";

	private CatsInArms level;
	public int phase = 0, instructionIndex = 0, nextInstructionIndex = 1;
	private Color color;
	private StringTyper textTyper, titleTyper;
	private float[] widths, heights;
	private Rectangle skipRect, nextRect, skipRectBase, nextRectBase;
	private Vector2 topLeft;

	public CatsInArmsInst(CatsInArms level) {
		this.level = level;
		widths = new float[TITLES.length];
		heights = new float[TITLES.length];

		// manually set BEGIN
		TextBounds bounds = KFNekko.resource.chunkFive
				.getMultiLineBounds(TITLES[0]);
		widths[0] = bounds.width + Instructions.PADDING_WIDTH * 2.0f;
		heights[0] = bounds.height
				+ KFNekko.resource.arial.getMultiLineBounds(TEXT[0]).height
				+ Instructions.PADDING_HEIGHT;
		for (int i = 1; i < TITLES.length; i++) {
			bounds = KFNekko.resource.arial.getMultiLineBounds(TEXT[i]);
			widths[i] = bounds.width + Instructions.PADDING_WIDTH * 2.0f;
			heights[i] = bounds.height
					+ KFNekko.resource.chunkFive.getMultiLineBounds(TITLES[i]).height
					+ Instructions.PADDING_HEIGHT;
		}
		color = new Color(Color.CLEAR);
		titleTyper = new StringTyper(TITLES[instructionIndex], 70000000L);
		textTyper = new StringTyper(TEXT[instructionIndex], KFNekko.UPDATE_TIME);

		// init touchable rects
		bounds = KFNekko.resource.chunkFive.getBounds(SKIP);
		skipRectBase = new Rectangle(KFNekko.settings.viewWidthHalf
				- bounds.width * 0.5f, KFNekko.settings.viewHeight
				- bounds.height - 4.0f, bounds.width, bounds.height);
		skipRect = new Rectangle(skipRectBase);
		bounds = KFNekko.resource.chunkFive.getBounds(NEXT);
		nextRectBase = new Rectangle(KFNekko.settings.viewWidthHalf
				- bounds.width * 0.5f, 37.0f, bounds.width, bounds.height);
		nextRect = new Rectangle(nextRectBase);
		// the top left point of the shape
		topLeft = new Vector2(10.0f, 240.0f);
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		// draw skip
		KFNekko.resource.chunkFive.setColor(KFNekko.intro.menu.titleColor);
		KFNekko.resource.chunkFive.setScale(1.0f);
		KFNekko.resource.chunkFive.draw(batch, SKIP, skipRect.x, skipRect.y
				+ skipRect.height);
		// draw next
		KFNekko.resource.chunkFive.draw(batch, NEXT, nextRect.x, nextRect.y
				+ nextRect.height);
		// draw instruction title
		KFNekko.resource.chunkFive.setColor(1.0f, 1.0f, 1.0f, color.a);
		KFNekko.resource.chunkFive.setScale(0.8f);
		KFNekko.resource.chunkFive.draw(batch, titleTyper.getTypedStr(),
				KFNekko.camera.rect.x + topLeft.x + Instructions.PADDING_WIDTH,
				KFNekko.camera.rect.y + topLeft.y - 2.0f);
		KFNekko.resource.chunkFive.setScale(1.0f);
		// draw instruction text
		KFNekko.resource.arial.setColor(1.0f, 1.0f, 1.0f, color.a);
		KFNekko.resource.arial.setScale(1.0f);
		KFNekko.resource.arial
				.drawMultiLine(batch, textTyper.getTypedStr(),
						KFNekko.camera.rect.x + topLeft.x
								+ Instructions.PADDING_WIDTH,
						KFNekko.camera.rect.y + topLeft.y
								- Instructions.PADDING_HEIGHT);
		batch.end();
	}

	@Override
	public void update() {
		KFNekko.intro.menu.updateTitleColor();
		titleTyper.update();
		textTyper.update();
		updateColor(0.03f);
		skipRect.x = skipRectBase.x + KFNekko.camera.rect.x;
		skipRect.y = skipRectBase.y + KFNekko.camera.rect.y;
		nextRect.x = nextRectBase.x + KFNekko.camera.rect.x;
		nextRect.y = nextRectBase.y + KFNekko.camera.rect.y;
	}

	private boolean updateColor(float colorSpeed) {
		switch (phase) {
		case 0: // fade in
			if ((color.r += colorSpeed) > 0.4118f)
				color.r = 0.4118f;
			if ((color.g += colorSpeed) > 0.6157f)
				color.g = 0.6157f;
			if ((color.b += colorSpeed) > 1.0f)
				color.b = 1.0f;
			if ((color.a += colorSpeed) > 1.0f)
				color.a = 1.0f;
			break;
		case 1: // fade out
			color.a -= 0.03f;
			if (color.a < 0.0f) {
				phase = 0;
				instructionIndex = nextInstructionIndex;
				if (instructionIndex == TITLES.length)
					start();
				else
					level.helper.setCamera(instructionIndex);

				titleTyper.reset(TITLES[instructionIndex]);
				textTyper.reset(TEXT[instructionIndex]);
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

	private void start() {
		// make sure states are set to their starting values
		instructionIndex = 0;
		KFNekko.view = KFNekko.VIEW_GAME;
		if (KFNekko.intro.music.isPlaying())
			KFNekko.intro.music.stop();
		if (KFNekko.settings.musicOn)
			KFNekko.audio.music.play();
		// camera work
		KFNekko.camera.targetActor = KFNekko.player;
		KFNekko.camera.mode = Camera.MODE_NORMAL;
		KFNekko.camera.normalizeXSpeed = Camera.DEFAULT_NORMALIZE_SPEED;
		KFNekko.camera.normalizeYSpeed = Camera.DEFAULT_NORMALIZE_SPEED;

		// reset
		KFNekko.player.nekkoSprite.targetColor.set(Color.WHITE);
		for (int i = 0; i < level.helper.allyPieces.size; i++)
			level.helper.allyPieces.get(i).chessSprite.targetColor
					.set(Color.WHITE);
		for (int i = 0; i < level.helper.enemyPieces.size; i++)
			level.helper.enemyPieces.get(i).chessSprite.targetColor
					.set(Color.WHITE);
		level.startTime = TimeUtils.nanoTime();
		level.catnip = 5;
		for (int i = level.helper.staticAllyCount; i < KFNekko.allies.size; i++) {
			KFNekko.allies.removeIndex(level.helper.staticAllyCount);
		}
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (nextRect.contains(x, y)) {
			nextInstructionIndex = instructionIndex + 1;
			phase = 1;
			KFNekko.audio.touch();
		} else if (skipRect.contains(x, y)) {
			nextInstructionIndex = TITLES.length;
			phase = 1;
			KFNekko.audio.touch();
		}
		return true;
	}

}
