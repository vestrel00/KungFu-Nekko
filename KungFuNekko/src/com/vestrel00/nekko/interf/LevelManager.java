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

package com.vestrel00.nekko.interf;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vestrel00.nekko.actors.Monster;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public interface LevelManager extends Updatable, Drawable, Touchable {

	public void monsterDown(Monster monster);

	public void saveHighScores();

	public void pause();

	public void resume();

	public void drawText(SpriteBatch batch);

	public LevelHelper getHelper();

	public void drawSecondary(SpriteBatch batch);

}
