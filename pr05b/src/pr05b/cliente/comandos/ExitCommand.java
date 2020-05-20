package pr05b.cliente.comandos;

import pr05b.cliente.OyenteServidor;

public class ExitCommand extends Command {
	protected ExitCommand() {
		super("exit", "exits the application");
	}

	@Override
	public void exec(OyenteServidor oc) {
		// TODO Auto-generated method stub
		System.err.println("Not implemented yet");
	}
}
