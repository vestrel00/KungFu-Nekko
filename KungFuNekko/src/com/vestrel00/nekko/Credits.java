package com.vestrel00.nekko;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.extras.StringTyper;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public class Credits implements Updatable, Drawable {

	private final CharSequence[] authors = { "Vandolf Estrellado",
			"Dogchicken", "Buch", "PowStudios", "Lourme & Gabovitch",
			"CGEffex", "Movingplaid", "NoiseCollector", "Soundscalpel.com",
			"Shaynecantly", "Thebondman", "The League of Moveable Type" };
	private final CharSequence[] links = { "@Indiedb.com/members/vestrel00",
			"@Opengameart.org/users/dogchicken", "@Opengameart.org/users/buch",
			"@PowStudios.com", "@Jamendo.com/en/artist/560/gregoire-lourme",
			"@Freesound.org/people/CGEffex/sounds",
			"@Freesound.org/people/movingplaid/sounds",
			"@Freesound.org/people/NoiseCollector/sounds",
			"@Freesound.org/people/soundscalpel.com/sounds",
			"@Freesound.org/people/shaynecantly/sounds",
			"@Freesound.org/people/thebondman/sounds",
			"@Theleagueofmoveabletype.com" };
	private final CharSequence[] works = {
			"KungFu Nekko\nWrath of Magic\nGruff",
			"Flying Tongue Monster\nCute Monster\nSkull Monster\nSpring Demon\nDance Music",
			"Minimal sidescroller tileset\nDwarf Portrait\nPixel art user interface",
			"Smoke animation pack 1", "Commando Team (Action)",
			"Punches\nFist Punch", "Concrete Steps", "Cat 1",
			"Foley cable woosh air", "Miners Explosion",
			"Indiana jones style punch", "Chunkfive Font" };

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
			color.r += colorSpeed;
			color.g += colorSpeed;
			color.b += colorSpeed;
			color.a += colorSpeed;
			if (color.a > 1.0f) {
				phaseEndTime = TimeUtils.nanoTime();
				phase = 1;
				color.r = 1.0f;
				color.g = 1.0f;
				color.b = 1.0f;
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
