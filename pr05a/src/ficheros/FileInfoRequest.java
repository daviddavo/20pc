package ficheros;

import java.io.Serializable;

class FileInfoRequest implements Serializable {
	private static final long serialVersionUID = -747777106916383251L;
	private String _fileName;
	
	FileInfoRequest(String fileName) {
		_fileName = fileName;
	}
	
	String getFileName() {
		return _fileName;
	}
}
