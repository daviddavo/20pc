package pr05b.cliente.comandos;

import java.util.Collection;

import pr05b.cliente.OyenteServidor;
import pr05b.modelo.Usuario;

public class GetUserListCommand extends Command {

	protected GetUserListCommand() {
		super("users", "shows connected users to server");
	}

	@Override
	public void exec(OyenteServidor oc) {
		try {
			Collection<Usuario> lu = oc.waitListaUsuarios();
			if (lu == null) {
				System.err.println("There is no response from server");	
			} else {
				if (lu.isEmpty()) System.out.println("Warning: Empty user list");
				for (Usuario u : lu) {
					System.out.printf("%-10s%-20s%3c%n",
						u.getUserName(), u.getInetAddress().getHostAddress(), u.isConnected()?'C':'D');
				}
			}
		} catch (Exception e){
			System.err.println(e);
		}
	}

}
