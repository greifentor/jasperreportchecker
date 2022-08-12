package de.ollie.jrc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.ollie.jrc.jrxml.FileReader;
import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.Parameter;
import de.ollie.jrc.jrxml.model.Variable;
import de.ollie.jrc.util.StringListSplitter;

public class JRC {

	public static StringListSplitter stringListSplitter = new StringListSplitter();

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("f", true, "name a file to check.");
		try {
			CommandLine cmd = new DefaultParser().parse(options, args);
			getFilesToCheck(cmd).forEach(JRC::checkForFile);
		} catch (ParseException pe) {
			System.out.println("ParseException: " + pe.getMessage());
		} catch (RuntimeException rte) {
			System.out.println("RuntimeException: " + (rte.getMessage() == null ? rte.getCause() : rte.getMessage()));
		}
		System.out.println();
	}

	private static List<String> getFilesToCheck(CommandLine cmd) {
		List<String> sourceFolderNames = null;
		if (cmd.hasOption("f")) {
			sourceFolderNames = stringListSplitter.split(cmd.getOptionValue("f"));
		}
		return sourceFolderNames;
	}

	private static void checkForFile(String fileName) {
		try {
			System.out.println("\nProcessing: " + fileName);
			JasperReport jasperReport = new FileReader(fileName).readFromFile();
			String fileContent = Files.readString(Path.of(fileName));
			listUnusedFieldsParametersAndVariablesToConsole(jasperReport, fileContent);
		} catch (IOException | JAXBException e) {
			throw new RuntimeException(
					"something went wrong while reading the JRXML file (" + fileName
							+ "): "
							+ e.getClass().getSimpleName()
							+ ": "
							+ e.getMessage());
		}
	}

	private static void listUnusedFieldsParametersAndVariablesToConsole(JasperReport jasperReport, String fileContent) {
		List<String> messages = new ArrayList<>();
		for (Field field : jasperReport.getFields()) {
			if (!fileContent.contains("$F{" + field.getName() + "}")) {
				messages.add("Field     " + field.getName());
			}
		}
		for (Parameter parameter : jasperReport.getParameters()) {
			if (!fileContent.contains("$P{" + parameter.getName() + "}")) {
				messages.add("Parameter " + parameter.getName());
			}
		}
		for (Variable variable : jasperReport.getVariables()) {
			if (!fileContent.contains("$V{" + variable.getName() + "}")) {
				messages.add("Variable  " + variable.getName());
			}
		}
		if (messages.isEmpty()) {
			System.out.println("No unused field, parameter or variable found.");
		} else {
			messages.stream().sorted((s0, s1) -> s0.compareTo(s1)).forEach(System.out::println);
		}
	}

}