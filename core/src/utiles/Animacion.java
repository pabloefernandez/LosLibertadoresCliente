package utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animacion {
	private TextureRegion[] frames;
	private float tiempo;
	private float delay;
	private int currentFrame;
	private float width = 3f , height = 6;
	private int vecesEjecutada = 0;
	
	public Animacion(String ruta,int columnas,float width,float height) {
		Texture tex = new Texture(ruta);
		this.width = width;
		this.height = height;
		TextureRegion[] sprites = TextureRegion.split(tex, tex.getWidth() / columnas , tex.getHeight())[0];
		setFrames(sprites);
	}

	public void setDelay(float f) { 
		delay = f; 
	}
	public void setFrames(TextureRegion[] frames) {
		setFrames(frames, 1 / 12f);
	}
	public void setFrames(TextureRegion[] frames, float delay) {
		this.frames = frames;
		tiempo = 0;
		currentFrame = 0;
		this.delay = delay;
	}
	
	public void update(float dt) {
		if(delay <= 0) return;
		tiempo += dt;
		while(tiempo >= delay) {
			ejecucion();
		}
	}
	
	private void ejecucion() {
		tiempo -= delay;
		currentFrame++;
		if(currentFrame == frames.length) {
			currentFrame = 0;
			vecesEjecutada++;
		}
	}
	
	public TextureRegion getFrame() { 
		return frames[currentFrame]; 
	}
	public int getVecesEjecutada() { 
		return vecesEjecutada; 
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
}
