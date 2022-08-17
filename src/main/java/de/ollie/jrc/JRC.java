package de.ollie.jrc;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.ParseException;

import de.ollie.jrc.jrxml.UnusedObjectChecker;

public class JRC {

	private static final FileNameProvider FILE_NAME_PROVIDER = new FileNameProvider();
	private static final UnusedObjectChecker UNUSED_OBJECT_CHECKER = new UnusedObjectChecker();
	private static final PrintStream OUT = System.out;

	public static void main(String[] args) {
		if ((args == null) || (args.length == 0) || "help".equalsIgnoreCase(args[0])) {
			OUT.println("\nUsage: [command] {parameters}\n");
			OUT.println("commands:");
			OUT.println("\n  check");
			OUT.println("    - checks for unused fields, parameters and variables.");
			OUT.println("    - parameters:");
			OUT.println("        -f FILE_NAME[,FILE_NAME]");
		} else if ("check".equalsIgnoreCase(args[0])) {
			try {
				FILE_NAME_PROVIDER.getFileNamesFromCommandLineParameters(args).forEach(JRC::checkForFile);
			} catch (ParseException pe) {
				OUT.println("ParseException: " + pe.getMessage());
			} catch (RuntimeException rte) {
				OUT.println("RuntimeException: " + (rte.getMessage() == null ? rte.getCause() : rte.getMessage()));
			}
		}
		OUT.println();
	}

	private static void checkForFile(String jrxmlFileName) {
		OUT.println("\nProcessing: " + jrxmlFileName);
		List<String> messages = UNUSED_OBJECT_CHECKER.checkForUnusedFieldsParametersAndVariables(jrxmlFileName);
		if (messages.isEmpty()) {
			OUT.println("No unused field, parameter or variable found.");
		} else {
			messages.stream().sorted((s0, s1) -> s0.compareTo(s1)).forEach(OUT::println);
		}
	}

}