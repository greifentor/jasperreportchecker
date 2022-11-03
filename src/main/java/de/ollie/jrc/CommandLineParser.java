package de.ollie.jrc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.Data;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
public class CommandLineParser {

	@Accessors(chain = true)
	@Data
	@Generated
	public static class CommandLineData {

		private String directory;
		private String fileName;
		private String fileNamePattern;
		private boolean listOutput;
		private String outputFileName;
		private String subreportDirectory;
		private boolean suppressMessageForFileHavingNoUnusedObjects;

	}

	private final String[] args;

	private Options options;
	private CommandLine commandLine;

	public CommandLineData parse() throws ParseException {
		createOptions();
		parseCommandLine();
		return convertCommandLineToCommandLineData();
	}

	private void createOptions() {
		options = new Options();
		options.addOption("d", true, "directory to search into.");
		options.addOption("f", true, "name a file to check.");
		options.addOption("sd", true, "a directory for the subreports.");
		options.addOption("o", true, "a name for the output file.");
		options.addOption("p", true, "file pattern for search in a directory.");
		options.addOption("snfm", false, "suppresses any message for file which doesn't contain any unused fields.");
		options.addOption("l", false, "switches to list output.");
	}

	private void parseCommandLine() throws ParseException {
		commandLine = new DefaultParser().parse(options, args);
	}

	private CommandLineData convertCommandLineToCommandLineData() {
		CommandLineData cmd = new CommandLineData();
		return cmd
				.setDirectory(getOption("d", cmd.getDirectory()))
				.setFileName(getOption("f", cmd.getFileName()))
				.setListOutput(getOptionAsBoolean("l"))
				.setOutputFileName(getOption("o", cmd.getOutputFileName()))
				.setFileNamePattern(getOption("p", cmd.getFileNamePattern()))
				.setSubreportDirectory(getOption("sd", cmd.getSubreportDirectory()))
				.setSuppressMessageForFileHavingNoUnusedObjects(getOptionAsBoolean("snfm"));
	}

	private String getOption(String identifier, String defaultValue) {
		return commandLine.hasOption(identifier) ? commandLine.getOptionValue(identifier) : defaultValue;
	}

	private boolean getOptionAsBoolean(String identifier) {
		return commandLine.hasOption(identifier);
	}

}
