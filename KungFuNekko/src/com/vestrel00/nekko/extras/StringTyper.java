package com.vestrel00.nekko.extras;

import com.badlogic.gdx.utils.TimeUtils;
import com.vestrel00.nekko.interf.Updatable;

public class StringTyper implements Updatable {

	private StringBuilder builder;
	private CharSequence str;
	private long typeDelay, lastTypeTime;
	public int strIndex = 0;
	public boolean finished;

	public StringTyper(CharSequence str, long typeDelay) {
		this.str = str;
		this.typeDelay = typeDelay;
		builder = new StringBuilder();
		lastTypeTime = TimeUtils.nanoTime();
	}

	public CharSequence getTypedStr() {
		return builder;
	}

	@Override
	public void update() {
		if (!finished && TimeUtils.nanoTime() - lastTypeTime > typeDelay) {
			lastTypeTime = TimeUtils.nanoTime();
			builder.append(str.charAt(strIndex++));
			finished = strIndex == str.length();
		}
	}
	
	public void reset(CharSequence str) {
		reset(str, this.typeDelay);
	}

	public void reset(CharSequence str, long typeDelay) {
		this.str = str;
		this.typeDelay = typeDelay;
		finished = false;
		lastTypeTime = TimeUtils.nanoTime();
		strIndex = 0;
		builder.delete(0, builder.length());
	}

	public void finish() {
		while (!finished) {
			builder.append(str.charAt(strIndex++));
			finished = strIndex == str.length();
		}
	}

}
