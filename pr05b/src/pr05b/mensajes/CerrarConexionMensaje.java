package pr05b.mensajes;

public class CerrarConexionMensaje extends Mensaje {
	private static final long serialVersionUID = 3891196038155788758L;

	public CerrarConexionMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_CERRAR_CONEXION;
	}

}
