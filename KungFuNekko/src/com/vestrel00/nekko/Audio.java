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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Audio implements Disposable {

	private Array<Disposable> disposables;
	private Array<Sound> punch, smack;
	private Sound superSmack, footStep, cuteGrowl, skullGrowl, groundBoom,
			meow, touch, spawn, powerup, powerupDrop, sodaOpen, sodaDrop,
			shotHit, wingFlap;
	public Music music, gameOver, victory;
	private Random rand;

	public Audio() {
		punch = new Array<Sound>();
		smack = new Array<Sound>();
		for (int i = 0; i < 3; i++)
			punch.add(Gdx.audio.newSound(Gdx.files.internal("sound/punch"
					+ String.valueOf(i) + ".mp3")));
		for (int i = 0; i < 9; i++)
			smack.add(Gdx.audio.newSound(Gdx.files.internal("sound/smack"
					+ String.valueOf(i) + ".mp3")));
		superSmack = Gdx.audio.newSound(Gdx.files
				.internal("sound/superSmack.mp3"));
		footStep = Gdx.audio.newSound(Gdx.files.internal("sound/footStep.mp3"));
		cuteGrowl = Gdx.audio.newSound(Gdx.files
				.internal("sound/cuteGrowl.mp3"));
		skullGrowl = Gdx.audio.newSound(Gdx.files
				.internal("sound/skullGrowl.mp3"));
		groundBoom = Gdx.audio.newSound(Gdx.files
				.internal("sound/groundBoom.mp3"));
		meow = Gdx.audio.newSound(Gdx.files.internal("sound/meow.mp3"));
		touch = Gdx.audio.newSound(Gdx.files.internal("sound/touch.mp3"));
		spawn = Gdx.audio.newSound(Gdx.files.internal("sound/spawn.mp3"));
		powerup = Gdx.audio.newSound(Gdx.files.internal("sound/powerup.mp3"));
		powerupDrop = Gdx.audio.newSound(Gdx.files
				.internal("sound/powerupDrop.mp3"));
		sodaOpen = Gdx.audio.newSound(Gdx.files.internal("sound/sodaOpen.mp3"));
		sodaDrop = Gdx.audio.newSound(Gdx.files.internal("sound/sodaDrop.mp3"));
		shotHit = Gdx.audio.newSound(Gdx.files.internal("sound/shotHit.mp3"));
		wingFlap = Gdx.audio.newSound(Gdx.files.internal("sound/wingFlap.mp3"));

		gameOver = Gdx.audio.newMusic(Gdx.files.internal("music/gameOver.mp3"));
		victory = Gdx.audio.newMusic(Gdx.files
				.internal("music/victoryChant.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("music/main.mp3"));
		music.setLooping(true);

		rand = new Random();

		addToDisposables();
	}

	private void addToDisposables() {
		disposables = new Array<Disposable>();
		disposables.addAll(punch);
		disposables.addAll(smack);
		disposables.add(superSmack);
		disposables.add(footStep);
		disposables.add(cuteGrowl);
		disposables.add(groundBoom);
		disposables.add(meow);
		disposables.add(touch);
		disposables.add(music);
		disposables.add(spawn);
		disposables.add(powerup);
		disposables.add(powerupDrop);
		disposables.add(sodaOpen);
		disposables.add(sodaDrop);
		disposables.add(skullGrowl);
		disposables.add(gameOver);
		disposables.add(shotHit);
		disposables.add(wingFlap);
		disposables.add(victory);
	}

	public void touch() {
		if (KFNekko.settings.soundOn)
			touch.play();
	}

	public void wingFlap(float x) {
		if (KFNekko.settings.soundOn)
			wingFlap.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void sodaDrop(float x) {
		if (KFNekko.settings.soundOn)
			sodaDrop.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void shotHit(float x) {
		if (KFNekko.settings.soundOn)
			shotHit.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void sodaOpen(float x) {
		if (KFNekko.settings.soundOn)
			sodaOpen.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void powerupDrop(float x) {
		if (KFNekko.settings.soundOn)
			powerupDrop.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void powerup(float x) {
		if (KFNekko.settings.soundOn)
			powerup.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void spawn(float x) {
		if (KFNekko.settings.soundOn)
			spawn.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void meow(float x) {
		if (KFNekko.settings.soundOn)
			meow.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void groundBoom(float x) {
		if (KFNekko.settings.soundOn)
			groundBoom.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void growl(float x, float pitch) {
		if (KFNekko.settings.soundOn)
			skullGrowl.play(1.0f, pitch, getSoundPan(x));
	}

	public void cuteGrowl(float x) {
		if (KFNekko.settings.soundOn)
			cuteGrowl.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void footStep(float x) {
		if (KFNekko.settings.soundOn)
			footStep.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void superSmack(float x) {
		if (KFNekko.settings.soundOn)
			superSmack.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void smack(float x) {
		if (KFNekko.settings.soundOn)
			smack.get(rand.nextInt(smack.size))
					.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void punch(float x) {
		if (KFNekko.settings.soundOn)
			punch.get(rand.nextInt(punch.size))
					.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void punchSpecial(float x, float pitch) {
		if (KFNekko.settings.soundOn)
			punch.get(rand.nextInt(punch.size)).play(1.0f, pitch,
					getSoundPan(x));
	}

	/**
	 * -1.0f <-----------camera.position.x------------> 1.0f
	 */
	public static float getSoundPan(float x) {
		if (x == KFNekko.camera.camera.position.x)
			return 0.0f;
		else
			return (x - KFNekko.camera.camera.position.x)
					/ KFNekko.settings.viewWidthHalf;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < disposables.size; i++)
			disposables.get(i).dispose();
	}
}
