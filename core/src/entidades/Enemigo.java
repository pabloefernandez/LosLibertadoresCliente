package entidades;

import com.badlogic.gdx.math.Vector2;

import utiles.Constantes;

public abstract class Enemigo extends ObjetoBox2D { //ABSTRACT
	protected int vida;
	protected boolean destruir = false;
	protected boolean setDestruir = false;
	
	public Enemigo(int vida,float width,float height,Vector2 posicion) {
		super(posicion);
		this.vida = vida;
		super.width = width;
		super.height = height;
		if(!Constantes.cooperativo) crearObjetoBox2D();
	}
	
	public abstract void render(float delta);
	

	public void update() {
		if(!Constantes.cooperativo) {
			if(setDestruir && !destruir) {
				world.destroyBody(body);
				destruir = true;
			}
		}
	}
	
	public void setVida() {
		this.vida--;
		if(this.vida < 0) this.vida = 0;
		if(this.vida == 0) setDestruir = true;
	}
	
	public void setDestruir(boolean destruir) {
		this.destruir = destruir;
	}
}
