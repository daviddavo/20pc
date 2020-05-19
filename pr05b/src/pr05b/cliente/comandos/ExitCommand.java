package pr05b.cliente.comandos;

import pr05b.cliente.OyenteCliente;

public class ExitCommand extends Command {
	protected ExitCommand() {
		super("exit", "exits the application");
	}

	@Override
	public void exec(OyenteCliente oc) {
		// TODO Auto-generated method stub
		System.err.println("Not implemented yet");
	}
}
