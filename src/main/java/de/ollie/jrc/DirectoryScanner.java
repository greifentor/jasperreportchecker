package de.ollie.jrc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class DirectoryScanner {

	public List<File> scan(List<File> foundPathes, String path, String pattern) {
		File dir = new File(path);
		FileFilter fileFilter = new WildcardFileFilter(pattern);
		File[] dirs = dir.listFiles(File::isDirectory);
		File[] files = dir.listFiles(fileFilter);
		for (int i = 0; i < files.length; i++) {
			foundPathes.add(files[i]);
		}
		for (File d : dirs) {
			foundPathes.addAll(scan(new ArrayList<>(), d.getAbsolutePath(), pattern));
		}
		return foundPathes;
	}

}
