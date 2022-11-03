package de.ollie.jrc;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.ParseException;

import de.ollie.jrc.CommandLineParser.CommandLineData;
import de.ollie.jrc.gui.JRCFrame;
import de.ollie.jrc.jrxml.DependencyDiagramBuilder;
import de.ollie.jrc.jrxml.DirectoryReader;
import de.ollie.jrc.jrxml.FileReader;
import de.ollie.jrc.jrxml.NodeSampleDataGenerator;
import de.ollie.jrc.jrxml.SampleXMLBuilder;
import de.ollie.jrc.jrxml.TopUsageReportListBuilder;
import de.ollie.jrc.jrxml.UnusedObjectChecker;
import de.ollie.jrc.jrxml.XMLWriter;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.xml.model.XMLNode;

public class JRC {

	static PrintStream out = System.out;

	private static final FileNameProvider FILE_NAME_PROVIDER = new FileNameProvider();
	private static final NodeSampleDataGenerator NODE_SAMPLE_DATA_GENERATOR = new NodeSampleDataGenerator();
	private static final UnusedObjectChecker UNUSED_OBJECT_CHECKER = new UnusedObjectChecker();

	JRC() {
		throw new UnsupportedOperationException();
	}

	public static void main(String[] args) {
		try {
			CommandLineData cmd = new CommandLineParser(args).parse();
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
				out.println("\n  gui");
				out.println("    - opens a Swing GUI.");
				out.println("    - parameters:");
				out.println("        -d DIR_NAME (a directory to start with (default: \".\")");
				out.println("\n  usage");
				out.println("    - create a PlantUML diagram with the reports dependent to the passed one");
				out.println("    - parameters:");
				out
						.println(
								"        -f FILE_NAME (the name of the JRXML file which dependent report to investigate for)");
				out.println("        -d DIR_NAME (the name of a directory, where the reports could be found)");
				out.println("        -l (print a list of the top reports only)");
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
						.map(fileName -> checkForFile(fileName, cmd.isSuppressMessageForFileHavingNoUnusedObjects()))
						.reduce((b0, b1) -> b0 || b1)
						.orElse(true);
			} else if ("gui".equalsIgnoreCase(args[0])) {
				String dirName = cmd.getDirectory() != null ? cmd.getDirectory() : ".";
				new JRCFrame(dirName).setVisible(true);
			} else if ("usage".equalsIgnoreCase(args[0])) {
				String dir = cmd.getDirectory();
				if ((dir == null) || dir.isEmpty()) {
					dir = "./";
				}
				usage(cmd.getFileName(), dir, cmd.isListOutput());
			} else if ("xml".equalsIgnoreCase(args[0])) {
				String dir = cmd.getDirectory();
				if ((dir == null) || dir.isEmpty()) {
					dir = "./";
				}
				String subreportDir = cmd.getSubreportDirectory();
				if ((subreportDir == null) || subreportDir.isEmpty()) {
					subreportDir = "./";
				}
				xml(cmd.getFileName(), subreportDir);
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

	public static boolean checkForFile(String jrxmlFileName, boolean suppressNothingFoundMessage) {
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

	public static void usage(String fileName, String dir, boolean topLevelList) throws IOException, JAXBException {
		Map<String, JasperReport> reports = new DirectoryReader(dir).readAllReports();
		String plainFileName = fileName.replace(dir, "");
		if (topLevelList) {
			new TopUsageReportListBuilder(plainFileName, reports, out).build();
		} else {
			new DependencyDiagramBuilder(plainFileName, reports, out).build();
		}
	}

	public static void xml(String reportFileName, String subreportDir) throws IOException, JAXBException {
		JasperReport report = new FileReader(reportFileName).readFromFile();
		XMLNode rootNode = new SampleXMLBuilder().buildXMLFromJasperReport(report, subreportDir);
		new XMLWriter(NODE_SAMPLE_DATA_GENERATOR).write(rootNode, out);
	}

}