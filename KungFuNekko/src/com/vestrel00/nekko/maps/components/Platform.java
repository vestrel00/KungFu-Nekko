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

package com.vestrel00.nekko.maps.components;

import com.badlogic.gdx.math.Vector2;
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
			// cannot use rect.contains because it does not check for equality
			// on boundaries
			if (sections.get(i).rect.x <= x
					&& sections.get(i).rect.x + sections.get(i).rect.width >= x
					&& sections.get(i).rect.y <= y
					&& sections.get(i).rect.y + sections.get(i).rect.height >= y) {
				section = sections.get(i);
				break;
			}
	}

	public boolean hitSlopeVec(Vector2 initLoc, float dx, float dy) {
		setSection(initLoc.x, initLoc.y);
		if (section == null)
			return false;
		for (int i = 0; i < section.pieces.size; i++)
			if (section.pieces.get(i).slope != null) {
				float[] slope = section.pieces.get(i).slope;
				for (int j = 0; j < slope.length; j += 4) {
					// remember slope[j] = {x1, y1, x2, y2}
					float yVal = (((slope[j + 3] - slope[j + 1]) / (slope[j + 2] - slope[j])) * (initLoc.x - slope[j]))
							+ slope[j + 1];
					// now we have x1, y, x2 just like hitPlatform!

					// check if within x range
					if ((initLoc.x <= slope[j + 2] && initLoc.x >= slope[j])
							|| (initLoc.x + dx <= slope[j + 2] && initLoc.x
									+ dx >= slope[j]))
						// determine if we hit a slope
						if (initLoc.y + DETECTION_OFFSET >= yVal
								&& initLoc.y + dy <= yVal)
							return true;
				}
			}
		return false;
	}

	public boolean hitPlatformVec(Vector2 initLoc, float dx, float dy) {
		setSection(initLoc.x, initLoc.y);
		if (section == null)
			return false;

		for (int i = 0; i < section.pieces.size; i++)
			if (section.pieces.get(i).horizontal != null) {
				float[] horizontal = section.pieces.get(i).horizontal;

				// horizontal[j] = x1, horizontal[j+1] = y1 or y2,
				// horizontal[j+2] = x2
				for (int j = 0; j < horizontal.length; j += 3) {
					// check if within x range
					if ((initLoc.x <= horizontal[j + 2] && initLoc.x >= horizontal[j])
							|| (initLoc.x + dx <= horizontal[j + 2] && initLoc.x
									+ dx >= horizontal[j]))
						if (initLoc.y + DETECTION_OFFSET >= horizontal[j + 1]
								&& initLoc.y + dy <= horizontal[j + 1])
							return true;
				}
			}

		return false;
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
