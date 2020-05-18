package ficheros;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServidorFicheros {
	private static final String USAGE = "Usage: ./servidor [HOST] [PORT] [folder]";
	private static final int EXIT_INCORRECT_ARGUMENTS = 1;
	
	public static void main(String [] argv) throws UnknownHostException {
		if (argv.length != 2) {
			System.err.println(USAGE);
			System.exit(EXIT_INCORRECT_ARGUMENTS);
		}
		
		InetAddress host = InetAddress.getByName(argv[0]);
		Integer port = Integer.parseInt(argv[1]);
		
		// It's networking time
		// Try-with-resources auto-closes the socket if something
		// goes wrong
		try (ServerSocket serverSocket = new ServerSocket(port, 1024, host)) {
			while (true) {
				System.out.println("Waiting for input");
				Socket socket = serverSocket.accept();
				Thread t = new ThreadServidor(socket);
				t.start();
			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
