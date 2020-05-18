package ficheros;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
			System.out.println("Waiting for input");
			Socket socket = serverSocket.accept();
			System.out.println("Connection stablished");
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			FileInfoRequest fir = (FileInfoRequest) ois.readObject();
			System.out.println("Should send " + fir.getFileName());
			
			File file = new File(fir.getFileName());
			InputStream fis = new FileInputStream(file);
			
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new FileInfoResponse(fir.getFileName(), "", file.length()));
			
			System.out.println("Trying to send file");
			OutputStream fos = socket.getOutputStream();
			
			int bytes;
			byte[] buf = new byte[4*1024];
			while ((bytes = fis.read(buf)) > 0) {
				fos.write(buf, 0, bytes);
				System.out.printf("Sent %d bytes%n", bytes);
			}
			
			fis.close();
			socket.close();
			
			System.out.println("Server closed");
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
