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

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Touchable;
import com.vestrel00.nekko.interf.Updatable;

public class PauseManager implements Touchable, Drawable, Updatable {

	private final float COLOR_SPEED = 0.01f;
	private final CharSequence PAUSED = "PAUSED", RESUME = "RESUME",
			QUIT = "QUIT";

	private Array<Rectangle> baseRects;
	private Rectangle resumeRect, quitRect;
	private Vector2 resumeVec, quitVec;
	private float halfWidth;
	public Color resumeColor, targetResumeColor;
	private Random rand;

	public PauseManager() {
		TextBounds bound = KFNekko.resource.chunkFive.getBounds(RESUME);
		resumeVec = new Vector2();
		bound = KFNekko.resource.chunkFive.getBounds(QUIT);
		quitVec = new Vector2();
		bound = KFNekko.resource.chunkFive.getBounds(PAUSED);
		halfWidth = bound.width * 0.5f;
		resumeColor = new Color(Color.WHITE);
		targetResumeColor = new Color(Color.YELLOW);
		rand = new Random();
		baseRects = KFNekko.hud.ui.getQuitResumeRects();
		resumeRect = new Rectangle(baseRects.get(1));
		quitRect = new Rectangle(baseRects.get(0));
	}

	@Override
	public void update() {
		updateResumeColor();
		resumeVec.x = KFNekko.camera.rect.x + 380.0f;
		resumeVec.y = KFNekko.camera.rect.y + 302.0f;
		quitVec.x = KFNekko.camera.rect.x + 22.0f;
		quitVec.y = KFNekko.camera.rect.y + 302.0f;
		resumeRect.x = KFNekko.camera.rect.x + baseRects.get(1).x;
		resumeRect.y = KFNekko.camera.rect.y + baseRects.get(1).y;
		quitRect.x = KFNekko.camera.rect.x + baseRects.get(0).x;
		quitRect.y = KFNekko.camera.rect.y + baseRects.get(0).y;
	}

	@Override
	public void draw(SpriteBatch batch) {
		batch.begin();
		if (KFNekko.view == KFNekko.VIEW_PAUSED) {
			KFNekko.resource.chunkFive.setColor(Color.WHITE);
			KFNekko.resource.chunkFive.setScale(1.5f);
			KFNekko.resource.chunkFive.draw(batch, PAUSED,
					KFNekko.camera.camera.position.x - halfWidth * 1.5f,
					KFNekko.camera.camera.position.y + 25.0f);
		}
		KFNekko.resource.chunkFive.setColor(resumeColor);
		KFNekko.resource.chunkFive.setScale(0.5f);
		KFNekko.resource.chunkFive
				.draw(batch, RESUME, resumeVec.x, resumeVec.y);
		KFNekko.resource.chunkFive.draw(batch, QUIT, quitVec.x, quitVec.y);
		KFNekko.resource.chunkFive.setScale(1.0f);
		batch.end();
	}

	@Override
	public boolean onTouchDown(float x, float y) {
		if (resumeRect.contains(x, y)) {
			KFNekko.view = KFNekko.VIEW_GAME;
			KFNekko.audio.touch();
			KFNekko.map.manager.resume();
			if (KFNekko.settings.musicOn)
				KFNekko.audio.music.play();
			return true;
		} else if (quitRect.contains(x, y)) {
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
			if (KFNekko.audio.music.isPlaying())
				KFNekko.audio.music.stop();
			// reset the intro states!
			KFNekko.intro.reset();
			// reset the player!
			KFNekko.player.reset(0, 0);
			return true;
		}
		return false;
	}

	private void updateResumeColor() {
		boolean change = false;
		if (resumeColor.r > targetResumeColor.r) {
			if ((resumeColor.r -= COLOR_SPEED) < targetResumeColor.r)
				change = true;
		} else if (resumeColor.r < targetResumeColor.r) {
			if ((resumeColor.r += COLOR_SPEED) > targetResumeColor.r)
				change = true;
		}

		if (resumeColor.g > targetResumeColor.g) {
			if ((resumeColor.g -= COLOR_SPEED) < targetResumeColor.g)
				change = true;
		} else if (resumeColor.g < targetResumeColor.g) {
			if ((resumeColor.g += COLOR_SPEED) > targetResumeColor.g)
				change = true;
		}

		if (resumeColor.b > targetResumeColor.b) {
			if ((resumeColor.b -= COLOR_SPEED) < targetResumeColor.b)
				change = true;
		} else if (resumeColor.b < targetResumeColor.b) {
			if ((resumeColor.b += COLOR_SPEED) > targetResumeColor.b)
				change = true;
		}

		if (change)
			Methods.randomColor(targetResumeColor, rand);

	}

}
