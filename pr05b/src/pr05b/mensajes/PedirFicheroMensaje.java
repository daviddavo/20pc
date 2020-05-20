package pr05b.mensajes;

public class PedirFicheroMensaje extends Mensaje {
	private static final long serialVersionUID = -7653571842635596582L;
	private String _filename;

	public PedirFicheroMensaje(String destino, String origen, String filename) {
		super(destino, origen);
		_filename = filename;
	}
	
	public String getFilename() {
		return _filename;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PEDIR_FICHERO;
	}

}
