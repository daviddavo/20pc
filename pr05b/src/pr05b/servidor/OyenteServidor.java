package pr05b.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pr05b.mensajes.*;

/**
 * Implementa el interfaz Runnable o hereda de la clase
 * Thread, y será usada para llevar a cabo una escucha
 * continua en el canal de comunicación con el servidor,
 * en un hilo diferente.
 */
public class OyenteServidor extends Thread {
	private Socket _socket;
	private static final String SERVIDOR = "servidor";
	
	public OyenteServidor(Socket socket) {
		_socket = socket;
	}
	
	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream());
			 ObjectOutputStream oos = new ObjectOutputStream(_socket.getOutputStream())) {
			while (true) {
				Mensaje msg = (Mensaje) ois.readObject();
				
				switch (msg.getTipo()) {
				case MENSAJE_CONEXION:
					System.out.printf("Nuevo cliente (%s) conectado desde %s%n", msg.getOrigen(), _socket.getInetAddress().getHostAddress());
					oos.writeObject(new ConexionConfirmacionMensaje(msg.getOrigen(), SERVIDOR));
					break;
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
					break;
				case MENSAJE_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
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
				// Mensajes no válidos (debería usarlos el cliente)
				case MENSAJE_CONFIRMACION_CONEXION:
				default:
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
		}
	}
}
