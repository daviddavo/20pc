package pr05b.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pr05b.mensajes.*;
import pr05b.servidor.Servidor;

/**
 * Implementa el interfaz Runnable o hereda de la clase Thread
 * y es usada para proporcionar concurrencia respecto a las 
 * sesiones de cada usuario con el servidor. El método run se limita
 * a hacer lecturas del flujo de entrada correspondiente, realizar
 * las acciones oportunas, y devolver los resultados en forma de mensajes
 * que serán enviados al usuario o usuarios involucrados
 * @author davo
 *
 */
public class OyenteCliente extends Thread {
	private static final int TIMEOUT_MILLIS = 10*1000;
	private Socket _socket;
	private ObjectOutputStream _oos;
	private String _username;
	private boolean _conectando;
	private boolean _esperandoListaUsuarios;
	
	public OyenteCliente(Socket socket, String username) throws IOException {
		super("OyenteCliente");
		_socket = socket;
		_oos = new ObjectOutputStream(_socket.getOutputStream());
		_username = username;
	}
	
	/*
	 * Nota: Asumimos que siempre hay un Cliente para un OyenteCliente, de no ser
	 * así habría que incluir números de secuencia en los mensajes para ver a quién
	 * pertenece cada respuesta
	 */
	public synchronized void waitSendConexion() throws IOException {
		long endTimeout = System.currentTimeMillis() + TIMEOUT_MILLIS;
		_conectando = true;
		_oos.writeObject(new ConexionMensaje(Servidor.SERVIDOR, _username));
		try {
			while (_conectando && System.currentTimeMillis() < endTimeout) wait(TIMEOUT_MILLIS);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		if (_conectando) {
			System.err.println("No se recibió un mensaje de confirmación de conexión");
		}
	}
	
	public synchronized void waitListaUsuarios() throws IOException {
		long endTimeout = System.currentTimeMillis() + TIMEOUT_MILLIS;
		_esperandoListaUsuarios = true;
		_oos.writeObject(new ListaUsuariosMensaje(Servidor.SERVIDOR, _username));
		System.out.println("Mandando peticion lista usuarios");
		try {
			while (_esperandoListaUsuarios && System.currentTimeMillis() < endTimeout) wait(TIMEOUT_MILLIS);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		if (_esperandoListaUsuarios) {
			System.err.println("No se recibió la lista de usuarios");
		}
	}
	
	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream()); ){
			while (true) {
				Mensaje msg = (Mensaje) ois.readObject();
				
				switch (msg.getTipo()) {
				case MENSAJE_CONFIRMACION_CONEXION:
					synchronized (this) {
						System.out.printf("Succesfully connected to %s%n", _socket.getInetAddress().getHostAddress());
						_conectando = false;
						notifyAll();
					}
					break;
				case MENSAJE_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
					break;
				case MENSAJE_EMITIR_FICHERO:
					break;
				case MENSAJE_PEDIR_FICHERO:
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					break;
				// Mensajes no válidos (debería usarlos el servidor)
				case MENSAJE_CONEXION:
				case MENSAJE_LISTA_USUARIOS:
				default:
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			
		}
	}
}
