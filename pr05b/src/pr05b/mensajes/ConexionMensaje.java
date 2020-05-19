package pr05b.mensajes;

public class ConexionMensaje extends Mensaje {
	private static final long serialVersionUID = 7125628742368620831L;

	public ConexionMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_CONEXION;
	}
}
