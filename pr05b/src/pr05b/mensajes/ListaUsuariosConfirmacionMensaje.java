package pr05b.mensajes;

import java.util.List;

import pr05b.modelo.Usuario;

public class ListaUsuariosConfirmacionMensaje extends Mensaje {
	private static final long serialVersionUID = -7887406130784464177L;
	private List<Usuario> _listaUsuarios;

	public ListaUsuariosConfirmacionMensaje(String destino, String origen, List<Usuario> listaUsuarios) {
		super(destino, origen);
		_listaUsuarios = listaUsuarios;
	}

	@Override
	public Tipo getTipo() {
		return Tipo.MENSAJE_CONFIRMACION_LISTA_USUARIOS;
	}

	public List<Usuario> getListaUsuarios () {
		return _listaUsuarios;
	}
}
