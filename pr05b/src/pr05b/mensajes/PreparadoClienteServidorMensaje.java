package pr05b.mensajes;

import java.net.InetAddress;

public class PreparadoClienteServidorMensaje extends Mensaje {
	private static final long serialVersionUID = 7114752838516448684L;
	
	private InetAddress _addr;
	private int _port;
	private String _filename;

	public PreparadoClienteServidorMensaje(String destino, String origen, InetAddress addr, int port, String filename) {
		super(destino, origen);
		_addr = addr;
		_port = port;
		_filename = filename;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PREPARADO_CLIENTESERVIDOR;
	}
	
	public InetAddress getAddress() {
		return _addr;
	}

	public int getPort() {
		return _port;
	}
	
	public String getFileName() {
		return _filename;
	}
}
