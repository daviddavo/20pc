package pr05b.cliente.comandos;

import pr05b.cliente.OyenteCliente;

public abstract class Command {
	String _name;
	String _help;
	
	protected Command(String name, String help) {
		_name = name;
		_help = help;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getHelp() {
		return _help;
	}
	
	public Command parse(String [] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase(_name)) return this;
		else return null;
	}
	
	public abstract void exec(OyenteCliente oc);
}
