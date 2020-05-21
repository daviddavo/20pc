package pr05b.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pr05b.mensajes.*;
import pr05b.modelo.Usuario;

/**
 * Implementa el interfaz Runnable o hereda de la clase
 * Thread, y será usada para llevar a cabo una escucha
 * continua en el canal de comunicación con el servidor,
 * en un hilo diferente.
 */
public class OyenteCliente extends Thread {
	Socket _socket;
	volatile Servidor _servidor;
	ObjectInputStream _ois;
	ObjectOutputStream _oos;
	
	public OyenteCliente(Socket socket, Servidor servidor) {
		super("OyenteCliente-"+socket.getPort());
		_socket = socket;
		_servidor = servidor;
		
	}
	
	@Override
	public void run() {
		boolean connected = true;
		try {
			_ois = new ObjectInputStream(_socket.getInputStream());
			_oos = new ObjectOutputStream(_socket.getOutputStream());
			
			while (connected) {
				Mensaje msg = (Mensaje) _ois.readObject();
				
				System.out.printf(">> %s de %s para %s%n",
					msg.getTipo().toString(), msg.getOrigen(), msg.getDestino());
				
				switch (msg.getTipo()) {
				case MENSAJE_CONEXION:
					System.out.printf("Nuevo cliente (%s) conectado desde %s%n", msg.getOrigen(), _socket.getInetAddress().getHostAddress());
					_servidor.connect(this, msg.getOrigen());
					_oos.writeObject(new ConexionConfirmacionMensaje(msg.getOrigen(), Servidor.SERVIDOR));
					break;
				case MENSAJE_LISTA_USUARIOS:
					_oos.writeObject(new ListaUsuariosConfirmacionMensaje(msg.getOrigen(), Servidor.SERVIDOR, _servidor.getListaUsuarios()));
					break;
				case MENSAJE_CERRAR_CONEXION:
					connected = false;
					_oos.writeObject(new CerrarConexionConfirmacionMensaje(msg.getOrigen(), Servidor.SERVIDOR));
					break;
				case MENSAJE_PEDIR_FICHERO:
				{
					// TODO Paso 1: Obtener el socket y el usuario asignado a dicho fichero
					String fname = ((PedirFicheroMensaje) msg).getFilename();
					Usuario u = _servidor.getUsuarioByFilename(fname);
					OyenteCliente oc = _servidor.getOyenteClienteByUsername(u.getUsername());
					// TODO Paso 2: Enviar el mensaje MENSAJE_EMITIR_FICHERO
					if (oc != null)
						oc._oos.writeObject(new EmitirFicheroMensaje(u.getUsername(), msg.getOrigen(), fname));
					else
						System.out.printf("User %s not connected%n", u.getUsername());
				}
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
				{
					// TODO: "Reenviar" mensaje (cambiando el tipo) al cliente que lo necesite
					PreparadoClienteServidorMensaje pcsmsg = (PreparadoClienteServidorMensaje) msg;
					OyenteCliente oc = _servidor.getOyenteClienteByUsername(pcsmsg.getDestino());
					oc._oos.writeObject(new PreparadoServidorClienteMensaje(pcsmsg.getDestino(), pcsmsg.getOrigen(),
						this._socket.getInetAddress(), pcsmsg.getPort(), pcsmsg.getFileName()));
				}
					break;
				// Mensajes no válidos (debería usarlos el cliente)
				case MENSAJE_CONFIRMACION_CONEXION:
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
				case MENSAJE_EMITIR_FICHERO:
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
				default:
					break;
				}
			}
			
			if (connected) {
				_ois.reset();
				_oos.reset();
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println(e);
		}
		
		_servidor.disconnect(this);
	}
}
