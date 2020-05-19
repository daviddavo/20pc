package pr05b.cliente.comandos;

public class HelpCommand extends Command {

	protected HelpCommand() {
		super("help", "shows help");
	}

	@Override
	public void exec() {
		CommandFactory.printHelp(System.out);
	}

}
