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
