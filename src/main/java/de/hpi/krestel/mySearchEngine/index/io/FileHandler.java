package de.hpi.krestel.mySearchEngine.index.io;

import java.io.IOException;

public interface FileHandler {
	void close() throws IOException;
	String getFileName();
}
