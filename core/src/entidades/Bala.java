package entidades;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import enums.DireccionesDisparo;
import enums.DireccionesPersonaje;
import eventos.TeclasListener;
import utiles.Animacion;
import utiles.Constantes;
import utiles.Render;
import utiles.Utiles;

public class Bala extends ObjetoBox2D implements TeclasListener{
	private Vector2 velocidad;
	private float velY = 35f, velX = 35f;
	private final float width = 1f;
	private final float height = 1f;
	private boolean hit = false, remove;
	private Texture textura;
	private Animacion animacionHit;
	private DireccionesDisparo direccionDisparo = DireccionesDisparo.DISPARODERECHA;
	private static ArrayList<DireccionesPersonaje> estados = new ArrayList<DireccionesPersonaje>();
	
	public Bala(Vector2 posicion) {
		super(posicion);
		Utiles.listener.add(this);
		textura = new Texture("sprites/bala1.png");
//		animacionHit = new Animacion(Recursos.ANIMACION_BALA_HIT,3,10f,10f);
	}
	
	public Bala(float x,float y) {
		this(new Vector2(x,y));
	}

	public void update() {
		if(((body.getPosition().x * Constantes.PPM)) >= (Constantes.V_WIDTH / 2) + (posicion.x * Constantes.PPM ) || (body.getPosition().x * Constantes.PPM) <= -((Constantes.V_WIDTH / 2) - (posicion.x * Constantes.PPM))) { //60
			remove = true;
		} else if(body.getPosition().y * Constantes.PPM >= (Constantes.V_HEIGHT / 2) +  (posicion.y * Constantes.PPM)) { //33.75
			remove = true;
		}
		if(hit) remove = true;
	}
	
	public void crearObjetoBox2D() {
		if(direccionDisparo == DireccionesDisparo.DISPARODERECHA) {
			bodyDef.position.set(posicion.x + Constantes.WIDTH_CUERPO_BOX2D, posicion.y + Constantes.WIDTH_CUERPO_BOX2D);
		} else if(direccionDisparo == DireccionesDisparo.DISPAROIZQUIERDA) {
			bodyDef.position.set(posicion.x - Constantes.WIDTH_CUERPO_BOX2D, posicion.y + Constantes.WIDTH_CUERPO_BOX2D);
		} else if(direccionDisparo == DireccionesDisparo.DISPAROARRIBA) {
			bodyDef.angle = 0.5f * 3.14f;
			bodyDef.position.set(posicion.x - 0.3f, posicion.y + Constantes.HEIGHT_CUERPO_BOX2D);
		}
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.gravityScale = 0f;
		body = Constantes.world.createBody(bodyDef);
		body.setLinearVelocity(velocidad);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.3f, 0.3f);
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = Constantes.BIT_BALA;
		fixtureDef.filter.maskBits = Constantes.BIT_SUELO | Constantes.BIT_ENEMIGO;
		body.createFixture(fixtureDef).setUserData(this);
	}

	public void dibujar(int i,DireccionesPersonaje direccion,ArrayList<Bala> balas) {
//		if(estados.size() != balas.size()) {	
//			estados.add(direccion );
//		}
//		if(estados.get(i) == DireccionesPersonaje.DERECHA) {
//			System.out.println(body.getPosition().x);
//			Render.batch.draw(textura, body.getPosition().x - (width/ 2f), body.getPosition().y - (height / 2f), (width/ 2f), (height / 2f), width, height, 1f, 1f,0f, 0, 0,textura.getWidth(),textura.getHeight(), false, false);
//		} else if(estados.get(i) == DireccionesPersonaje.IZQUIERDA){
//			Render.batch.draw(textura, body.getPosition().x - (width/ 2f), body.getPosition().y - (height / 2f), (width/ 2f), (height / 2f), width, height, 1f, 1f,180f, 0, 0,textura.getWidth(),textura.getHeight(), false, false);
//		} else if(estados.get(i) == DireccionesPersonaje.ARRIBA) {
			Render.batch.draw(textura, body.getPosition().x - (width/ 2f), body.getPosition().y - (height / 2f), (width/ 2f), (height / 2f), width, height, 1f, 1f,90f, 0, 0,textura.getWidth(),textura.getHeight(), false, false);
			
//			} 		
	}
	
	public void dibujar() {
		 Render.batch.draw(textura, posicion.x - width / 2,posicion.y - height / 2,width,height);
	}
	
	public void dibujarHit(float delta) {
		TextureRegion currentFrame = animacionHit.getFrame();
		Render.batch.draw(currentFrame, body.getPosition().x ,body.getPosition().y ,1, 1);
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	@Override
	public void right() {
		this.velocidad = new Vector2(velX,0f);
		direccionDisparo = DireccionesDisparo.DISPARODERECHA;
	}

	@Override
	public void left() {
		this.velocidad = new Vector2(-velX,0f);
		direccionDisparo = DireccionesDisparo.DISPAROIZQUIERDA;
	}

	@Override
	public void up() {
		this.velocidad = new Vector2(0, velY);
		direccionDisparo = DireccionesDisparo.DISPAROARRIBA;
		
	}

	@Override
	public void down() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isHit() {
		return hit;
	}
	public void setHit() {
		if(hit) return;
		hit = true;

	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public ArrayList<DireccionesPersonaje> getEstados() {
		return estados;
	}	
//	public Animacion getAnimacionHit() {
//		return animacionHit;
//	}
	
	public void setRemoverBala(boolean remove) {
		System.out.println("REMUEVO BALA");
		this.remove = remove;
	}
	
}
