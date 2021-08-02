package com.pruebas.prueba;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import pantallas.Menu;
import utiles.Utiles;
public class Prueba extends Game {
	@Override
	public void create () {
		Utiles.prueba = this;
		Utiles.manager = new AssetManager();
		Utiles.manager.load("sonidos/musica.ogg", Music.class);
		Utiles.manager.finishLoading();
		this.setScreen(new Menu());
		
	}

	@Override
	public void render () {
		super.render();
	}
	
	public void cambiarPantalla(Screen pantalla) {
		this.setScreen(pantalla);
	}
	
}
