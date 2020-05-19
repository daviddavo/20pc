package pr05b.cliente.comandos;

import pr05b.cliente.OyenteCliente;

public class HelpCommand extends Command {

	protected HelpCommand() {
		super("help", "shows help");
	}

	@Override
	public void exec(OyenteCliente oc) {
		CommandFactory.printHelp(System.out);
	}

}
