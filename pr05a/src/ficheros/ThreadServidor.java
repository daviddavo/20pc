package ficheros;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;

class ThreadServidor extends Thread {
	private Socket _socket;
	
	ThreadServidor(Socket socket) {
		_socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// Reading request
			System.out.println("Connection stablished");
			ObjectInputStream ois = new ObjectInputStream(_socket.getInputStream());
			
			FileInfoRequest fir = (FileInfoRequest) ois.readObject();
			System.out.println("Should send " + fir.getFileName());
			
			File file = new File(fir.getFileName());
			InputStream fis = new FileInputStream(file);
			
			System.out.println("Trying to send file");
			OutputStream fos = _socket.getOutputStream();
			
			// Sending response
			int bytes;
			byte[] buf = new byte[4*1024];
			while ((bytes = fis.read(buf)) > 0) {
				fos.write(buf, 0, bytes);
				System.out.printf("Sent %d bytes%n", bytes);
			}
			
			fis.close();
			_socket.close();
			
			System.out.println("Server closed");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
