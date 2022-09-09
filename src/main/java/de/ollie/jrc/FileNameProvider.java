package de.ollie.jrc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;

import de.ollie.jrc.util.StringListSplitter;

public class FileNameProvider {

	public static final StringListSplitter STRING_LIST_SPLITTER = new StringListSplitter();
	public static final DirectoryScanner DIRECTORY_SCANNER = new DirectoryScanner();

	public List<String> getFileNamesFromCommandLineParameters(CommandLine cmd) {
		return getFilesToCheck(cmd);
	}

	private List<String> getFilesToCheck(CommandLine cmd) {
		List<String> fileNames = getFileNamesFromCommandLine(cmd);
		String dir = getDirectoryFromCommandLine(cmd);
		String pattern = getPatternFromCommandLine(cmd);
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

	private String getDirectoryFromCommandLine(CommandLine cmd) {
		return cmd.hasOption("d") ? cmd.getOptionValue("d") : null;
	}

	private List<String> getFileNamesFromCommandLine(CommandLine cmd) {
		return cmd.hasOption("f") ? STRING_LIST_SPLITTER.split(cmd.getOptionValue("f")) : new ArrayList<>();
	}

	private String getPatternFromCommandLine(CommandLine cmd) {
		return cmd.hasOption("p") ? cmd.getOptionValue("p") : null;
	}

}
