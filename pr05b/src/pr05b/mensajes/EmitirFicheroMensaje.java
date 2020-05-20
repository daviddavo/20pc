package pr05b.mensajes;

public class EmitirFicheroMensaje extends Mensaje {
	private static final long serialVersionUID = -165737459575149634L;
	private String _filename;

	public EmitirFicheroMensaje(String destino, String origen, String filename) {
		super(destino, origen);
		_filename = filename;
	}
	
	public String getFilename() {
		return _filename;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_EMITIR_FICHERO;
	}

}
