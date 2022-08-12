package de.ollie.jrc.jrxml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.Parameter;
import de.ollie.jrc.jrxml.model.Variable;

public class UnusedObjectChecker {

	public List<String> checkForUnusedFieldsParametersAndVariables(String jrxmlFileName) {
		try {
			JasperReport jasperReport = new FileReader(jrxmlFileName).readFromFile();
			String fileContent = Files.readString(Path.of(jrxmlFileName));
			return listUnusedFieldsParametersAndVariablesToConsole(jasperReport, fileContent);
		} catch (IOException | JAXBException e) {
			throw new RuntimeException(
					"something went wrong while reading the JRXML file (" + jrxmlFileName
							+ "): "
							+ e.getClass().getSimpleName()
							+ ": "
							+ e.getMessage());
		}
	}

	private List<String> listUnusedFieldsParametersAndVariablesToConsole(JasperReport jasperReport,
			String fileContent) {
		List<String> messages = new ArrayList<>();
		for (Field field : jasperReport.getFields()) {
			if (!fileContent.contains("$F{" + field.getName() + "}")) {
				messages.add("Field " + field.getName());
			}
		}
		for (Parameter parameter : jasperReport.getParameters()) {
			if (!fileContent.contains("$P{" + parameter.getName() + "}")) {
				messages.add("Parameter " + parameter.getName());
			}
		}
		for (Variable variable : jasperReport.getVariables()) {
			if (!fileContent.contains("$V{" + variable.getName() + "}")) {
				messages.add("Variable " + variable.getName());
			}
		}
		return messages;
	}

}
