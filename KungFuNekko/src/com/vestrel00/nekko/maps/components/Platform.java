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

package com.vestrel00.nekko.maps.components;

import com.badlogic.gdx.utils.Array;
import com.vestrel00.nekko.actors.Actor;
import com.vestrel00.nekko.actors.components.Speed;

public class Platform {

	private static final float DETECTION_OFFSET = 50.0f;

	private Array<MapSection> sections;
	private MapSection section;

	public Platform(Array<MapSection> sections) {
		this.sections = sections;
	}

	private void setSection(float x, float y) {
		for (int i = 0; i < sections.size; i++)
			if (sections.get(i).rect.contains(x, y)) {
				section = sections.get(i);
				break;
			}
	}

	/**
	 * Finally make use of the laws of similar triangles that we learned back in
	 * elementary school.
	 */
	public boolean hitSlope(Actor actor, float dx, float dy) {
		setSection(actor.location.x, actor.location.rect.y);
		if (section == null)
			return false;

		for (int i = 0; i < section.pieces.size; i++)
			if (section.pieces.get(i).slope != null) {
				float[] slope = section.pieces.get(i).slope;
				for (int j = 0; j < slope.length; j += 4) {
					// remember slope[j] = {x1, y1, x2, y2}
					float yVal = (((slope[j + 3] - slope[j + 1]) / (slope[j + 2] - slope[j])) * (actor.location.x - slope[j]))
							+ slope[j + 1];
					// now we have x1, y, x2 just like hitPlatform!

					// check if within x range
					if ((actor.location.x <= slope[j + 2] && actor.location.x >= slope[j])
							|| (actor.location.x + dx <= slope[j + 2] && actor.location.x
									+ dx >= slope[j]))
						// determine if we hit a slope
						switch (actor.location.speed.yDirection) {
						case Speed.DIRECTION_DOWN:
							if (actor.location.rect.y + DETECTION_OFFSET >= yVal
									&& actor.location.rect.y + dy <= yVal) {
								actor.location.y = yVal
										+ actor.location.rect.height * 0.5f;
								actor.location.onSlope = true;
								actor.location.doubleJumped = false;
								return true;
							}
							break;
						}
				}
			}
		actor.location.onSlope = false;
		return false;
	}

	public boolean hitPlatform(Actor actor, float dx, float dy) {
		setSection(actor.location.x, actor.location.rect.y);
		if (section == null)
			return false;

		for (int i = 0; i < section.pieces.size; i++)
			if (section.pieces.get(i).horizontal != null) {
				float[] horizontal = section.pieces.get(i).horizontal;

				// horizontal[j] = x1, horizontal[j+1] = y1 or y2,
				// horizontal[j+2] = x2
				for (int j = 0; j < horizontal.length; j += 3) {
					// check if within x range
					if ((actor.location.x <= horizontal[j + 2] && actor.location.x >= horizontal[j])
							|| (actor.location.x + dx <= horizontal[j + 2] && actor.location.x
									+ dx >= horizontal[j]))
						switch (actor.location.speed.yDirection) {
						case Speed.DIRECTION_DOWN:
							if (actor.location.rect.y + DETECTION_OFFSET >= horizontal[j + 1]
									&& actor.location.rect.y + dy <= horizontal[j + 1]) {
								actor.location.onPlatform = true;
								actor.location.y = horizontal[j + 1]
										+ actor.location.rect.height * 0.5f;
								actor.location.doubleJumped = false;
								return true;
							}
							break;
						case Speed.DIRECTION_UP:
							if (actor.location.rect.y
									+ actor.location.rect.height <= horizontal[j + 1]
									&& actor.location.rect.y
											+ actor.location.rect.height + dy
											+ DETECTION_OFFSET >= horizontal[j + 1]) {
								actor.location.y = horizontal[j + 1]
										- DETECTION_OFFSET;
								actor.location.doubleJumped = true;
								return true;
							}
							break;
						}
				}
			}
		actor.location.onPlatform = false;
		return false;
	}
}
