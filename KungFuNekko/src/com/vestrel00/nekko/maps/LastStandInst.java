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
import com.vestrel00.nekko.Camera;
import com.vestrel00.nekko.Instructions;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class LastStandInst implements Touchable, Updatable, Drawable {

	private static final CharSequence[] TITLES = { "The Last Stand",
			"The Pieces", "Pawn", "Rook", "Queen", "Bishop", "Knight", "King",
			"Black Hole" };
	private static final CharSequence[] TEXT = {
			"Your cat has been turned into a chess piece!\n"
					+ "You must control your cat in this game of chess.\n"
					+ "This is your cat's final stand.\n"
					+ "Help him in his final moments\n"
					+ "as he fight a horde of cute monsters.\n"
					+ "Help him defeat as many monsters as possible!",
			"There are several chess pieces in the map.\n"
					+ "It is game over once all the pieces are destoyed,\n"
					+ "or if your cat is defeated in battle.\n"
					+ "All of the chess pieces will attack enemies\n"
					+ "only if they are within your sight!\n"
					+ "Each piece's health is indicated by\n"
					+ "its color. Dark gray means dead!\n"
					+ "Each piece drop soda at the beginning of every wave.",
			"These are your basic defense units.\n"
					+ "They don't have much health and do minimal damage.\n",
			"Have a lot of health and power.\n"
					+ "Rooks are much more dependable than pawns.\n"
					+ "However, they will not last forever.",
			"The most powerful piece in the game.\n"
					+ "Queens destroy multiple enemies at once\n"
					+ "with ease. They also have plenty of health.",
			"Stronger and lasts longer than a pawn.\n"
					+ "Bishops also attack multiple enemies at once.",
			"Knights are stronger than rooks\n" + "but have less health.",
			"The second most powerful piece in the game\n"
					+ "next to the Queen. The King has twice\n"
					+ "the health of a Queen!",
			"Monsters will spawn at one of these these locations.\n"
					+ "Monsters will spawn more frequently as time passes\n"
					+ "and also become faster, stronger, and better!\n"
					+ "These units may drop soda and powerups!\n"
					+ "Beat the color out of them!" };
	private final CharSequence SKIP = "SKIP", NEXT = "NEXT";

	private LastStand level;
	public int phase = 0, instructionIndex = 0, nextInstructionIndex = 1;
	private Color color;
	private StringTyper textTyper, titleTyper;
	private float[] widths, heights;
	private Rectangle skipRect, nextRect, skipRectBase, nextRectBase;
	private Vector2 topLeft;

	public LastStandInst(LastStand level) {
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
		for (int i = 0; i < level.helper.chessPieces.size; i++) {
			level.helper.chessPieces.get(i).chessSprite.targetColor
					.set(Color.WHITE);
			level.helper.chessPieces.get(i).resetVibrationState();
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
