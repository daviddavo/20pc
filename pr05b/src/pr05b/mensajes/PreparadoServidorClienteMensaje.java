package pr05b.mensajes;

import java.net.InetAddress;

public class PreparadoServidorClienteMensaje extends Mensaje {
	private static final long serialVersionUID = -8649090471101140972L;
	
	private InetAddress _addr;
	private int _port;
	private String _filename;

	public PreparadoServidorClienteMensaje(String destino, String origen, InetAddress addr, int port, String filename) {
		super(destino, origen);
		_addr = addr;
		_port = port;
		_filename = filename;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PREPARADO_SERVIDORCLIENTE;
	}
	
	public InetAddress getAddress() {
		return _addr;
	}
	
	public int getPort() {
		return _port;
	}

	public String getFilename() {
		return _filename;
	}
}
