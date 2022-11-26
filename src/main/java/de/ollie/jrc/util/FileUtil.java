package de.ollie.jrc.util;

import java.io.File;

public class FileUtil {

	public boolean isADirectory(String fileName) {
		return new File(fileName).isDirectory();
	}

	public boolean isExisting(String fileName) {
		return new File(fileName).exists();
	}

}
