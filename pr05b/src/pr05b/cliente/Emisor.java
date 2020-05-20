package pr05b.cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {
	private ServerSocket _serverSocket;
	
	public Emisor(String filename) throws IOException {
		_serverSocket = new ServerSocket(0);
	}
	
	public int getPort() {
		return _serverSocket.getLocalPort();
	}
	
	@Override
	public void run() {
		// No loop, only one file transferred
		try (Socket socket = _serverSocket.accept()) {
			
		}
	}
}
