package pr05b.cliente.comandos;

import pr05b.cliente.Cliente;

public class NoneCommand extends Command {
	
	public NoneCommand() {
		super("", "");
	}
	
	@Override
	public Command parse(String [] args) {
		if (args.length == 1 && args[0].isBlank()) return this;
		else return null;
	}

	@Override
	public void exec(Cliente cl) {
		// Este comando solo sirve para imprimir un salto de linea
	}

}
