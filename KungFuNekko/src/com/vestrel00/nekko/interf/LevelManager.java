package com.vestrel00.nekko.interf;

import com.vestrel00.nekko.actors.Monster;
import com.vestrel00.nekko.interf.Drawable;
import com.vestrel00.nekko.interf.Updatable;

public interface LevelManager extends Updatable, Drawable {

	public void monsterDown(Monster monster);
	
	public void pause();
	
	public void resume();

}
