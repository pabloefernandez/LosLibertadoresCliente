package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import Input.KeysListener;
import entidades.Bala;
import entidades.MundoBox2D;
import entidades.Oleada;
import enums.DireccionesPersonaje;
import enums.Estados;
import eventos.TeclasListener;
import hud.Hud;
import red.HiloCliente;
import utiles.Constantes;
import utiles.Imagen;
import utiles.Recursos;
import utiles.Render;
import utiles.Utiles;

public abstract class PantallaJuego extends PantallaBase{
	protected OrthographicCamera camaraJuego;

	protected MundoBox2D mundo = new MundoBox2D();
	protected float shooTimer;
	protected HiloCliente hc;
	protected KeysListener teclas;
	private int nroCliente;
	private Oleada[] oleadas;
	private int oleada;
	private Hud hud;
	public PantallaJuego() {
		
	}
	
	@Override
	public void show() {
		camaraJuego = new OrthographicCamera(); 
		ventanaGrafica = new FitViewport(Constantes.V_WIDTH / Constantes.PPM, Constantes.V_HEIGHT / Constantes.PPM,camaraJuego); // seteo cuanto va ser el area de mi mundo que vea mi camara,aunque redimensione la ventana
		camaraJuego.position.set(ventanaGrafica.getWorldWidth() / 2, ventanaGrafica.getWorldHeight()/ 2, 0); 
		if(Constantes.cooperativo) {
			stage = new Stage();
			fondo = new Imagen(Recursos.FONDOMENU,Constantes.V_WIDTH,Constantes.V_HEIGHT);
			Skin skin = new Skin(Gdx.files.internal("skin/star-soldier-ui.json"));
			Label mensaje= new Label("Esperando jugador",skin);
			mensaje.setPosition(Constantes.V_WIDTH / 2 - mensaje.getWidth() / 2, Constantes.V_HEIGHT / 2 - mensaje.getHeight() / 2);
			stage.addActor(mensaje);

		} else {
			hud = new Hud(mundo.getPersonaje(),0);
		}
		
	}
	
	public void handleInput(float dt) {
		if (mundo.getPersonaje()[0].getEstados() != Estados.DEAD) {
			if (teclas.isJump() && ((!Constantes.cooperativo)? mundo.getPersonaje()[0].getBody().getLinearVelocity().y <= 3 : mundo.getPersonaje()[0].getVelocidad().y <= 3) && ((!Constantes.cooperativo)?mundo.getContactListener().isJugadorEnSuelo() : Constantes.cooperativo)) {
				if(!Constantes.cooperativo) mundo.getPersonaje()[0].salta();
				else {
					hc.enviarMensaje("ApreteSaltar");
				}
			}
			if (teclas.isRight() && ((!Constantes.cooperativo)? mundo.getPersonaje()[0].getBody().getLinearVelocity().x <= 6 : mundo.getPersonaje()[0].getVelocidad().x <= 6)) {
				if(!Constantes.cooperativo) mundo.getPersonaje()[0].moverDerecha(true);
				else hc.enviarMensaje("ApreteDerecha");
			}
			if (teclas.isLeft() &&((!Constantes.cooperativo)? mundo.getPersonaje()[0].getBody().getLinearVelocity().x >= -6 : mundo.getPersonaje()[0].getVelocidad().x >= -6) ) {
				if(!Constantes.cooperativo) mundo.getPersonaje()[0].moverDerecha(false);
				else hc.enviarMensaje("ApreteIzquierda");
			}
			if (teclas.isUp()) {
				if(!Constantes.cooperativo)	mundo.getPersonaje()[0].miraArriba();
				else hc.enviarMensaje("ApreteArriba");
			}

			if (teclas.isShoot() && shooTimer >= Constantes.cadenciaDisparoActual) {// mundo.getPersonaje().getCadenciaDisparo() Gdx.input.isKeyJustPressed(Keys.SPACE
				disparar();
			}
			if(teclas.isPowerUp() && ((!Constantes.cooperativo)? mundo.getPersonaje()[0].isTienePowerUp(): Constantes.cooperativo)) {
				if(!Constantes.cooperativo) {
					if(!mundo.getPersonaje()[0].isShield()) {
						mundo.getPersonaje()[0].getPowerUp().usarPowerUp();
						mundo.getPersonaje()[0].setTienePowerUp(false);
					}
				} else {
					hc.enviarMensaje("ApretePowerUp");
				}
			}
		}
	}
	private void disparar() {
		if(!Constantes.cooperativo) {
			shooTimer = 0;
			Bala bala = new Bala(mundo.getPersonaje()[0].getBody().getPosition());
			for (int i = 0; i < Utiles.listener.size(); i++) {
				try {
					TeclasListener tl = (TeclasListener) Utiles.listener.get(i);
					if (mundo.getPersonaje()[0].getDireccionPersonaje() == DireccionesPersonaje.DERECHA) {
						tl.right();
						

					} else if (mundo.getPersonaje()[0].getDireccionPersonaje() == DireccionesPersonaje.IZQUIERDA) {
						tl.left();
						

					} 
//					else if (mundo.getPersonaje()[0].getDireccionPersonaje() == DireccionesPersonaje.ARRIBA
//							&& mundo.getPersonaje()[0].getBody().getLinearVelocity().x == 0) {
//						tl.up();
//					}
				} catch (Exception e) {
				}
			}
			bala.crearObjetoBox2D();
			mundo.getPersonaje()[0].getBalas().add(bala);

		} else {
			shooTimer = 0;
			hc.enviarMensaje("ApreteDisparar");
		}
	}
	
	public void update(float delta) {
		shooTimer += delta;
		if(!Constantes.cooperativo) {
			camaraJuego.update();
		}
		camaraJuego.update();
		handleInput(delta);
		mundo.update(delta, camaraJuego);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		if(Constantes.cooperativo) {
			if(!Constantes.empiezaPartida) {
				Render.batch.begin();
				fondo.dibujar();
				Render.batch.end();
				stage.act();
				stage.draw();
			} else {	
				update(delta);
				mundo.render(delta);
				Render.batch.begin();
				for (int i = 0; i < mundo.getPersonaje()[0].getBalas().size(); i++) {
					mundo.getPersonaje()[0].getBalas().get(i).dibujar();
				}
				Render.batch.end();
				dibujarEnemigos(delta);
				if(hud != null) {
					hud.dibujar();
				}
				Render.batch.setProjectionMatrix(camaraJuego.combined);
			}
		} else {
			update(delta);
			mundo.render(delta, camaraJuego);
			camaraJuego.position.x = mundo.getPersonaje()[0].getBody().getPosition().x;
			Render.batch.setProjectionMatrix(camaraJuego.combined);
			hud.dibujar();
			Constantes.shoot = false;
		}

	}
	
	public int getOleada() {
		return oleada;
	}

	@Override
	public void dispose() {
		hud.dispose();
		fondo.dispose();
		stage.dispose();
	}
	
	public void setPosicion(float x,float y,int personaje) {
		if(this.nroCliente == personaje) {
			mundo.getPersonaje()[0].setPosicion(x, y);
		} else {
			mundo.getPersonaje()[1].setPosicion(x, y);
		}
	}
	
	public void setVelocidad(float x,float y,int personaje) {
		if(this.nroCliente == personaje) { // si el nro de cliente es igual al personaje quiere decir que el que movio al personaje es el mismo cliente
			mundo.getPersonaje()[0].setVelocidad(x, y);
		} else {
			mundo.getPersonaje()[1].setVelocidad(x, y);
		}	
	}
	
	public void agregarBala(float x, float y,int personaje) {
		try {
			Bala bala = new Bala(x,y);
			mundo.getPersonaje()[personaje].getBalas().add(bala);
		} catch (Exception e) {
			
		}
		
	}
	
	public void setPosicionBala(float x,float y,int indiceBala,int personaje) {
		try {
			if(indiceBala < mundo.getPersonaje()[0].getBalas().size()) {
				mundo.getPersonaje()[personaje].getBalas().get(indiceBala).setPosicion(x , y );
			}
		} catch (Exception e) {
			
		}
		
	}
	
//	public void setVelocidadBala(float x, float y, int indiceBala) {
//		if(indiceBala < mundo.getPersonaje()[0].getBalas().size()) {
//			mundo.getPersonaje()[0].getBalas().get(indiceBala).setVelocidad(x, y);
//		}
//	}	
	
	public void setRemoverBala(boolean remove, int indiceBala,int personaje) {
		try {
			if(remove) {
				mundo.getPersonaje()[personaje].getBalas().remove(indiceBala);
			}
		} catch (Exception e) {
			
		}
			
		
		
	}
	
	public MundoBox2D getMundo() {
		return mundo;
	}
	
	public void setNroCliente(int nroCliente) {
		this.nroCliente = nroCliente;
		hud = new Hud(mundo.getPersonaje(),nroCliente);
	}
	
	public void setPosXCamara(float posXCamara) {
		camaraJuego.position.x = posXCamara;
	}
	
	public void crearZombies(float x, float y, int enemigo,int oleada) {
		try {
			oleadas[oleada].crearEnemigos(x, y, enemigo);
		} catch (Exception e) {
			
		}
			
		
		
	}

	public void setCantidadOleadas(int cantidadOleadas,int cantidadZombies,int oleada) {
		try {
			if (oleada == 0) {
				oleadas = new Oleada[cantidadOleadas];
			}
			oleadas[oleada] = new Oleada();
			oleadas[oleada].setEnemigos(cantidadZombies);
		} catch (Exception e) {
			
		}
		
	}
	
	public void setPosicionZombies(float x, float y, int zombie,int oleada) {
		try {
			this.oleada = oleada;
			oleadas[oleada].getEnemigos()[zombie].setPosicion(x, y);
		} catch (Exception e) {
			
		}
	}
	
	public void setVelocidadZombies(float x, float y, int zombie,int oleada) {
			this.oleada = oleada;
			oleadas[oleada].getEnemigos()[zombie].setVelocidad(x, y);
		
		
	}
	
	public void setZombieDestruido(boolean destruido, int zombie,int oleada) {
		try {
			this.oleada = oleada;
			oleadas[oleada].getEnemigos()[zombie].setDestruir(destruido);
		} catch (Exception e) {
			
		}
			
		
		
	}
	
	public void setMuertoTemporal(boolean muerto,int personaje) {
		try {
			if(nroCliente == personaje) {
				mundo.getPersonaje()[0].setMuerto(muerto);
			} else {
				mundo.getPersonaje()[1].setMuerto(muerto);
			}	
		} catch (Exception e) {
		
		}
			
		
		
	}
	
	public void setVida(int vida, int personaje) {
		if(nroCliente == personaje) {
			mundo.getPersonaje()[0].setVidaActual(vida);
		} else {
			mundo.getPersonaje()[1].setVidaActual(vida);
		}
	}
	
	public void dibujarEnemigos(float delta) {
		if (oleadas != null) {
			for (int i = 0; i <9; i++) { 
				if (oleadas[i] != null) {
					oleadas[i].render(delta);
				}
			}
		}
	}
	
	public void setMuertoFinal(int indicePersonaje) {
		if(this.nroCliente == indicePersonaje) {
			mundo.getPersonaje()[0].setMuertoFinal(true);
		} else {
			mundo.getPersonaje()[1].setMuertoFinal(true);
		}
	}
	
}
