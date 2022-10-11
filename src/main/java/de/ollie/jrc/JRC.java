package de.ollie.jrc;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import de.ollie.jrc.jrxml.FileReader;
import de.ollie.jrc.jrxml.SampleXMLBuilder;
import de.ollie.jrc.jrxml.UnusedObjectChecker;
import de.ollie.jrc.jrxml.XMLWriter;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.xml.model.XMLNode;

public class JRC {

	static PrintStream out = System.out;

	private static final FileNameProvider FILE_NAME_PROVIDER = new FileNameProvider();
	private static final UnusedObjectChecker UNUSED_OBJECT_CHECKER = new UnusedObjectChecker();

	JRC() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		try {
			CommandLine cmd = new CommandLineParser().parse(args);
			boolean somethingPrinted = true;
			if ((args == null) || (args.length == 0) || "help".equalsIgnoreCase(args[0])) {
				out.println("\nUsage: [command] {parameters}\n");
				out.println("commands:");
				out.println("\n  check");
				out.println("    - checks for unused fields, parameters and variables.");
				out.println("    - parameters:");
				out
						.println(
								"        -d DIRECTORY_TO_SEARCH_INTO (is \".\" if not set; do set this option without setting -p also)");
				out.println("        -f FILE_NAME[,FILE_NAME]");
				out.println("        -p PATTERN_TO_SEARCH_FOR (e. g. \"*.jrxml\")");
				out.println("        -snfm (suppresses messages if nothing unused found)");
				out.println("\n  xml");
				out.println("    - creates sample xml templates for a JRXML file.");
				out.println("    - parameters:");
				out.println("        -f FILE_NAME (the name of the JRXML file which the sample is to create for)");
				out.println("        -sd DIR_NAME (the name of a directory, where the subreports could be found)");
				// out.println(" -o FILE_NAME (a name for the output file)");
			} else if ("check".equalsIgnoreCase(args[0])) {
				List<String> fileNames = FILE_NAME_PROVIDER.getFileNamesFromCommandLineParameters(cmd);
				if (fileNames.isEmpty()) {
					out.println("\nNo matching files found!");
				}
				somethingPrinted = fileNames
						.stream()
						.map(fileName -> checkForFile(fileName, cmd.hasOption("snfm")))
						.reduce((b0, b1) -> b0 || b1)
						.orElse(true);
			} else if ("xml".equalsIgnoreCase(args[0])) {
				JasperReport report = new FileReader(cmd.getOptionValue("f")).readFromFile();
				String subreportDir = cmd.getOptionValue("sd");
				if ((subreportDir == null) || subreportDir.isEmpty()) {
					subreportDir = "./";
				}
				XMLNode rootNode = new SampleXMLBuilder().buildXMLFromJasperReport(report, subreportDir);
				new XMLWriter().write(rootNode, out);
			}
			if (somethingPrinted) {
				out.println();
			}
		} catch (IOException ioe) {
			out.println("IOException: " + ioe.getMessage());
			ioe.printStackTrace();
		} catch (JAXBException jaxbe) {
			out.println("JAXBException: " + jaxbe.getMessage());
			jaxbe.printStackTrace();
		} catch (ParseException pe) {
			out.println("ParseException: " + pe.getMessage());
		} catch (RuntimeException rte) {
			out.println("RuntimeException: " + (rte.getMessage() == null ? rte.getCause() : rte.getMessage()));
		}
	}

	private static boolean checkForFile(String jrxmlFileName, boolean suppressNothingFoundMessage) {
		List<String> messages = UNUSED_OBJECT_CHECKER.checkForUnusedFieldsParametersAndVariables(jrxmlFileName);
		boolean suppressMessages = isMessageToSuppress(messages, suppressNothingFoundMessage);
		boolean somethingPrinted = false;
		if (!suppressMessages) {
			out.println("\nProcessing: " + jrxmlFileName);
			somethingPrinted = true;
		}
		if (messages.isEmpty()) {
			if (!suppressMessages) {
				out.println("No unused field, parameter or variable found.");
			}
		} else {
			messages.stream().sorted((s0, s1) -> s0.compareTo(s1)).forEach(out::println);
			somethingPrinted = true;
		}
		return somethingPrinted;
	}

	private static boolean isMessageToSuppress(List<String> messages, boolean suppressNothingFoundMessage) {
		return messages.isEmpty() && suppressNothingFoundMessage;
	}

}