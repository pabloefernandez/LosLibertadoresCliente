package enums;

public enum Estados {
	RUNNING("RUNNING"),
	STANDING("STANDING"),
	JUMPING("JUMPING"),
	MIRARRIBA("MIRARRIBA"),
	FALLING("FALLING"),
	DEAD("DEAD");
	
	private String nombre;
	
	private Estados(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
}
