package de.ollie.jrc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.ollie.jrc.CommandLineParser.CommandLineData;
import de.ollie.jrc.util.StringListSplitter;

public class FileNameProvider {

	public static final StringListSplitter STRING_LIST_SPLITTER = new StringListSplitter();
	public static final DirectoryScanner DIRECTORY_SCANNER = new DirectoryScanner();

	public List<String> getFileNamesFromCommandLineParameters(CommandLineData cmd) {
		return getFilesToCheck(cmd);
	}

	public List<String> getFileNamesFromCommandLineParameters(List<String> fileNames, String dir, String pattern) {
		return getFilesToCheck(fileNames, dir, pattern);
	}

	private List<String> getFilesToCheck(CommandLineData cmd) {
		List<String> fileNames = getFileNamesFromCommandLine(cmd);
		String dir = cmd.getDirectory();
		String pattern = cmd.getFileNamePattern();
		return getFilesToCheck(fileNames, dir, pattern);
	}

	private List<String> getFilesToCheck(List<String> fileNames, String dir, String pattern) {
		if (pattern != null) {
			if (dir == null) {
				dir = ".";
			}
			fileNames = DIRECTORY_SCANNER
					.scan(new ArrayList<>(), dir, pattern)
					.stream()
					.map(File::getAbsolutePath)
					.collect(Collectors.toList());
		} else if (dir != null) {
			throw new RuntimeException("-d option cannot be set without setting -p option.");
		}
		return fileNames;
	}

	private List<String> getFileNamesFromCommandLine(CommandLineData cmd) {
		return cmd.getFileName() != null ? STRING_LIST_SPLITTER.split(cmd.getFileName()) : new ArrayList<>();
	}

}
