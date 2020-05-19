package pr05b.cliente.comandos;

import java.io.OutputStream;
import java.io.PrintStream;

public class CommandFactory {
	static final Command [] _clist = {
		new HelpCommand(),
		new ExitCommand(),
		new NoneCommand()
	};
	
	/*
	static final List<Class<? extends Command>> _clist = Arrays.asList(
		ExitCommand.class
	);
	*/

	public static Command parse(String line) {
		return parse(line.split("\\s"));
	}
	
	public static Command parse(String [] arguments) {
		for (Command c : _clist) {
			if (c.parse(arguments) != null) return c;
		}
		
		return null;
	}
	
	public static void printHelp(OutputStream out) {
		printHelp(new PrintStream(out));
	}

	public static void printHelp(PrintStream out) {
		for (Command c : _clist) {
			System.out.printf("%-10s%s%n", c.getName(), c.getHelp());
		}
	}
}
