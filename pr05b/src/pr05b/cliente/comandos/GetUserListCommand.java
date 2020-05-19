package pr05b.cliente.comandos;

import pr05b.cliente.OyenteCliente;

public class GetUserListCommand extends Command {

	protected GetUserListCommand() {
		super("users", "shows connected users to server");
	}

	@Override
	public void exec(OyenteCliente oc) {
		try {
			oc.waitListaUsuarios();
		} catch (Exception e){
			System.err.println(e);
		}
	}

}
