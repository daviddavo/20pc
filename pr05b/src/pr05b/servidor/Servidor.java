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
	private static final String USAGE = "Usage: ./servidor [HOST] PORT";
	private static final int EXIT_INCORRECT_ARGUMENTS = 1;
	
	public void main(String [] argv) throws UnknownHostException {		
		if (argv.length < 1 || argv.length > 2) {
			System.err.println(USAGE);
			System.exit(EXIT_INCORRECT_ARGUMENTS);
		}
		
		InetAddress host;
		Integer port;
		if (argv.length == 1) {
			host = InetAddress.getByName(argv[0]);
			port = Integer.parseInt(argv[1]);
		} else {
			host = InetAddress.getLocalHost();
			port = Integer.parseInt(argv[0]);
		}
		
		try (ServerSocket ss = new ServerSocket(port, 0, host)){
			while (true) {
				Socket socket = ss.accept();
				(new OyenteServidor(socket)).start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
