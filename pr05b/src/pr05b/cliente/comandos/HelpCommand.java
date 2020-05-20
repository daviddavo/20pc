package pr05b.cliente.comandos;

import pr05b.cliente.OyenteServidor;

public class HelpCommand extends Command {

	protected HelpCommand() {
		super("help", "shows help");
	}

	@Override
	public void exec(OyenteServidor oc) {
		CommandFactory.printHelp(System.out);
	}

}
