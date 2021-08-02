package utiles;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import entidades.Bala;
import entidades.Enemigo;
import entidades.Personaje;
import entidades.PowerUp;

public class MiContactListener implements ContactListener {
	private boolean jugadorEnSuelo = false;
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB(); // sensor

		int cDef = fa.getFilterData().categoryBits | fb.getFilterData().categoryBits;
		if (fa == null || fb == null)
			return;
		if (fa.getUserData() == null || fb.getUserData() == null)
			return;
		if (fb.getUserData() != null && fb.getUserData().equals("sensor")) {
			jugadorEnSuelo = true;
		}
//		if(fb.getUserData() instanceof Bala && fa.getUserData().equals("suelo") || fa.getUserData() instanceof Enemigo) {
//			((Bala)fb.getUserData()).setHit();
//			if(!fa.getUserData().equals("suelo")) ((Enemigo)fa.getUserData()).setVida();
//		}
		switch (cDef) {
		case Constantes.BIT_BALA | Constantes.BIT_ENEMIGO:
			if (fa.getFilterData().categoryBits == Constantes.BIT_BALA) {
				((Bala) fa.getUserData()).setHit();
				((Enemigo) fb.getUserData()).setVida();
			} else if (fb.getFilterData().categoryBits == Constantes.BIT_BALA) {
				((Bala) fb.getUserData()).setHit();
				((Enemigo) fa.getUserData()).setVida();
			}
			break;
		case Constantes.BIT_BALA | Constantes.BIT_SUELO:
			if(fa.getFilterData().categoryBits == Constantes.BIT_BALA) {
				((Bala)fa.getUserData()).setHit();
			} else if(fb.getFilterData().categoryBits == Constantes.BIT_BALA) {
				((Bala)fb.getUserData()).setHit();
			}
			break;
		case Constantes.BIT_PLAYER | Constantes.BIT_ENEMIGO:
			
			if (fa.getFilterData().categoryBits == Constantes.BIT_PLAYER) {
				if (!fa.getUserData().equals("sensor")) {
					((Personaje) fa.getUserData()).restarVida();
					((Enemigo)fb.getUserData()).setVida();
				}
			} else {
				((Personaje) fb.getUserData()).restarVida();
				((Enemigo)fa.getUserData()).setVida();
			}
			break;
		case Constantes.BIT_PLAYER | Constantes.BIT_POWER_UP:			
			if(fa.getFilterData().categoryBits == Constantes.BIT_POWER_UP) {
				PowerUp power = ((PowerUp)fa.getUserData());
				power.setDestruir();
				if (!fb.getUserData().equals("sensor")) {
					((Personaje)fb.getUserData()).setPowerUp(power);
				}
			} else {
				PowerUp power = ((PowerUp)fb.getUserData());
				power.setDestruir();
				if (!fa.getUserData().equals("sensor")) {
					((Personaje)fa.getUserData()).setPowerUp(power);
				}
			}
			break;
		}
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fb = contact.getFixtureB(); // sensor
		if (fb.getUserData() != null && fb.getUserData().equals("sensor")) {
			jugadorEnSuelo = false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	public boolean isJugadorEnSuelo() {
		return jugadorEnSuelo;
	}

}
