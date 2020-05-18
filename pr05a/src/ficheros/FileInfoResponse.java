package ficheros;

import java.io.Serializable;

public class FileInfoResponse implements Serializable {
	private static final long serialVersionUID = -9171863652612986036L;
	private String _fName;
	private long _size;
	private String _md5;
	
	public FileInfoResponse(String fName, String md5, long size) {
		_fName = fName;
		_md5 = md5;
		_size = size;
	}

	public String getFilename() {
		return _fName;
	}
	
	public long getSize() {
		return _size;
	}
}
