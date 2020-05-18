package ficheros;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClienteFicheros {
	private static final String USAGE = "Usage: ./cliente [HOST] [PORT] [remote] [local]";
	private static final int EXIT_INCORRECT_ARGUMENTS = 1;
	
	public static void main(String [] argv) {
		if (argv.length != 4) {
			System.err.println(USAGE);
			System.exit(EXIT_INCORRECT_ARGUMENTS);
		}
		
		String host = argv[0];
		Integer port = Integer.parseInt(argv[1]);
		String remoteFname = argv[2];
		String localFname = argv[3];
		
		// It's networking time
		// Try-with-resources auto-closes the socket if something goes wrong
		try (Socket socket = new Socket(host, port)) {
			System.out.println("Client: Connection stablished");
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			
			oos.writeObject(new FileInfoRequest(remoteFname));
			
			System.out.println("Client: Waiting response");
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			FileInfoResponse fir = (FileInfoResponse) ois.readObject();
			System.out.printf("Response: fname: %s, size: %d%n", fir.getFilename(), fir.getSize());
			
			InputStream is = socket.getInputStream();
			File file = new File(localFname);
			OutputStream fos = new FileOutputStream(localFname);
			
			int bytes;
			byte[] buf = new byte[4*1024];
			while ((bytes = is.read(buf)) > 0) {
				fos.write(buf, 0, bytes);
				System.out.printf("Read %d bytes%n", bytes);
			}
			
			fos.close();
			socket.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		
		System.out.println("Client finished");
	}
}
