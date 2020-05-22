package pr05b.modelo;

import java.io.Serializable;

public class InfoFichero implements Serializable {
	private static final long serialVersionUID = -8603324193695119082L;
	
	private String _path;
	private long _size;
	
	public InfoFichero(String path, long size) {
		this._path = path;
		this._size = size;
	}
	
	public String getPath() {
		return _path;
	}
	
	public long getSize() {
		return _size;
	}
}
