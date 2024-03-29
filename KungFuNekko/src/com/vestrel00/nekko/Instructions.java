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
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class Instructions implements Updatable, Drawable, Touchable {

	private enum Widgets {
		BEGIN, HEALTH, STAMINA, PAUSE, OPTIONS, ATTACK, JUMP;
	}

	public static final float PADDING_WIDTH = 6.0f, PADDING_HEIGHT = 30.0f;

	private static final CharSequence[] TITLES = { "Touch for instructions",
			"Health", "Stamina", "Pause", "Options", "Attack Buttons",
			"Jump & Move" };
	private static final CharSequence[] TEXT = {
			"Touch something!",
			"The more red this is, the more health you have.\n"
					+ "Once it turns completely gray, it is game over!\n"
					+ "You can regain health by drinking soda.",
			"The more yellow this is, the more stamina you have.\n"
					+ "Once it turns completely gray, you can no longer attack!\n"
					+ "Your stamina decreases everytime you attack.\n"
					+ "Some attacks cost more than the others.\n"
					+ "You can regain stamina by drinking soda!",
			"Touch this if you need to go to the bathroom!\n"
					+ "Or for some other reason...\n"
					+ "You may quit or resume the game by touching\n"
					+ "the health and stamina button respectively.",
			"Touch this to open the options menu.\n"
					+ "The options menu will allow you to turn\n"
					+ "the music and sound effects on or off.\n"
					+ "This will also pause the game.",
			"Tap anyone of these buttons to execute a combat move.\n"
					+ "You can perform combos by tapping the buttons consecutively.\n"
					+ "You can mix and match combos!\n"
					+ "This is a button smahing game =)",
			"Touch anywhere else on the screen to jump.\n"
					+ "You can tap again while in air to perform another jump.\n"
					+ "You move by tilting the phone." };
	private final CharSequence BACK = "BACK";

	private Widgets currentWidget, nextWidget;
	private Color color;
	private StringTyper textTyper, titleTyper;
	private float[] widths, heights;
	private Rectangle[] instructionRects;
	private Rectangle backRect;
	private Array<Rectangle> widgetRects;
	private int phase = 0, instructionIndex;

	public Instructions() {
		widths = new float[TITLES.length];
		heights = new float[TITLES.length];

		// manually set BEGIN
		TextBounds bounds = KFNekko.resource.chunkFive
				.getMultiLineBounds(TITLES[0]);
		widths[0] = bounds.width + PADDING_WIDTH * 2.0f;
		heights[0] = bounds.height
				+ KFNekko.resource.arial.getMultiLineBounds(TEXT[0]).height
				+ PADDING_HEIGHT;
		for (int i = 1; i < TITLES.length; i++) {
			bounds = KFNekko.resource.arial.getMultiLineBounds(TEXT[i]);
			widths[i] = bounds.width + PADDING_WIDTH * 2.0f;
			heights[i] = bounds.height
					+ KFNekko.resource.chunkFive.getMultiLineBounds(TITLES[i]).height
					+ PADDING_HEIGHT;
		}
		currentWidget = Widgets.BEGIN;
		nextWidget = Widgets.BEGIN;
		setInstructionIndex();
		color = new Color(Color.CLEAR);
		titleTyper = new StringTyper(TITLES[instructionIndex], 70000000L);
		textTyper = new StringTyper(TEXT[instructionIndex], KFNekko.UPDATE_TIME);

		// get widget rectangles
		widgetRects = KFNekko.hud.ui.getBaseRects();
		initInstructionRects();
		bounds = KFNekko.resource.chunkFive.getBounds(BACK);
		backRect = new Rectangle(KFNekko.settings.viewWidthHalf - bounds.width
				* 0.5f, KFNekko.settings.viewHeight - bounds.height - 14.0f,
				bounds.width, bounds.height);
	}

	/**
	 * HUDSimple rects.add(attack1); rects.add(attack2); rects.add(attack3);
	 * rects.add(attack4); rects.add(pause); rects.add(options);
	 * rects.add(topLeft); rects.add(topRight);
	 */
	private void initInstructionRects() {
		instructionRects = new Rectangle[TITLES.length];
		// BEGIN
		instructionRects[0] = new Rectangle(KFNekko.settings.viewWidthHalf
				- widths[0] * 0.5f, KFNekko.settings.viewHeightHalf
				- heights[0] * 0.5f, widths[0], heights[0]);
		// HEALTH
		instructionRects[1] = new Rectangle(widgetRects.get(6).x,
				widgetRects.get(6).y - heights[1], widths[1], heights[1]);
		// STAMINA
		instructionRects[2] = new Rectangle(widgetRects.get(7).x
				+ widgetRects.get(7).width - widths[2], widgetRects.get(7).y
				- heights[2], widths[2], heights[2]);
		// PAUSE
		instructionRects[3] = new Rectangle(widgetRects.get(4).x
				+ (widgetRects.get(4).width * 0.5f) - widths[3] * 0.5f,
				widgetRects.get(4).y + widgetRects.get(4).height + 8.0f,
				widths[3], heights[3]);
		// OPTIONS
		instructionRects[4] = new Rectangle(widgetRects.get(5).x
				+ (widgetRects.get(5).width * 0.5f) - widths[4] * 0.5f,
				widgetRects.get(5).y + widgetRects.get(5).height + 8.0f,
				widths[4], heights[4]);
		// ATTACKS
		instructionRects[5] = new Rectangle(KFNekko.settings.viewWidthHalf
				- widths[5] * 0.5f, KFNekko.settings.viewHeightHalf
				- heights[5] * 0.5f - 40.0f, widths[5], heights[5]);
		// JUMP
		instructionRects[6] = new Rectangle(KFNekko.settings.viewWidthHalf
				- widths[6] * 0.5f, KFNekko.settings.viewHeightHalf
				- heights[6] * 0.5f, widths[6], heights[6]);
	}

	private void setInstructionIndex() {
		switch (currentWidget) {
		case BEGIN:
			instructionIndex = 0;
			break;
		case HEALTH:
			instructionIndex = 1;
			break;
		case STAMINA:
			instructionIndex = 2;
			break;
		case PAUSE:
			instructionIndex = 3;
			break;
		case OPTIONS:
			instructionIndex = 4;
			break;
		case ATTACK:
			instructionIndex = 5;
			break;
		case JUMP:
			instructionIndex = 6;
			break;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		// draw rectangle
		KFNekko.hud.shape.begin(ShapeType.FilledRectangle);
		KFNekko.hud.shape.setColor(color);
		KFNekko.hud.shape.filledRect(instructionRects[instructionIndex].x,
				instructionRects[instructionIndex].y,
				instructionRects[instructionIndex].width,
				instructionRects[instructionIndex].height);
		KFNekko.hud.shape.end();

		batch.begin();
		// draw back
		KFNekko.resource.chunkFive.setColor(KFNekko.intro.menu.titleColor);
		KFNekko.resource.chunkFive.setScale(1.0f);
		KFNekko.resource.chunkFive.draw(batch, BACK, backRect.x, backRect.y
				+ backRect.height);
		// draw instruction title
		KFNekko.resource.chunkFive.setColor(1.0f, 1.0f, 1.0f, color.a);
		KFNekko.resource.chunkFive.setScale(0.8f);
		KFNekko.resource.chunkFive.draw(batch, titleTyper.getTypedStr(),
				KFNekko.camera.rect.x + instructionRects[instructionIndex].x
						+ PADDING_WIDTH, KFNekko.camera.rect.y
						+ instructionRects[instructionIndex].y
						+ instructionRects[instructionIndex].height - 2.0f);
		KFNekko.resource.chunkFive.setScale(1.0f);
		// draw instruction text
		KFNekko.resource.arial.setColor(1.0f, 1.0f, 1.0f, color.a);
		KFNekko.resource.arial.setScale(1.0f);
		KFNekko.resource.arial.drawMultiLine(batch, textTyper.getTypedStr(),
				KFNekko.camera.rect.x + instructionRects[instructionIndex].x
						+ PADDING_WIDTH, KFNekko.camera.rect.y
						+ instructionRects[instructionIndex].y
						+ instructionRects[instructionIndex].height
						- PADDING_HEIGHT);
	}

	@Override
	public void update() {
		// update the hud
		KFNekko.hud.update();
		titleTyper.update();
		textTyper.update();
		updateColor(0.03f);
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
				if (nextWidget == null) {
					KFNekko.intro.menu.view = IntroMenuManager.VIEW_MENU;
					currentWidget = Widgets.BEGIN;
				} else
					currentWidget = nextWidget;

				setInstructionIndex();
				titleTyper.reset(TITLES[instructionIndex]);
				textTyper.reset(TEXT[instructionIndex]);
				color.set(Color.CLEAR);
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * HUDSimple rects.add(attack1); rects.add(attack2); rects.add(attack3);
	 * rects.add(attack4); rects.add(pause); rects.add(options);
	 * rects.add(topLeft); rects.add(topRight);
	 */
	@Override
	public boolean onTouchDown(float x, float y) {
		if (widgetRects.get(0).contains(x, y)
				|| widgetRects.get(1).contains(x, y)
				|| widgetRects.get(2).contains(x, y)
				|| widgetRects.get(3).contains(x, y))
			nextWidget = Widgets.ATTACK;
		else if (x > widgetRects.get(4).x
				&& x < widgetRects.get(4).x + widgetRects.get(4).width
				&& y > widgetRects.get(4).y - 10.0f
				&& y < widgetRects.get(4).y + widgetRects.get(4).height + 10.0f)
			nextWidget = Widgets.PAUSE;
		// manually check for overlap to enlarge the detection
		// without enlarging the texture or using another rectangle
		// same thing for HUDSimple.java
		else if (x > widgetRects.get(5).x
				&& x < widgetRects.get(5).x + widgetRects.get(5).width
				&& y > widgetRects.get(5).y - 10.0f
				&& y < widgetRects.get(5).y + widgetRects.get(5).height + 10.0f)
			nextWidget = Widgets.OPTIONS;
		else if (widgetRects.get(6).contains(x, y))
			nextWidget = Widgets.HEALTH;
		else if (widgetRects.get(7).contains(x, y))
			nextWidget = Widgets.STAMINA;
		else if (backRect.contains(x, y)) {
			nextWidget = null;
		} else
			nextWidget = Widgets.JUMP;

		phase = 1; // fade out then in to next instruction
		KFNekko.audio.touch();
		return true;
	}

}
