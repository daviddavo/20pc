package pr05b.cliente.comandos;

import java.io.File;
import java.io.IOException;

import pr05b.cliente.Cliente;
import pr05b.modelo.InfoFichero;

public class AddFileCommand extends Command {
	String _filename;
	long _size;

	protected AddFileCommand() {
		super("add", "sends the given file info to the server, making it available to other clients");
	}

	@Override
	public Command parse(String [] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase(_name)) {
			if (args.length != 2)
				throw new IllegalArgumentException("Usage: add [filename]");
			
			_filename = args[1];
			File file = new File(_filename);
			if (!file.isFile()) throw new IllegalArgumentException(args[1] + " is not a file");
			if (!file.canRead()) throw new IllegalArgumentException("File "+args[1]+" is not readable!");
			
			_size = file.length();
			
			return this;
		}
		
		return null;
	}

	@Override
	public void exec(Cliente cl) {
		try {
			if (cl.sendInfoFichero(new InfoFichero(_filename, _size))) {
				System.out.println("No se ha recibido confirmación del servidor");
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
