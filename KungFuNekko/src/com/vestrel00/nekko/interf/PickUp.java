package com.vestrel00.nekko.interf;

import com.vestrel00.nekko.actors.components.Location;

public interface PickUp extends Updatable, Drawable {
	
	public void drop(Location location);
	
	public void pickUp();

}
