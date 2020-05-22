package pr05b.cliente.comandos;

import java.util.Collection;

import pr05b.cliente.Cliente;
import pr05b.cliente.OyenteServidor;
import pr05b.modelo.InfoFichero;
import pr05b.modelo.Usuario;

public class GetUserListCommand extends Command {

	protected GetUserListCommand() {
		super("users", "shows connected users to server");
	}

	@Override
	public void exec(Cliente cl) {
		OyenteServidor os = cl.getOyenteServidor();
		if (os == null || !os.isAlive()) throw new IllegalArgumentException("You are not connected to a server");
		try {
			Collection<Usuario> lu = os.waitListaUsuarios();
			if (lu == null) {
				System.err.println("There is no response from server");	
			} else {
				if (lu.isEmpty()) System.out.println("Warning: Empty user list");
				for (Usuario u : lu) {
					System.out.printf("%-10s%-20s%3c%n",
						u.getUsername(), u.getInetAddress().getHostAddress(), u.isConnected()?'C':'D');
					
					for (InfoFichero f : u.getInfoFicheros()) {
						System.out.printf("  | %-20s%5d Bytes%n", f.getPath(), f.getSize());
					}
				}
			}
		} catch (Exception e){
			System.err.println(e);
		}
	}

}
