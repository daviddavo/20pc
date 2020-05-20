package pr05b.mensajes;

import java.net.InetAddress;

public class PreparadoServidorClienteMensaje extends Mensaje {
	private static final long serialVersionUID = -8649090471101140972L;
	
	InetAddress _addr;
	int _port;

	public PreparadoServidorClienteMensaje(String destino, String origen, InetAddress addr, int port) {
		super(destino, origen);
		_addr = addr;
		_port = port;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PREPARADO_SERVIDORCLIENTE;
	}

}
