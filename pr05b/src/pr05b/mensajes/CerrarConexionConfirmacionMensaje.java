package pr05b.mensajes;

public class CerrarConexionConfirmacionMensaje extends Mensaje {
	private static final long serialVersionUID = 495047388128104073L;

	public CerrarConexionConfirmacionMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_CONFIRMACION_CERRAR_CONEXION;
	}

}
