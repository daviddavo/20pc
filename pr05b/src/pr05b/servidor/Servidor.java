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
	// Nota: Podríamos crear un lock para acceder al hashmap
	// (al fin y al cabo es el problema de los readers-writers)
	// Como ya lo hemos hecho en prácticas anteriores, no mostramos
	// la implementación y usamos directamente ConcurrentHashMap que
	// tiene optimizaciones de bajo nivel, y el bloqueo se realiza a nivel
	// de tabla y permite incluso escrituras concurrentes a distintas claves
	// (tendríamos que crear nuesttra propia clase Map para hacerlo
	private ConcurrentHashMap<String, Usuario> _mapUsuarios;
	private ConcurrentHashMap<OyenteCliente, Usuario> _mapClientes;
	private List<InfoFichero> _listaFicheros;
	
	public Servidor(InetAddress host, int port) {
		_host = host;
		_port = port;
		_mapClientes = new ConcurrentHashMap<>();
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
			_listaFicheros = (List<InfoFichero>) ois.readObject();
			System.out.printf("Read %s%n", filename);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {
			System.out.printf("File %s not found, creating%n", filename);
			_listaFicheros = new ArrayList<InfoFichero>();
		}
	}
	
	public void writeInformacion(String filename) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(filename);
			 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(_listaFicheros);
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
			// TODO: Delete this
			// s._mapUsuarios.get("davo").getInfoFicheros().add(new InfoFichero("fichero1.txt", -1));
			// s._mapUsuarios.get("juan").getInfoFicheros().add(new InfoFichero("fichero2.txt", -1));
			s.readInformacion("infoficheros.ser");
			s.runOyente();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	public Usuario getUsuarioByFilename(String filename) {
		for (Usuario u : _mapUsuarios.values()) {
			for (InfoFichero f : u.getInfoFicheros()) {
				if (f.path.equals(filename)) return u;
			}
		}
		
		return null;
	}
	
	public OyenteCliente getOyenteClienteByUsername(String username) {
		for (Map.Entry<OyenteCliente, Usuario> e : _mapClientes.entrySet()) {
			if (e.getValue().getUserName().equals(username)) return e.getKey();
		}
		
		return null;
	}

	// TODO: Hay race conditions y además
	// Aunque sea concurrenthashmap no se actualiza la referencia
	// TODO: Preguntar sobre esto
	public void connect(OyenteCliente oc, String origen) {
		Usuario u = _mapUsuarios.get(origen); 
		if (u == null) {
			u = new Usuario(origen, oc._socket.getInetAddress());
			_mapUsuarios.put(origen, u);
		}
		
		_mapClientes.put(oc, u);
		u.setConnected();
	}

	public void disconnect(OyenteCliente oc) {
		Usuario u = _mapClientes.get(oc);
		u.setDisconnected();
		_mapClientes.remove(oc);
		// assert(!u.isConnected());
		// assert(!_mapUsuarios.get(u.getUserName()).isConnected());
		System.out.printf("%s disconnected%n", u.getUserName());
	}
}
