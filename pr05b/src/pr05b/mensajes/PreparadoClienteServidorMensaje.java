package pr05b.mensajes;

public class PreparadoClienteServidorMensaje extends Mensaje {
	private static final long serialVersionUID = 7114752838516448684L;
	
	int _port;

	public PreparadoClienteServidorMensaje(String destino, String origen, int port) {
		super(destino, origen);
		_port = port;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PREPARADO_CLIENTESERVIDOR;
	}

	public int getPort() {
		return _port;
	}
}
