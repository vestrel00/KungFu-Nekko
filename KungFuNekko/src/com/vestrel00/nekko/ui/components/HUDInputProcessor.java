package com.vestrel00.nekko.ui.components;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.vestrel00.nekko.KFNekko;
import com.vestrel00.nekko.actors.Nekko;
import com.vestrel00.nekko.actors.states.FaceState;
import com.vestrel00.nekko.actors.states.HorizontalMotionState;
import com.vestrel00.nekko.interf.CombatStateManager;

public class HUDInputProcessor implements InputProcessor {

	public CombatStateManager attackManager;
	private Vector3 touchPos;
	private Nekko player;

	public HUDInputProcessor(Nekko player) {
		this.player = player;
		touchPos = new Vector3();
		// attackManager = new ComboAttackManager();
		attackManager = new SimpleAttackManager(player);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		touchPos.set(x, y, 0);
		KFNekko.camera.camera.unproject(touchPos);
		switch (KFNekko.view) {
		case KFNekko.VIEW_INTRO:
			return (KFNekko.intro.onTouchDown(touchPos.x, touchPos.y)) ? true
					: false;
		case KFNekko.VIEW_GAME:
			return (KFNekko.hud.onTouchDown(touchPos.x, touchPos.y)) ? true
					: false;
		case KFNekko.VIEW_PAUSED:
			return (KFNekko.pauseManager.onTouchDown(touchPos.x, touchPos.y)) ? true
					: false;
		case KFNekko.VIEW_OPTIONS:
			if (KFNekko.pauseManager.onTouchDown(touchPos.x, touchPos.y)
					|| KFNekko.optionsManager.onTouchDown(touchPos.x,
							touchPos.y))
				return true;
			break;
		}
		return false;
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
		switch (keycode) {
		case Keys.A:
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_DOWN));
			return true;
		case Keys.Z:
			if (++player.health >= player.maxHealth)
				player.health = player.maxHealth;
			return true;
		case Keys.X:
			if (++player.stamina >= player.maxStamina)
				player.stamina = player.maxStamina;
			return true;
		case Keys.S:
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_LEFT));
			return true;
		case Keys.D:
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_RIGHT));
			return true;
		case Keys.F:
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_UP));
			return true;
		case Keys.LEFT:
			player.setHorizontalMotionState(HorizontalMotionState.MOVING);
			player.faceState = FaceState.LEFT;
			return true;
		case Keys.RIGHT:
			player.setHorizontalMotionState(HorizontalMotionState.MOVING);
			player.faceState = FaceState.RIGHT;
			return true;
		case Keys.SPACE: // JUMP
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_JUMP));
			player.jump();
			return true;
		case Keys.E: // FORCE JUMP TODO REMOVE
			player.location.onPlatform = true;
			player.jump();
			return true;
		default: // any other key is attack
			player.setCombatState(attackManager
					.input(ComboAttackManager.INPUT_ATTACK));
			return true;
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.LEFT:
		case Keys.RIGHT:
			player.setHorizontalMotionState(HorizontalMotionState.IDLE);
			return true;
		}
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
