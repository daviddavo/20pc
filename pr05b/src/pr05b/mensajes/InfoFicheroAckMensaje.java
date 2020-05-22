package pr05b.mensajes;

public class InfoFicheroAckMensaje extends Mensaje {
	private static final long serialVersionUID = 2998982660289957515L;

	public InfoFicheroAckMensaje(String destino, String origen) {
		super(destino, origen);
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_ACK_INFO_FICHERO;
	}

}
