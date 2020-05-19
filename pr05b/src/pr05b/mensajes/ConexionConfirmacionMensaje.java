package pr05b.mensajes;

public class ConexionConfirmacionMensaje extends Mensaje {
	private static final long serialVersionUID = -6308295082886430606L;

	public ConexionConfirmacionMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_CONFIRMACION_CONEXION;
	}

}
