package entidades;
import com.badlogic.gdx.math.Vector2;
import utiles.Constantes;

public class Oleada {
	private Enemigo[] enemigos;
	private Vector2 posicion;
	private boolean bandera = false;
	private int distancia = 0;
	public void setEnemigos(int cantidad) {
		enemigos = new Enemigo[cantidad];
	}
	
	public void setPosicion(Vector2 posicion) {
		this.posicion = posicion;
	}
	
	public void crearEnemigos() {
		for (int i = 0; i < enemigos.length; i++) {
			enemigos[i] = new Zombie(new Vector2((Constantes.V_WIDTH / 2) / Constantes.PPM + posicion.x + i + distancia ,4)); 
			distancia+=3;
		}
	}
	
	public void update(float delta) {
		for (int i = 0; i < enemigos.length; i++) {
			enemigos[i].update();
		}
	}
	
	public void render(float delta) {
		for (int i = 0; i < enemigos.length; i++) {
			if(enemigos[i] != null ) {
				enemigos[i].render(delta);
			}
		}	
		
	}
	public boolean isBandera() {
		return bandera;
	}
	
	public Enemigo[] getEnemigos() {
		return enemigos;
	}
	
	public void crearEnemigos(float x,float y,int enemigo) {
		enemigos[enemigo] = new Zombie(new Vector2(x,y)); 	
	}
	
	
}
