package de.ollie.jrc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParser {

	public CommandLine parse(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("d", true, "directory to search into.");
		options.addOption("f", true, "name a file to check.");
		options.addOption("sd", true, "a directory for the subreports.");
		options.addOption("o", true, "a name for the output file.");
		options.addOption("p", true, "file pattern for search in a directory.");
		options.addOption("snfm", false, "suppresses any message for file which doesn't contain any unused fields.");
		options.addOption("l", false, "switches to list output.");
		return new DefaultParser().parse(options, args);
	}

}
