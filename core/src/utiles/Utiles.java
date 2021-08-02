package utiles;

import java.util.ArrayList;
import java.util.EventListener;

import com.badlogic.gdx.assets.AssetManager;
import com.pruebas.prueba.Prueba;

import pantallas.Menu;
import pantallas.PreferenciasMenu;

public abstract class Utiles {
	public static Prueba prueba;
	public static Menu menu;
	public static PreferenciasMenu preferenciasMenu;
	public static ArrayList<EventListener> listener = new ArrayList<EventListener>();
	public static AssetManager manager;
}
