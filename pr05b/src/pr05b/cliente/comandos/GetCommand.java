package pr05b.cliente.comandos;

import java.io.IOException;

import pr05b.cliente.Cliente;

public class GetCommand extends Command {
	private String _filename;

	public GetCommand() {
		super("get", "establishes a connection and downloads a file");
	}
	
	@Override
	public Command parse(String [] args) {
		if (args.length >= 1) {
			if (args[0].equals(_name)) {
				if (args.length != 2) throw new IllegalArgumentException("Usage: download filename");
				
				_filename = args[1];
				return this;
			}
		}
			
		return null;
	}

	@Override
	public void exec(Cliente cl) {
		boolean failed = true;
		try {
			failed = cl.beginDownload(_filename);
		} catch (IOException e) {
			System.err.println(e);
			failed = true;
		}
		
		if (failed) {
			System.out.println("Couldn't start downloading");
		} else {
			System.out.printf("Started downloading of file %s%n", _filename);
		}
	}

}
