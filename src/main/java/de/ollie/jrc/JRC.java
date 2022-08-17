package de.ollie.jrc;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.ParseException;

import de.ollie.jrc.jrxml.UnusedObjectChecker;

public class JRC {

	static PrintStream out = System.out;

	private static final FileNameProvider FILE_NAME_PROVIDER = new FileNameProvider();
	private static final UnusedObjectChecker UNUSED_OBJECT_CHECKER = new UnusedObjectChecker();

	public static void main(String[] args) {
		if ((args == null) || (args.length == 0) || "help".equalsIgnoreCase(args[0])) {
			out.println("\nUsage: [command] {parameters}\n");
			out.println("commands:");
			out.println("\n  check");
			out.println("    - checks for unused fields, parameters and variables.");
			out.println("    - parameters:");
			out.println("        -f FILE_NAME[,FILE_NAME]");
			out.println("        -p PATTERN_TO_SEARCH_FOR (e. g. \"*.jrxml\")");
			out
					.println(
							"        -d DIRECTORY_TO_SEARCH_INTO (is \".\" if not set; do set this option without setting -p also)");
		} else if ("check".equalsIgnoreCase(args[0])) {
			try {
				List<String> fileNames = FILE_NAME_PROVIDER.getFileNamesFromCommandLineParameters(args);
				if (fileNames.isEmpty()) {
					out.println("\nNo matching files found!");
				}
				fileNames.forEach(JRC::checkForFile);
			} catch (ParseException pe) {
				out.println("ParseException: " + pe.getMessage());
			} catch (RuntimeException rte) {
				out.println("RuntimeException: " + (rte.getMessage() == null ? rte.getCause() : rte.getMessage()));
			}
		}
		out.println();
	}

	private static void checkForFile(String jrxmlFileName) {
		out.println("\nProcessing: " + jrxmlFileName);
		List<String> messages = UNUSED_OBJECT_CHECKER.checkForUnusedFieldsParametersAndVariables(jrxmlFileName);
		if (messages.isEmpty()) {
			out.println("No unused field, parameter or variable found.");
		} else {
			messages.stream().sorted((s0, s1) -> s0.compareTo(s1)).forEach(out::println);
		}
	}

}