package pr05b.cliente.comandos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pr05b.cliente.Cliente;

public class BashCommand extends Command {
	String [] _command;

	protected BashCommand() {
		super("!c", "runs command \"c\" in terminal");
	}
	
	@Override
	public Command parse(String [] args) {
		if (args.length >= 1 && args[0].length() >= 1 && args[0].charAt(0) == '!') {
			if (args[0].length() == 1) throw new IllegalArgumentException("Example: !ls");
			
			// Quitamos el "!"
			_command = args.clone();
			_command[0] = args[0].substring(1);
			
			return this;
		}
		
		return null;
	}

	@Override
	public void exec(Cliente cl) {
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", String.join(" ", _command));
			pb.redirectErrorStream(true);
			Process p = pb.start();
			
			p.getInputStream().transferTo(System.out);
			p.getErrorStream().transferTo(System.err);
			if (p.waitFor() != 0) {
				System.err.println("process exit with non-zero status code");
			}
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
	}

}
