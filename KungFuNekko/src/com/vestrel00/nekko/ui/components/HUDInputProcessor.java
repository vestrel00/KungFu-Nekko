package com.vestrel00.nekko.ui.components;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.vestrel00.nekko.KFNekko;

public class HUDInputProcessor implements InputProcessor {

	private Vector3 touchPos;

	public HUDInputProcessor() {
		touchPos = new Vector3();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		touchPos.set(x, y, 0);
		KFNekko.camera.camera.unproject(touchPos);
		return (KFNekko.hud.onTouchDown(touchPos.x, touchPos.y)) ? true : false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	// DESKTOP
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	// //////////

}