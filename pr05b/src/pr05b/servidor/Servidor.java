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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

	// Usamos este lock en las funciones de escritura (connect y disconnect)
	// que además usan ambos hashmap.
	private final Lock _mcLock;
	private ConcurrentHashMap<String, Usuario> _mapUsuarios;
	private ConcurrentHashMap<OyenteCliente, Usuario> _mapClientes;
	
	public Servidor(InetAddress host, int port) {
		_host = host;
		_port = port;
		_mapClientes = new ConcurrentHashMap<>();
		_mcLock = new ReentrantLock();
		_mapUsuarios = new ConcurrentHashMap<>();
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
	private void readUsuarios(String filename) throws IOException {
		try (FileInputStream fis = new FileInputStream(filename);
			 ObjectInputStream ois = new ObjectInputStream(fis)) {
			 _mapUsuarios = new ConcurrentHashMap<>((Map<String, Usuario>) ois.readObject());
			 for (Usuario u: _mapUsuarios.values()) u.setDisconnected();
			 System.out.printf("Read %s%n", filename);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			System.out.printf("File %s not found, creating%n", filename);
			_mapUsuarios = new ConcurrentHashMap<String, Usuario>();
		}
	}
	
	private void writeUsuarios(String filename) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			for (Usuario u : _mapUsuarios.values()) u.setDisconnected();
			oos.writeObject(_mapUsuarios);
		}
		// We throw again the IOException, but with the try-with-resources
		// the streams are closed
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
			host = null;
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
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		});
		
		try {
			s.readUsuarios("listausuarios.ser");
			s.runOyente();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public Usuario getUsuarioByFilename(String filename) {
		return _mapUsuarios.searchValues(2, u->
			u.getInfoFicheros().stream().anyMatch(f->f.getPath().equalsIgnoreCase(filename))?u:null
		);
	}
	
	public OyenteCliente getOyenteClienteByUsername(String username) {
		return _mapClientes.search(4, (k,u)->u.getUsername().equalsIgnoreCase(username)?k:null);
	}

	public void connect(OyenteCliente oc, String origen) {
		_mcLock.lock();
		try {
			Usuario usuario = _mapUsuarios.getOrDefault(origen, new Usuario(origen, oc._socket.getInetAddress()));
			usuario.setConnected();
			_mapClientes.put(oc,  usuario);
			_mapUsuarios.put(usuario.getUsername(), usuario);
			System.out.printf("%s connected%n", usuario.getUsername());
		} finally {
			_mcLock.unlock();
		}
	}

	public void disconnect(OyenteCliente oc) {
		_mcLock.lock();
		try {
			Usuario usuario = _mapClientes.get(oc);
			_mapClientes.remove(oc).setDisconnected();
			_mapUsuarios.put(usuario.getUsername(), usuario);
			System.out.printf("%s disconnected%n", usuario.getUsername());
		} finally {
			_mcLock.unlock();
		}
	}

	public void addInfoFichero(String origen, InfoFichero infoFichero) {
		_mapUsuarios.get(origen).getInfoFicheros().add(infoFichero);
	}
}
