package ficheros;

import java.io.Serializable;

public class FileInfoResponse implements Serializable {
	private static final long serialVersionUID = -9171863652612986036L;
	private String _fName;
	private long _size;
	
	public FileInfoResponse(String fName, long size) {
		_fName = fName;
		_size = size;
	}

	public String getFilename() {
		return _fName;
	}
	
	public long getSize() {
		return _size;
	}
}
