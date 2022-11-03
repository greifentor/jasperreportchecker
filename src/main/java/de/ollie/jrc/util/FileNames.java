package de.ollie.jrc.util;


public class FileNames {

	FileNames() {
		throw new UnsupportedOperationException();
	}

	public static String normalize(String fileName) {
		return fileName.replace("\\", "/");
	}

}
