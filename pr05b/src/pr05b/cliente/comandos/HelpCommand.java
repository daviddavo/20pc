package pr05b.cliente.comandos;

import pr05b.cliente.Cliente;

public class HelpCommand extends Command {

	protected HelpCommand() {
		super("help", "shows help");
	}

	@Override
	public void exec(Cliente cl) {
		CommandFactory.printHelp(System.out);
	}

}
