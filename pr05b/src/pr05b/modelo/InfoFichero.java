package pr05b.modelo;

import java.io.Serializable;

public class InfoFichero implements Serializable {
	private static final long serialVersionUID = -8603324193695119082L;
	
	public String path;
	public int size;
	
	public InfoFichero(String path, int size) {
		this.path = path;
		this.size = size;
	}
}
