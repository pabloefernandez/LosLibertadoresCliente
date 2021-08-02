package pantallas;


import com.badlogic.gdx.Gdx;

import Input.KeysListener;
import red.HiloCliente;
import utiles.Constantes;

public class Pantalla extends PantallaJuego {

	public Pantalla() {
		if(Constantes.cooperativo) {
			hc = new HiloCliente(this);
			hc.start();
			teclas = new KeysListener(hc);
		} else {
			teclas = new KeysListener();
		}
		Gdx.input.setInputProcessor(teclas);
	}
}
