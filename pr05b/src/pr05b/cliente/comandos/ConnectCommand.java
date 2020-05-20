package pr05b.cliente.comandos;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import pr05b.cliente.Cliente;

public class ConnectCommand extends Command {
	private InetAddress _addr;
	private int _port;

	protected ConnectCommand() {
		super("connect", "Connects to given server");
	}
	
	@Override
	public Command parse(String [] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("connect"))    {
			if (args.length != 2) throw new IllegalArgumentException("Usage: connect server:port");

			String [] aux = args[1].split(":");
			if (aux.length != 2) throw new IllegalArgumentException("Usage: connect server:port");
			try {
				_addr = InetAddress.getByName(aux[0]);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(String.format("Host %s not found", aux[0]), e);
			}
			
			_port = Integer.parseInt(aux[1]);
			
			return this;
		}
		
		return null;
	}

	@Override
	public void exec(Cliente cl) {
		try {
			cl.connect(_addr, _port);
		} catch (IOException e) {
			System.out.println("Error trying to connect");
			System.err.println(e);
		}
	}

}
