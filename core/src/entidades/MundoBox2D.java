package entidades;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import utiles.Constantes;
import utiles.MiContactListener;

public class MundoBox2D {
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Box2DDebugRenderer lineas;
	private Personaje[] personajes;
	private MiContactListener cl;
	private ArrayList<PowerUp> powerUp = new ArrayList<PowerUp>();
	private int levelWidth = 0, levelHeight = 0, tileWidth = 0, tileHeight = 0;
	private float timer = 4;
	private int iflag = 1;
	private Oleada[] oleadas;
	public MundoBox2D() {
		map = new TmxMapLoader().load("mapas/Mapita2.tmx"); 
		renderer = new OrthogonalTiledMapRenderer(map, 1 / Constantes.PPM);
		if(!Constantes.cooperativo) {
			Constantes.world = new World(new Vector2(0,-9.81f), true);
			MapProperties propiedades = map.getProperties();
			levelWidth = propiedades.get("width",Integer.class);
			levelHeight = propiedades.get("height", Integer.class);
			tileWidth = propiedades.get("tilewidth",Integer.class);
			tileHeight = propiedades.get("tileheight", Integer.class);
			cl = new MiContactListener();
			Constantes.world.setContactListener(cl);
			lineas = new Box2DDebugRenderer();
			new ObjetosTiledMap(Constantes.world, map);
			personajes = new Personaje[1];
			personajes[0] = new PersonajeAzul();
			Random r = new Random();
			for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				int random = r.nextInt(2);
				powerUp.add(((random == 1)? new AngelGuardian(new Vector2(rect.getX() / Constantes.PPM,rect.getY() / Constantes.PPM)) : new DisparoRapido(new Vector2(rect.getX() / Constantes.PPM,rect.getY() / Constantes.PPM))));
			}
			oleadas = new Oleada[9];
			for (int i = 0; i < oleadas.length; i++) {
				oleadas[i] = new Oleada();
				int random = r.nextInt(5) + 3;
				oleadas[i].setEnemigos(random);
			}
			oleadas[0].setPosicion(personajes[0].getPosicion());
			oleadas[0].crearEnemigos();		
		} else {
			personajes = new Personaje[2];
			for (int i = 0; i < personajes.length; i++) {
				personajes[i] = new PersonajeAzul();
			}	
		}	
	}
	
	public void cargarMapa(String fuente) {
		map = new TmxMapLoader().load(fuente);
	}
	
	public void update(float delta,OrthographicCamera camara) {
		renderer.setView(camara);
	}
	
	public void render(float delta) {
		renderer.render();
		for (int i = 0; i < personajes.length; i++) {
			personajes[i].render(delta);
		}
	}
	
	public void render(float delta,OrthographicCamera camara) {
		Constantes.world.step(1 / 60f, 6, 2);
		for (PowerUp power : powerUp) {
			power.update();
		}
		if (timer >= 0) {
			timer -= delta;
			if (timer <= 0 && iflag < oleadas.length) {
				oleadas[iflag].setPosicion(personajes[0].getPosicion());
				oleadas[iflag].crearEnemigos();
				timer = 4;
				iflag++;
			}
		}
		for (int i = 0; i < iflag; i++) {
			oleadas[i].update(delta);
		}
		renderer.render();
//		lineas.render(Constantes.world, camara.combined);
		personajes[0].render(delta);
		for (int i = 0; i < iflag; i++) {
			oleadas[i].render(delta);
		}
		for (PowerUp power : powerUp) {
			power.render(delta);
		}

	}
	public Personaje[] getPersonaje() {
		return personajes;
	}
	public MiContactListener getContactListener() {
		return cl;
	}
	public int getLevelHeight() {
		return levelHeight;
	}
	public int getLevelWidth() {
		return levelWidth;
	}
	public int getTileHeight() {
		return tileHeight;
	}
	public int getTileWidth() {
		return tileWidth;
	}
	
	public void dispose() {
		map.dispose();
		renderer.dispose();
//		lineas.dispose();
	}
}
