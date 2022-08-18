package de.ollie.jrc;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import de.ollie.jrc.util.StringListSplitter;

public class FileNameProvider {

	public static final StringListSplitter STRING_LIST_SPLITTER = new StringListSplitter();

	public List<String> getFileNamesFromCommandLineParameters(CommandLine cmd) {
		return getFilesToCheck(cmd);
	}

	private static List<String> getFilesToCheck(CommandLine cmd) {
		List<String> fileNames = new ArrayList<>();
		String dir = null;
		String pattern = null;
		if (cmd.hasOption("f")) {
			fileNames.addAll(STRING_LIST_SPLITTER.split(cmd.getOptionValue("f")));
		}
		if (cmd.hasOption("p")) {
			pattern = cmd.getOptionValue("p");
		}
		if (cmd.hasOption("d")) {
			dir = cmd.getOptionValue("d");
		}
		if (pattern != null) {
			if (dir == null) {
				dir = ".";
			}
			fileNames = scan(new ArrayList<>(), dir, pattern)
					.stream()
					.map(File::getAbsolutePath)
					.collect(Collectors.toList());
		} else if (dir != null) {
			throw new RuntimeException("-d option cannot be set without setting -p option.");
		}
		return fileNames;
	}

	static List<File> scan(List<File> foundPathes, String path, String pattern) {
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
