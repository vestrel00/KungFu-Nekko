/*******************************************************************************
 * Copyright 2012 Vandolf Estrellado
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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
