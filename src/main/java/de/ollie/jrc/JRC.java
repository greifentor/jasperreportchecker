package de.ollie.jrc;

import java.util.List;

import org.apache.commons.cli.ParseException;

import de.ollie.jrc.jrxml.UnusedObjectChecker;
import de.ollie.jrc.util.StringListSplitter;

public class JRC {

	public static FileNameProvider fileNameProvider = new FileNameProvider();
	public static StringListSplitter stringListSplitter = new StringListSplitter();
	public static UnusedObjectChecker unusedObjectChecker = new UnusedObjectChecker();

	public static void main(String[] args) {
		try {
			fileNameProvider.getFileNamesFromCommandLineParameters(args).forEach(JRC::checkForFile);
		} catch (ParseException pe) {
			System.out.println("ParseException: " + pe.getMessage());
		} catch (RuntimeException rte) {
			System.out.println("RuntimeException: " + (rte.getMessage() == null ? rte.getCause() : rte.getMessage()));
		}
		System.out.println();
	}

	private static void checkForFile(String jrxmlFileName) {
		System.out.println("\nProcessing: " + jrxmlFileName);
		List<String> messages = unusedObjectChecker.checkForUnusedFieldsParametersAndVariables(jrxmlFileName);
		if (messages.isEmpty()) {
			System.out.println("No unused field, parameter or variable found.");
		} else {
			messages.stream().sorted((s0, s1) -> s0.compareTo(s1)).forEach(System.out::println);
		}
	}

}