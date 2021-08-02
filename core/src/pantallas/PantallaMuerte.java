package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import utiles.Constantes;
import utiles.Recursos;
import utiles.Render;

public class PantallaMuerte extends PantallaBase {
	private Texture personaje;
	private Label label;
	private float width = 200, height = 200	;
	public PantallaMuerte() {
		ventanaGrafica = new FitViewport(Constantes.V_WIDTH, Constantes.V_HEIGHT);
		stage = new Stage(ventanaGrafica);
		Skin skin = new Skin(Gdx.files.internal("skin/skin2/comic-ui.json"));
		personaje = new Texture(Recursos.ANIMACION_IDLE);
		label = new Label("YOU DIED",skin,"title");
		label.setColor(Color.WHITE);
		label.setPosition(Constantes.V_WIDTH / 2 - label.getWidth() / 2, Constantes.V_HEIGHT / 2 - label.getHeight() / 2);
		stage.addActor(label);
	}
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void hide() {
		stage.clear();
	}
	
	@Override
	public void render(float delta) {	
		super.render(delta);
		stage.act();
		Render.batch.begin();
			Render.batch.draw(personaje, Constantes.V_WIDTH / 2 - width / 2, Constantes.V_HEIGHT / 2 - height / 2,width,height);
		Render.batch.end();
		stage.draw();
	}
}
