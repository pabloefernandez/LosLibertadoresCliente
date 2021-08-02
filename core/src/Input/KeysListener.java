package Input;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import red.HiloCliente;
import utiles.Constantes;

public class KeysListener implements InputProcessor{
	private boolean right = false,left = false,jump = false,powerUp= false,up = false, shoot = false;
	private HiloCliente hc;
	public KeysListener(HiloCliente hc) {
		this.hc = hc;
	}
	public KeysListener() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.SHIFT_LEFT) jump = true;
		else if(keycode == Keys.LEFT) left = true;
		else if(keycode == Keys.RIGHT) {
			right = true;
		}
		else if(keycode == Keys.Z) powerUp = true;
		else if(keycode == Keys.UP) up = true;
		else if(keycode == Keys.SPACE) {
			shoot = true;
		}
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.SHIFT_LEFT) {
			jump = false;
			if(Constantes.cooperativo) hc.enviarMensaje("DejeApretarSaltar");
		}
		else if(keycode == Keys.LEFT) {
			left = false;
			if(Constantes.cooperativo) hc.enviarMensaje("DejeApretarIzquierda");
		}
		else if(keycode == Keys.RIGHT) {
			right = false;
			if(Constantes.cooperativo) hc.enviarMensaje("DejeApretarDerecha");
		}
		else if(keycode == Keys.Z) {
			powerUp = false;
			if(Constantes.cooperativo) hc.enviarMensaje("DejeApretarPowerUp");
		}
		else if(keycode == Keys.UP) {
			up = false;
			if(Constantes.cooperativo) hc.enviarMensaje("DejeApretarArriba");
		}
		else if(keycode == Keys.SPACE) {
			shoot = false;
			if(Constantes.cooperativo) {
				hc.enviarMensaje("DejeApretarDisparar");
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean isRight() {
		return right;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isJump() {
		return jump;
	}
	public boolean isPowerUp() {
		return powerUp;
	}
	public boolean isUp() {
		return up;
	}
	public boolean isShoot() {
		return shoot;
	}


}
