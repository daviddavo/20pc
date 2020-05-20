package pr05b.cliente;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Emisor extends Thread {
	private ServerSocket _serverSocket;
	private String _filename;
	private FileInputStream _file;
	
	public Emisor(String filename) throws IOException {
		_serverSocket = new ServerSocket(0);
		_filename = filename;
		_file = new FileInputStream(_filename);
	}
	
	public int getPort() {
		return _serverSocket.getLocalPort();
	}
	
	@Override
	public void run() {
		// No loop, only one file transferred
		try (Socket socket = _serverSocket.accept()) {
			_file.transferTo(socket.getOutputStream());
		} catch (IOException e) {
			System.err.printf("Failed to send file %s", _filename);
		}
	}
}
