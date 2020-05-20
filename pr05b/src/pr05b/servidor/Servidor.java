package pr05b.servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pr05b.modelo.*;

/**
 * Clase principal de la aplicación servidor. Tendrá como 
 * atributo una o varias estructuras de datos que contendrán la
 * información de los usuarios. El servidor espera la llegada de
 * peticiones de inicio de sesión, y asocia un hilo de ejecución
 * con cada usuario.
 */
public class Servidor {
	public static final String SERVIDOR = "servidor";
	private static final String USAGE = "Usage: ./servidor [HOST:]PORT";
	private static final int EXIT_INCORRECT_ARGUMENTS = 1;
	private InetAddress _host;
	private int _port;
	private Map<String, Usuario> _mapUsuarios;
	private Map<Socket, Usuario> _mapSockets;
	private List<InfoFichero> _listaInformacion;
	
	public Servidor(InetAddress host, int port) {
		_host = host;
		_port = port;
		_mapSockets = new HashMap<Socket, Usuario>();
	}
	
	public void runOyente() {
		try (ServerSocket ss = new ServerSocket(_port, 0, _host)){
			System.out.printf("Server listening at %s:%d%n", ss.getInetAddress().getHostAddress(), ss.getLocalPort());
			while (true) {
				Socket socket = ss.accept();
				(new OyenteCliente(socket, this)).start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public List<Usuario> getListaUsuarios() {
		return new ArrayList<>(_mapUsuarios.values());
	}
	
	@SuppressWarnings("unchecked")
	public void readUsuarios(String filename) throws IOException {
		try (FileInputStream fis = new FileInputStream(filename);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			 _mapUsuarios = (Map<String, Usuario>) ois.readObject();
			 for (Usuario u: _mapUsuarios.values()) u.setDisconnected();
			 System.out.printf("Read %s%n", filename);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			System.out.printf("File %s not found, creating%n", filename);
			_mapUsuarios = new HashMap<String, Usuario>();
		}
	}
	
	public void writeUsuarios(String filename) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			for (Usuario u : _mapUsuarios.values()) u.setDisconnected();
			oos.writeObject(_mapUsuarios);
		}
		// We throw again the IOException, but with the try-with-resources
		// the streams are closed
	}
	
	@SuppressWarnings("unchecked")
	public void readInformacion(String filename) throws IOException {
		try (FileInputStream fis = new FileInputStream(filename);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			_listaInformacion = (List<InfoFichero>) ois.readObject();
			System.out.printf("Read %s%n", filename);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			System.out.printf("File %s not found, creating%n", filename);
			_listaInformacion = new ArrayList<InfoFichero>();
		}
	}
	
	public void writeInformacion(String filename) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filename);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(_listaInformacion);
		}
	}
	
	public static void main(String [] argv) throws UnknownHostException {		
		if (argv.length != 1) {
			System.err.println(USAGE);
			System.exit(EXIT_INCORRECT_ARGUMENTS);
		}
		
		InetAddress host;
		Integer port;
		String [] aux = argv[0].split(":");
		
		if (aux.length == 2) {
			host = InetAddress.getByName(aux[0]);
			port = Integer.parseInt(aux[1]);
		} else {
			host = InetAddress.getLocalHost();
			port = Integer.parseInt(aux[0]);
		}
		
		Servidor s = new Servidor(host, port);
		
		// Escribimos los datos cuando la aplicación se para
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Saving data...");
				try {
					s.writeUsuarios("listausuarios.ser");
					s.writeInformacion("infoficheros.ser");
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		});
		
		try {
			s.readUsuarios("listausuarios.ser");
			s.readInformacion("infoficheros.ser");
			s.runOyente();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void connect(Socket socket, String origen) {
		Usuario u = new Usuario(origen, socket.getInetAddress());
		u.setConnected();
		_mapSockets.put(socket, u);
		_mapUsuarios.put(origen, u);
	}

	public void disconnect(Socket socket) {
		System.out.printf("%s disconnected%n", _mapSockets.get(socket).getUserName());
		_mapSockets.get(socket).setDisconnected();
		_mapSockets.remove(socket);
	}
}
