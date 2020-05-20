package pr05b.cliente.comandos;

import pr05b.cliente.Cliente;

public class ExitCommand extends Command {
	protected ExitCommand() {
		super("exit", "exits the application");
	}

	@Override
	public void exec(Cliente cl) {
		System.out.println("Bye!");
		try {
			cl.disconnect();
		} catch (Exception e) {
			System.err.println(e);
		}
		cl.stopRepl();
	}
}
