package pr05b.cliente.comandos;

import java.io.IOException;

import pr05b.cliente.Cliente;

public class DisconnectCommand extends Command {

	protected DisconnectCommand() {
		super("disconnect", "Disconnects from remote");
	}

	@Override
	public void exec(Cliente cl){
		System.out.println("Trying to disconnect...");
		boolean failed = true;
		try {
			failed = cl.disconnect();
		} catch (IOException e) {
			System.err.println(e);
			failed = true;
		}
		
		if (failed) {
			System.out.println("Disconnected!. Use \"connect\" to reconnect");
		} else {
			System.out.println("Failed to disconnect");
		}
		
	}

}
