package de.ollie.jrc.jrxml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.Parameter;
import de.ollie.jrc.jrxml.model.Variable;
import de.ollie.jrc.util.StringListSplitter;

public class UnusedObjectChecker {

	public List<String> checkForUnusedFieldsParametersAndVariables(String jrxmlFileName, String excludes) {
		try {
			JasperReport jasperReport = new FileReader(jrxmlFileName).readFromFile();
			String fileContent = Files.readString(Path.of(jrxmlFileName));
			return listUnusedFieldsParametersAndVariablesToConsole(jasperReport, fileContent, excludes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"something went wrong while reading the JRXML file (" + jrxmlFileName
							+ "): "
							+ e.getClass().getSimpleName()
							+ ": "
							+ e.getMessage());
		}
	}

	private List<String> listUnusedFieldsParametersAndVariablesToConsole(JasperReport jasperReport, String fileContent,
			String excludes) {
		List<String> messages = new ArrayList<>();
		List<String> exclude = StringListSplitter.INSTANCE.split(excludes);
		exclude = exclude != null ? exclude : List.of();
		for (Field field : jasperReport.getFields()) {
			String name = "$F{" + field.getName() + "}";
			if (!fileContent.contains(name) && !exclude.contains(name)) {
				messages.add("Field " + field.getName());
			}
		}
		for (Parameter parameter : jasperReport.getParameters()) {
			String name = "$P{" + parameter.getName() + "}";
			if (!fileContent.contains(name) && !exclude.contains(name)) {
				messages.add("Parameter " + parameter.getName());
			}
		}
		for (Variable variable : jasperReport.getVariables()) {
			String name = "$V{" + variable.getName() + "}";
			if (!fileContent.contains(name) && !exclude.contains(name)) {
				messages.add("Variable " + variable.getName());
			}
		}
		return messages;
	}

}
