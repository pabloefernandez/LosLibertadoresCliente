package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import com.badlogic.gdx.Gdx;
import pantallas.PantallaJuego;
import utiles.Constantes;

public class HiloCliente extends Thread {
	private DatagramSocket conexion;
	private InetAddress ipServer;
	private int puerto = 5000;
	private boolean fin = false;
	private PantallaJuego app;
	public HiloCliente(PantallaJuego app) {
		this.app = app;
		try {
			ipServer = InetAddress.getByName("192.168.0.31"); //InetAddress.getByName("192.168.0.31")
			conexion = new DatagramSocket();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		enviarMensaje("Conexion");
	}
	
	public void enviarMensaje(String mensaje) {
		byte[] data = mensaje.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length,ipServer,puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				conexion.receive(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			procesarMensaje(dp);
		} while (!fin);
	}

	private void procesarMensaje(final DatagramPacket dp) {
		Gdx.app.postRunnable(new Runnable() {	
			@Override
			public void run() {
				String msj = (new String(dp.getData())).trim();
				String mensajeParametrizado[] = msj.split("\\*");

				if(mensajeParametrizado.length < 2) {
					if(msj.equals("OK")) {
						ipServer = dp.getAddress();
					} else if(msj.equals("Empieza")) {
						Constantes.empiezaPartida = true;
					}
				} else {
					for (int i = 0; i < app.getMundo().getPersonaje().length; i++) {
						if (mensajeParametrizado[0].equals("Actualizar")) {
							if (mensajeParametrizado[1].equals("" + i)) {
								if (mensajeParametrizado[2].equals("Posicion")) {
									float posX = Float.parseFloat(mensajeParametrizado[3]);
									float posY = Float.parseFloat(mensajeParametrizado[4]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									app.setPosicion(posX, posY, indicePersonaje);
								} else if (mensajeParametrizado[2].equals("Velocidad")) {
									float velX = Float.parseFloat(mensajeParametrizado[3]);
									float velY = Float.parseFloat(mensajeParametrizado[4]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									app.setVelocidad(velX, velY, indicePersonaje);
								} else if (mensajeParametrizado[2].equals("Estado")) {
									boolean muerto = Boolean.parseBoolean(mensajeParametrizado[3]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									app.setMuertoTemporal(muerto,indicePersonaje);
								} else if (mensajeParametrizado[2].equals("Vida")) {
									int vida = Integer.parseInt(mensajeParametrizado[3]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									app.setVida(vida, indicePersonaje);
								} else if(mensajeParametrizado[2].equals("Dibujar")) {
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									app.setMuertoFinal(indicePersonaje);
								}
							} else if (mensajeParametrizado[2].equals("Bala")) { 
								if (mensajeParametrizado[3].equals("Posicion")) {
									float posX = Float.parseFloat(mensajeParametrizado[4]);
									float posY = Float.parseFloat(mensajeParametrizado[5]);
									int indiceBala = Integer.parseInt(mensajeParametrizado[6]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									System.out.println("ACTUALIZO LA POSICION DE LA BALA EN LOS DOS : " + indiceBala);
									app.setPosicionBala(posX, posY, indiceBala,indicePersonaje);
									
								} else if (mensajeParametrizado[3].equals("Remover")) {
									boolean remover = Boolean.parseBoolean(mensajeParametrizado[4]);
									int indiceBala = Integer.parseInt(mensajeParametrizado[5]);
									int indicePersonaje = Integer.parseInt(mensajeParametrizado[1]);
									System.out.println("REMUEVO LA BALA " + indiceBala);
									app.setRemoverBala(remover, indiceBala,indicePersonaje);
								} 
							}
						} 
					} 
					 if(mensajeParametrizado[0].equals("Cantidad")) {
						if(mensajeParametrizado[1].equals("Oleadas")) {
							int cantidadOleadas = Integer.parseInt(mensajeParametrizado[2]);
							int cantidadZombies = Integer.parseInt(mensajeParametrizado[3]);
							int oleada = Integer.parseInt(mensajeParametrizado[4]);
							app.setCantidadOleadas(cantidadOleadas,cantidadZombies,oleada);
						} 
					} else if(mensajeParametrizado[0].equals("Crear")) {
						if(mensajeParametrizado[1].equals("Bala")) {
							float posX = Float.parseFloat(mensajeParametrizado[2]);
							float posY = Float.parseFloat(mensajeParametrizado[3]);
							int indicePersonaje = Integer.parseInt(mensajeParametrizado[4]);
							app.agregarBala(posX, posY,indicePersonaje);
						} else if (mensajeParametrizado[1].equals("Zombies")) {
							float posX = Float.parseFloat(mensajeParametrizado[2]);
							float posY = Float.parseFloat(mensajeParametrizado[3]);
							int zombie = Integer.parseInt(mensajeParametrizado[4]);
							int oleada = Integer.parseInt(mensajeParametrizado[5]);
							app.crearZombies(posX, posY, zombie,oleada);	
						}
					} else if (mensajeParametrizado[0].equals("Cliente")) {
						int nroCliente = Integer.parseInt(mensajeParametrizado[1]);
						app.setNroCliente(nroCliente);
					} else if (mensajeParametrizado[0].equals("Actualizar")) {
						if(mensajeParametrizado[1].equals("Camara")) {
							float posXCamara = Float.parseFloat(mensajeParametrizado[2]);
							app.setPosXCamara(posXCamara);
						} else if(mensajeParametrizado[1].equals("Zombies")) {
							if(mensajeParametrizado[2].equals("Posicion")) {
								float posX = Float.parseFloat(mensajeParametrizado[3]);
								float posY = Float.parseFloat(mensajeParametrizado[4]);
								int zombie = Integer.parseInt(mensajeParametrizado[5]);
								int oleada = Integer.parseInt(mensajeParametrizado[6]);
								app.setPosicionZombies(posX, posY, zombie, oleada);
							} else if (mensajeParametrizado[2].equals("Velocidad")) {
								float velX = Float.parseFloat(mensajeParametrizado[3]);
								float velY = Float.parseFloat(mensajeParametrizado[4]);
								int zombie = Integer.parseInt(mensajeParametrizado[5]);
								int oleada = Integer.parseInt(mensajeParametrizado[6]);
								app.setVelocidadZombies(velX, velY, zombie,oleada);
							} else if (mensajeParametrizado[2].equals("Destruidos")) {
								boolean destruido = Boolean.parseBoolean(mensajeParametrizado[3]);
								int zombie = Integer.parseInt(mensajeParametrizado[4]);
								int oleada = Integer.parseInt(mensajeParametrizado[5]);
								app.setZombieDestruido(destruido, zombie,oleada);
							}
						}
					} 
				}	
			}
		});
	}
}
