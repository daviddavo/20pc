package pr05b.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import pr05b.cliente.comandos.Command;
import pr05b.cliente.comandos.CommandFactory;
import pr05b.mensajes.ConexionMensaje;
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
	public String _username = null;
	public InetAddress _srvAddr = null;
	public int _srvPort;
	
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
		System.out.println("Welcome to Pr05 Client. Type \"help\" for help");
		
		Socket socket = new Socket(_srvAddr, _srvPort);
		OyenteCliente oc = new OyenteCliente(socket, _username);
		oc.start();
		
		oc.waitSendConexion();
		
		while (true) {
			System.out.print("> ");
			Command c = CommandFactory.parse(cliin.readLine());
			if (c != null) { 
				c.exec(oc);
			} else {
				System.out.println("Error: Command not found, type \"help\" to get available commands");
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
}
