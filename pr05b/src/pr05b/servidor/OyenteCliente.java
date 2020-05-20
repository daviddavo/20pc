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
public class OyenteCliente extends Thread {
	private Socket _socket;
	private Servidor _servidor;
	
	public OyenteCliente(Socket socket, Servidor servidor) {
		super("OyenteServidor");
		_socket = socket;
		_servidor = servidor;
	}
	
	@Override
	public void run() {
		// TODO: Set user as disconnected on failure
		try (ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream());
			 ObjectOutputStream oos = new ObjectOutputStream(_socket.getOutputStream())) {
			while (true) {
				Mensaje msg = (Mensaje) ois.readObject();
				
				System.out.printf(">> %s de %s para %s%n",
					msg.getTipo().toString(), msg.getOrigen(), msg.getDestino());
				
				switch (msg.getTipo()) {
				case MENSAJE_CONEXION:
					System.out.printf("Nuevo cliente (%s) conectado desde %s%n", msg.getOrigen(), _socket.getInetAddress().getHostAddress());
					_servidor.connect(_socket, msg.getOrigen());
					oos.writeObject(new ConexionConfirmacionMensaje(msg.getOrigen(), Servidor.SERVIDOR));
					break;
				case MENSAJE_CERRAR_CONEXION:
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					break;
				case MENSAJE_EMITIR_FICHERO:
					break;
				case MENSAJE_LISTA_USUARIOS:
					oos.writeObject(new ListaUsuariosConfirmacionMensaje(msg.getOrigen(), Servidor.SERVIDOR, _servidor.getListaUsuarios()));
					break;
				case MENSAJE_PEDIR_FICHERO:
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					break;
				// Mensajes no válidos (debería usarlos el cliente)
				case MENSAJE_CONFIRMACION_CONEXION:
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
				default:
					break;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
		}
		
		_servidor.disconnect(_socket);
	}
}
