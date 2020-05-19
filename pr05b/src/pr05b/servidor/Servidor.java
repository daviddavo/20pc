package pr05b.servidor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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
	
	public Servidor(InetAddress host, int port) {
		_host = host;
		_port = port;
	}
	
	public void runOyente() {
		try (ServerSocket ss = new ServerSocket(_port, 0, _host)){
			System.out.printf("Server listening at %s:%d%n", ss.getInetAddress().getHostAddress(), ss.getLocalPort());
			while (true) {
				Socket socket = ss.accept();
				(new OyenteServidor(socket, this)).start();
			}
		} catch (IOException e) {
			System.err.println(e);
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
		s.readUsuarios();
		s.readInformacion();
		s.runOyente();
		s.writeFiles();
	}
}
