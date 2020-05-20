package pr05b.cliente;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Receptor extends Thread {
	Socket _socket;
	String _filename;
	FileOutputStream _file;
	
	public Receptor(InetAddress address, int port, String filename) throws IOException {
		_socket = new Socket(address, port);
		_filename = filename;
		_file = new FileOutputStream(_filename);
	}
	
	@Override
	public void run() {
		try {
			_socket.getInputStream().transferTo(_file);
			System.out.printf("Finished downloading of file %s%n", _filename);
		} catch (IOException e) {
			System.err.printf("Failed to get file %s%n", _filename);
			System.err.println(e);
		}
	}
}
