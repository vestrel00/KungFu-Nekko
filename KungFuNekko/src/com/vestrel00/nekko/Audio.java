package com.vestrel00.nekko;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Audio implements Disposable {

	private Array<Sound> punch, smack;
	private Sound superSmack, footStep, growl, groundBoom;
	public Music music;
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
		growl = Gdx.audio.newSound(Gdx.files.internal("sound/growl.mp3"));
		groundBoom = Gdx.audio.newSound(Gdx.files
				.internal("sound/groundBoom.mp3"));

		music = Gdx.audio.newMusic(Gdx.files.internal("music/main.mp3"));
		music.setLooping(true);

		rand = new Random();
	}

	public void groundBoom(float x) {
		if (KFNekko.settings.soundOn)
			groundBoom.play(1.0f, 1.0f, getSoundPan(x));
	}

	public void growl(float x) {
		if (KFNekko.settings.soundOn)
			growl.play(1.0f, 1.0f, getSoundPan(x));
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

	/**
	 * -1.0f <-----------camera.position.x------------> 1.0f
	 */
	private static float getSoundPan(float x) {
		if (x == KFNekko.camera.camera.position.x)
			return 0.0f;
		else
			return (x - KFNekko.camera.camera.position.x)
					/ KFNekko.settings.viewWidthHalf;
	}

	@Override
	public void dispose() {
		for (int i = 0; i < punch.size; i++)
			punch.get(i).dispose();
		for (int i = 0; i < smack.size; i++)
			smack.get(i).dispose();
		superSmack.dispose();
		footStep.dispose();
		growl.dispose();
		groundBoom.dispose();
		music.dispose();
	}

}