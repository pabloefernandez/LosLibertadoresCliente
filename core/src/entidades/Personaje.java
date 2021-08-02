package entidades;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import enums.DireccionesPersonaje;
import enums.Estados;
import pantallas.PantallaMuerte;
import utiles.Animacion;
import utiles.Constantes;
import utiles.Render;
import utiles.Utiles;


public abstract class Personaje extends ObjetoBox2D {
	protected Animacion animacionIdle, animacionJump, animacionRun, animacionActual, animacionMirArriba,animacionIdleShoot,animacionMuerte;
	private boolean miraArriba = false;
	private Estados estadoActual;
//	protected float widthAnimacion = 3, heightAnimacion = 6;
	private DireccionesPersonaje direccionPersonaje = DireccionesPersonaje.DERECHA;
	private static ArrayList<Bala> balas = new ArrayList<Bala>();
	//private int vidaMax = CONSTANTES.CREDITOS;
	private int vidasMaximo = 3;
	private int vidaActual = vidasMaximo,temporizadorMuerte = 0,contSegundos = 0;
	private boolean shield = false,muerto = false,tienePowerUp = false, muertoFinal = false;
	private float cadenciaDisparo = 0.3f;
	private PowerUp powerUp;

	
	public Personaje() {
		super.width = 3; // metros en box2d
		super.height = 6; // metros en box2d
		super.posicion.set(4, 8);
		estadoActual = Estados.STANDING;
		if(!Constantes.cooperativo) crearObjetoBox2D();
	}

	public void render(float delta) {
		Render.batch.begin();
		TextureRegion currentFrame = getAnimacionActual(delta);
			if(!Constantes.cooperativo) {
				temporizador();
				for (int i = 0; i < balas.size(); i++) {
					Bala balaActual = balas.get(i);
					balaActual.update();
					if (balaActual.shouldRemove()) {
						balas.remove(i);
						world.destroyBody(balaActual.getBody());
						i--;
					}
					if (i >= 0)
						balaActual.dibujar(i, direccionPersonaje, balas);
				}
		}
		if(!muertoFinal) {
			Render.batch.draw(currentFrame,
					(((!Constantes.cooperativo)?body.getPosition().x : posicion.x) + ((direccionPersonaje == DireccionesPersonaje.DERECHA|| direccionPersonaje == DireccionesPersonaje.ARRIBA) ? .1f : -0.1f))
							- (animacionActual.getWidth() / 2),
					((!Constantes.cooperativo)?body.getPosition().y : posicion.y) - ((!muerto)?(animacionActual.getHeight() / 2): (animacionActual.getHeight() / 2) + Constantes.HEIGHT_CUERPO_BOX2D - (animacionActual.getHeight() / 2)), animacionActual.getWidth(),
					animacionActual.getHeight());

		}
		Render.batch.end();
		animacionActual.update(delta);
	}

	private void temporizador() {
		if (muerto || (powerUp != null && powerUp instanceof DisparoRapido && ((DisparoRapido)powerUp).isUsoPowerUp())) {
			temporizadorMuerte++;
			if (temporizadorMuerte % 60 == 0) {
				contSegundos++;
				if(contSegundos % 5 == 0) {
					tienePowerUp = false;
					Constantes.cadenciaDisparoActual = cadenciaDisparo;
				}
				if (contSegundos % 3 == 0) {
					muerto = false;
					estadoActual = Estados.STANDING;
					Constantes.prueba = false;
				}
			}
		}
	}


	public void crearObjetoBox2D() {
		bodyDef.position.set(posicion); // 4 4
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(Constantes.WIDTH_CUERPO_BOX2D, Constantes.HEIGHT_CUERPO_BOX2D); //Constantes.HEIGHT_CUERPO_BOX2D Constantes.WIDTH_CUERPO_BOX2D
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constantes.BIT_PLAYER;
		fixtureDef.filter.maskBits = Constantes.BIT_SUELO | Constantes.BIT_ENEMIGO | Constantes.BIT_POWER_UP;
		fixture = body.createFixture(fixtureDef);
		fixture.setUserData(this);
		// sensor
		shape.setAsBox(0.9f, 1f, new Vector2(0, -2.1f), 0);
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		body.createFixture(fixtureDef).setUserData("sensor");
	}

	private Estados getEstadoss() {
		if(!Constantes.cooperativo) {
			if(muerto || muertoFinal) return Estados.DEAD;
			else if (body.getLinearVelocity().y > 0)
				return Estados.JUMPING;
			else if (body.getLinearVelocity().y < 0)
				return Estados.FALLING;
			else if (body.getLinearVelocity().x != 0)
				return Estados.RUNNING;
			else if (miraArriba)
				return Estados.MIRARRIBA;
			else
				return Estados.STANDING;
		} else {
			if(muerto) return Estados.DEAD;
			else if (velocidad.y > 0)
				return Estados.JUMPING;
			else if (velocidad.y < 0)
				return Estados.FALLING;
			else if (velocidad.x != 0)
				return Estados.RUNNING;
			else if (miraArriba)
				return Estados.MIRARRIBA;
			else
				return Estados.STANDING;
		}
	}

	private TextureRegion getAnimacionActual(float delta) {
		estadoActual = getEstadoss();
		TextureRegion region = null;
		switch (estadoActual) {
		case RUNNING:
			animacionActual = animacionRun;
			animacionActual.setDelay(1f / 8f);
			region = animacionActual.getFrame();
			break;
		case STANDING:
			animacionActual = animacionIdle;
			region = animacionActual.getFrame();
			break;
		case MIRARRIBA:
			animacionActual = animacionMirArriba;
			region = animacionActual.getFrame();
			break;
		case DEAD:
			animacionActual = animacionMuerte;
			region = animacionActual.getFrame();
			break;
		default:
			animacionActual = animacionJump;
			region = animacionActual.getFrame();
		}
		if ((((!Constantes.cooperativo)? body.getLinearVelocity().x < 0 : velocidad.x < 0) || direccionPersonaje == DireccionesPersonaje.IZQUIERDA) && !region.isFlipX()) { // si corremos para la izquierda y la region 
																															 // está mirando hacia la derecha.
																															 // Hay que invertir la region
			region.flip(true, false);
			direccionPersonaje = DireccionesPersonaje.IZQUIERDA;
		} else if ((((!Constantes.cooperativo)? body.getLinearVelocity().x > 0 : velocidad.x > 0) || direccionPersonaje == DireccionesPersonaje.DERECHA)
				&& region.isFlipX()) {
			region.flip(true, false);
			direccionPersonaje = DireccionesPersonaje.DERECHA;
		}
		return region;
	}

	public ArrayList<Bala> getBalas() {
		return balas;
	}

	public DireccionesPersonaje getDireccionPersonaje() {
		return direccionPersonaje;
	}

	
	public void moverDerecha(boolean derecha) {
		body.applyLinearImpulse(new Vector2(((derecha)?1f:-1), 0), body.getWorldCenter(), true);
		miraArriba = false;
		direccionPersonaje = ((derecha)?DireccionesPersonaje.DERECHA:DireccionesPersonaje.IZQUIERDA);
	}
	
	public void miraArriba() {
		miraArriba = true;
		direccionPersonaje = DireccionesPersonaje.ARRIBA;
	}

	public void salta() {
		body.applyLinearImpulse(new Vector2(0, 10f), body.getWorldCenter(), true);
		miraArriba = false;
	}
	
	public void restarVida() {
		if(!Constantes.cooperativo) {
			if(!shield){
				if(this.vidaActual > 0) {
					this.vidaActual--;	
					if(this.vidaActual == 0) {
						Utiles.prueba.cambiarPantalla(new PantallaMuerte());
					}
					Constantes.prueba = true;
					muerto = true;
				}
			} else {
				shield = false;
				tienePowerUp = false;
			}
		}	
	}
	
	public void setPowerUp(PowerUp power) {
		if (!Constantes.cooperativo) {
			if (power instanceof AngelGuardian) {
				shield = ((AngelGuardian) power).getShield();
				powerUp = new AngelGuardian();
			} else if (power instanceof DisparoRapido) {
				powerUp = new DisparoRapido();
				shield = false;
			}
			tienePowerUp = true;
		}
	}


	public Estados getEstados() {
		return estadoActual;
	}
	public float getCadenciaDisparo() {
		return cadenciaDisparo;
	}
	
	public boolean isTienePowerUp() {
		return tienePowerUp;
	}
	public void setTienePowerUp(boolean tienePowerUp) {
		this.tienePowerUp = tienePowerUp;
	}
	
	public PowerUp getPowerUp() {
		return powerUp;
	}
	
	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}
	
	public int getVidaActual() {
		return vidaActual;
	}
	
	public int getVidasMaximo() {
		return vidasMaximo;
	}
	
	public void setVidaActual(int vidaActual) {
		this.vidaActual = vidaActual;
	}
	
	public void setMuertoFinal(boolean muertoFinal) {
		this.muertoFinal = muertoFinal;
	}
	
	public boolean isShield() {
		return shield;
	}

}
