package pr05b.modelo;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda información para un usaurio registrado en el sistema.
 * Tendrá al menos los siguientes atributos: identificador de
 * usuario, dirección ip y lista de información compartida.
 * El servidor almacenará información sobre todos los usuarios
 * almacenados en el sitema.
 */
public class Usuario implements Serializable {
	private static final long serialVersionUID = 2889129328878888688L;
	private String _userName;
	private InetAddress _addr;
	private List<InfoFichero> _info;
	private boolean _connected = false;
	
	public Usuario(String userName, InetAddress inetAddress) {
		_userName = userName;
		_addr = inetAddress;
		_info = new ArrayList<>();
	}
	
	public String getUserName() {
		return _userName;
	}
	
	public InetAddress getInetAddress() {
		return _addr;
	}
	
	public void setConnected() {
		_connected = true;
	}
	
	public void setDisconnected() {
		_connected = false;
	}
	
	public boolean isConnected () {
		return _connected;
	}
	
	public List<InfoFichero> getInfoFicheros() {
		return _info;
	}
}
