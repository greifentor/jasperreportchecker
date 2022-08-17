package de.ollie.jrc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.ollie.jrc.util.StringListSplitter;

public class FileNameProvider {

	public static final StringListSplitter STRING_LIST_SPLITTER = new StringListSplitter();

	public List<String> getFileNamesFromCommandLineParameters(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("f", true, "name a file to check.");
		CommandLine cmd = new DefaultParser().parse(options, args);
		return getFilesToCheck(cmd);
	}

	private static List<String> getFilesToCheck(CommandLine cmd) {
		if (cmd.hasOption("f")) {
			return STRING_LIST_SPLITTER.split(cmd.getOptionValue("f"));
		}
		return new ArrayList<>();
	}

}
