package pr05b.cliente;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import pr05b.mensajes.*;
import pr05b.mensajes.Mensaje.Tipo;
import pr05b.modelo.*;
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
public class OyenteServidor extends Thread {
	private static final int TIMEOUT_MILLIS = 10*1000;
	private Socket _socket;
	private ObjectOutputStream _oos;
	private String _username;
	private Map<Tipo, Boolean> _expects;
	private List<Usuario> _listaUsuarios;
	
	public OyenteServidor(Socket socket, String username) throws IOException {
		super("OyenteServidor-"+username);
		_socket = socket;
		_oos = new ObjectOutputStream(_socket.getOutputStream());
		_username = username;
		_expects = new EnumMap<>(Tipo.class);
	}
	
	/*
	 * Nota: Asumimos que siempre hay un Cliente para un OyenteCliente, de no ser
	 * así habría que incluir números de secuencia en los mensajes para ver a quién
	 * pertenece cada respuesta, además de usar locks y variables condicionales.
	 */
	private synchronized boolean _waitCommon(Mensaje msg, Tipo expectedResponse) throws IOException {
		long endTimeout = System.currentTimeMillis() + TIMEOUT_MILLIS;
		_expects.put(expectedResponse, true);
		_oos.writeObject(msg);
		try {
			while(_expects.get(expectedResponse) && System.currentTimeMillis() < endTimeout) wait(TIMEOUT_MILLIS);
		} catch (Exception e) {
			System.err.println(e);
		}
		
		return _expects.get(expectedResponse);
	}
	
	public synchronized boolean waitSendConexion() throws IOException {
		return _waitCommon(new ConexionMensaje(Servidor.SERVIDOR, _username), Tipo.MENSAJE_CONFIRMACION_CONEXION);
	}
	
	public synchronized List<Usuario> waitListaUsuarios() throws IOException {
		if (_waitCommon(new ListaUsuariosMensaje(Servidor.SERVIDOR, _username), Tipo.MENSAJE_CONFIRMACION_LISTA_USUARIOS)) {
			return null;
		} else {
			return _listaUsuarios;
		}
	}
	
	public synchronized boolean waitDisconnect() throws IOException {
		return _waitCommon(new CerrarConexionMensaje(Servidor.SERVIDOR, _username), Tipo.MENSAJE_CONFIRMACION_CERRAR_CONEXION);
	}
	
	public synchronized boolean waitPedirFichero(String filename) throws IOException {
		return _waitCommon(new PedirFicheroMensaje(Servidor.SERVIDOR, _username, filename), Tipo.MENSAJE_PREPARADO_SERVIDORCLIENTE);
	}
	
	@Override
	public void run() {
		boolean connected = true;
		try (ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream()); ){
			while (connected) {
				Mensaje msg = (Mensaje) ois.readObject();
				
				switch (msg.getTipo()) {
				case MENSAJE_CONFIRMACION_CONEXION:
					synchronized (this) {
						_expects.put(msg.getTipo(), false);
						notifyAll();
					}
					break;
				case MENSAJE_CONFIRMACION_LISTA_USUARIOS:
					synchronized (this) {
						_listaUsuarios = ((ListaUsuariosConfirmacionMensaje) msg).getListaUsuarios();
						_expects.put(msg.getTipo(), false);
						notifyAll();
					}
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					connected = false;
					synchronized (this) {
						_expects.put(msg.getTipo(), false);
						notifyAll();
					}
					break;
				case MENSAJE_EMITIR_FICHERO:
				{
					// TODO: Paso 1. Crear proceso emisor (serversocketsiesnecesario?)
					String filename = ((EmitirFicheroMensaje) msg).getFilename();
					try {
						Emisor e = new Emisor(filename);
						e.start();
						// TODO: Paso 2. Enviar mensaje con puerto del emisor (MENSAJE_PREPARADO_CLIENTESERVIDOR)
						_oos.writeObject(new PreparadoClienteServidorMensaje(
								msg.getOrigen(), _username,_socket.getLocalAddress(), e.getPort(), filename));
					} catch (FileNotFoundException e) {
						System.err.printf("%s tried to request file %s, but it's not available%n",
							msg.getOrigen(), filename);
					}
				}
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					// TODO: Paso 1. Crear proceso receptor, conectando a ip y puerto que ha llegado
					PreparadoServidorClienteMensaje pscmsg = (PreparadoServidorClienteMensaje) msg;
					Receptor r = new Receptor(pscmsg.getAddress(), pscmsg.getPort(), pscmsg.getFilename());
					r.start();
					// No es necesario que el emisor/receptor envie mensaje, se hace con .accept()
					synchronized (this) {
						_expects.put(msg.getTipo(), false);
						notifyAll();
					}
					break;
				// Mensajes no válidos (debería usarlos el servidor)
				case MENSAJE_PEDIR_FICHERO:
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
				case MENSAJE_CONEXION:
				case MENSAJE_LISTA_USUARIOS:
				case MENSAJE_CERRAR_CONEXION:
				default:
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error processing received message");
			System.err.println(e);
		}
	}
}
