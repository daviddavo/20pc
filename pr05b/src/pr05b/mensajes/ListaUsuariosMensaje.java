package pr05b.mensajes;

public class ListaUsuariosMensaje extends Mensaje {
	private static final long serialVersionUID = -6083512730713235267L;

	public ListaUsuariosMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_LISTA_USUARIOS;
	}

}
