package pr05b.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import pr05b.mensajes.Mensaje;

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
	private Socket _socket;
	
	public OyenteCliente(Socket socket) {
		_socket = socket;
	}
	
	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream())){
			while (true) {
				Mensaje msg = (Mensaje) ois.readObject();
				
				switch (msg.getTipo()) {
				case MENSAJE_CONFIRMACION_CONEXION:
					System.out.printf("Succesfully connected to %s%n", _socket.getInetAddress().getHostAddress());
					break;
				case MENSAJE_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
					break;
				case MENSAJE_EMITIR_FICHERO:
					break;
				case MENSAJE_LISTA_USUARIOS:
					break;
				case MENSAJE_PEDIR_FICHERO:
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					break;
				// Mensajes no válidos (debería usarlos el servidor)
				case MENSAJE_CONEXION:
				default:
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			
		}
	}
}
