package pr05b.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import pr05b.cliente.comandos.Command;
import pr05b.cliente.comandos.CommandFactory;
import pr05b.servidor.Servidor;

/**
 * Clase principal de la aplicación cliente. Tendrá al menos los siguientes
 * atributos: nombre de usuario, dirección ip de la máquina. Puedes tener también
 * como atributos los objetos que proporcionan la comunicación con el servidor
 * (socket y streams). Es responsable de llevar a cabo todas las comunicaciones
 * con el servidor, y cuando sea necesario ejecutar el envío o recepción de información.
 * Además ofrece el soporte para la interacción con el usuario del sistema.
 */
public class Cliente {
	private String _username = null;
	private InetAddress _srvAddr = null;
	private int _srvPort;
	private OyenteServidor _os = null;
	private boolean _replRunning;
	
	public void connect(InetAddress addr, int port) throws IOException {
		if (_os != null && _os.isAlive()) throw new IllegalStateException("OyenteServidor is listening. Disconnect first");
		
		_srvAddr = addr;
		_srvPort = port;
		
		// TODO: Don't create a whole new OyenteServidor
		_os = new OyenteServidor(new Socket(_srvAddr, _srvPort), _username);
		_os.start();
		
		if (_os.waitSendConexion()) {
			System.err.println("No se recibió un mensaje de confirmación de conexión");
		} else {
			System.out.printf("Succesfully connected to %s:%d%n", addr.getHostAddress(), port); 
		}
	}
	
	public boolean disconnect() throws IOException {
		return _os.waitDisconnect();
	}
	
	public void repl() throws IOException {
		BufferedReader cliin = new BufferedReader(new InputStreamReader(System.in));
		
		if (_username == null) {
			System.out.print("Enter username: ");
			_username = cliin.readLine();
			if (_username.equals(Servidor.SERVIDOR)) {
				throw new IllegalArgumentException("Username can't be " + Servidor.SERVIDOR);
			}
		}
		
		if (_srvAddr == null) {
			System.out.print("Enter server address (host:port): ");
			String aux[] = cliin.readLine().split(":");
			_srvAddr = InetAddress.getByName(aux[0]);
			_srvPort = Integer.parseInt(aux[1]);
		}
		
		// We create an REPL
		System.out.printf("Welcome %s to Pr05 Client. Type \"help\" for help%n", _username);
		
		if (_srvAddr == null) {
			System.out.println("Warning. You are not connected to any server. Use \"connect\"");
		} else {
			connect(_srvAddr, _srvPort);
		}
		
		_replRunning = true;
		while (_replRunning) {
			System.out.print("> ");
			try {
				Command c = CommandFactory.parse(cliin.readLine());
				if (c != null) { 
					c.exec(this);
				} else {
					System.out.println("Error: Command not found, type \"help\" to get available commands");
				}
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
	
	public static void main(String [] argv) throws IOException {
		// We parse the CLI and add it to our "controller"
		
		Cliente c = new Cliente();
		if (argv.length >= 1) {
			c._srvAddr = InetAddress.getByName(argv[0].split(":")[0]);
			c._srvPort = Integer.parseInt(argv[0].split(":")[1]);
		}
		
		if (argv.length >= 2) {
			c._username = argv[1];
		}
		
		// Now we invoke the interface to our controller
		c.repl();
	}

	public OyenteServidor getOyenteServidor() {
		return _os;
	}

	public void stopRepl() {
		_replRunning = false;
	}

	public boolean beginDownload(String filename) throws IOException {
		return _os.waitPedirFichero(filename);
	}
}
