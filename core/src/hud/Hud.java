package hud;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entidades.Personaje;
import utiles.Constantes;


public class Hud implements Disposable {
	private ProgressBar vida,vida2; 
	private Personaje[] personajes;
	private float value,value2;
	private Label powerUp1,powerUp2,vidas,vidas2;
	private Stage stage;
	private Viewport ventanaGrafica;
	private Skin skin;
	private int nroCliente;
    public Hud(Personaje[] personajes,int nroCliente) {
    	this.nroCliente = nroCliente;
    	this.personajes = personajes;
    	ventanaGrafica = new FitViewport(Constantes.V_WIDTH, Constantes.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(ventanaGrafica); 
        skin = new Skin(Gdx.files.internal("skin/skin2/comic-ui.json"));
        vida = new ProgressBar(0, 100, 1, false, skin);

        vida.setWidth(500);
		vida.setValue(100);
       
    	if(personajes.length < 2) {
			vida.setPosition(Constantes.V_WIDTH / 2 - vida.getWidth() / 2, Constantes.V_HEIGHT - vida.getHeight() / 2 - 35);     
    	} else {
			vida.setPosition(10, Constantes.V_HEIGHT - vida.getHeight() / 2 - 35);
			vida2 = new ProgressBar(0, 100, 1, false, skin);
			vida2.setWidth(500);
			vida2.setValue(100);
			vida2.setPosition(Constantes.V_WIDTH - vida2.getWidth() - 10,Constantes.V_HEIGHT - vida.getHeight() / 2 - 35);
			
			vidas2 = new Label("VIDAS: " + personajes[((nroCliente == 0)? 1 : 0)].getVidaActual() + "/" + personajes[((nroCliente == 0)? 1 : 0)].getVidasMaximo(),skin,"title");
			vidas2.setPosition(vida2.getX() + vida2.getWidth() / 2 - vidas2.getWidth() / 2 , vida2.getY() - vida2.getHeight() / 2 - vidas2.getHeight() / 2);
			powerUp2 = new Label("Power Up: " + ((personajes[((nroCliente == 0)? 1 : 0)].isTienePowerUp()) ? "SI" : "NO"), skin,"title");
			powerUp2.setColor(Color.GREEN);
			powerUp2.setPosition(vida2.getX() - powerUp2.getWidth() / 2 - 150,vida.getY() - powerUp2.getHeight() / 2 + 25);
			stage.addActor(vida2);
			stage.addActor(powerUp2);
			stage.addActor(vidas2);
    	}
    		
	    	powerUp1 = new Label("Power Up: " + ((personajes[((nroCliente == 0)? 0 : 1)].isTienePowerUp())? "SI" : "NO"), skin,"title");
	        powerUp1.setColor(Color.RED);
	        powerUp1.setPosition(vida.getX() + vida.getWidth() + powerUp1.getWidth() / 2,vida.getY() - powerUp1.getHeight() / 2  + 25);
	        powerUp1.setPosition(vida.getX() + vida.getWidth() + 10 ,vida.getY() - powerUp1.getHeight() / 2  + 25);
	    	vidas = new Label("VIDAS: " + personajes[((nroCliente == 0)? 0 : 1)].getVidaActual() + "/" + personajes[((nroCliente == 0)? 0 : 1)].getVidasMaximo(),skin,"title");
			vidas.toFront();
			vidas.setPosition(vida.getX() + vida.getWidth() / 2 - vidas.getWidth() / 2 , vida.getY() - vida.getHeight() / 2 - vidas.getHeight() / 2);	
	    	stage.addActor(vida);  
	        stage.addActor(powerUp1);
	        stage.addActor(vidas);
    	
    	
    }

    public void dibujar() {
    	stage.act();
    	if(Constantes.cooperativo) {
    		powerUp1.setText("Power Up: " + ((personajes[((nroCliente == 0)? 0 : 1)].isTienePowerUp())? "SI" : "NO"));
    		powerUp2.setText("Power Up: " + ((personajes[((nroCliente == 0)? 1 : 0)].isTienePowerUp())? "SI" : "NO"));
    		vidas.setText("VIDAS: " + + personajes[((nroCliente == 0)? 0 : 1)].getVidaActual() + "/" + personajes[((nroCliente == 0)? 0 : 1)].getVidasMaximo());
    		vidas2.setText("VIDAS: " + personajes[((nroCliente == 0)? 1 : 0)].getVidaActual() + "/" + personajes[((nroCliente == 0)? 1 : 0)].getVidasMaximo());
    		value = personajes[((nroCliente == 0)? 0 : 1)].getVidaActual() * 100 / personajes[((nroCliente == 0)? 0 : 1)].getVidasMaximo();
    		value2 = personajes[((nroCliente == 0)? 1 : 0)].getVidaActual() * 100 / personajes[((nroCliente == 0)? 1 : 0)].getVidasMaximo();
    		vida.setValue(value);
    		vida2.setValue(value2);
    	} else {
			powerUp1.setText("Power Up: " + ((personajes[0].isTienePowerUp()) ? "SI" : "NO"));
			value = personajes[0].getVidaActual() * 100 / personajes[0].getVidasMaximo();
			vida.setValue(value);
			vidas.setText("VIDAS: " + + personajes[0].getVidaActual() + "/" + personajes[0].getVidasMaximo());
    	}
    	
    	stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
    public Stage getStage() {
		return stage;
	}
    
}
