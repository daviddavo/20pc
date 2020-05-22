package pr05b.mensajes;

import pr05b.modelo.InfoFichero;

public class InfoFicheroMensaje extends Mensaje {
	private static final long serialVersionUID = 6959398039071550347L;
	private InfoFichero _infoFichero;

	public InfoFicheroMensaje(String destino, String origen, InfoFichero infoFichero) {
		super(destino, origen);
		_infoFichero = infoFichero;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_PUT_INFO_FICHERO;
	}
	
	public InfoFichero getInfoFichero() {
		return _infoFichero;
	}
}
