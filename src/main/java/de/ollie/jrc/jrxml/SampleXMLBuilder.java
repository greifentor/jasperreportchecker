package de.ollie.jrc.jrxml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.xml.model.XMLNode;

public class SampleXMLBuilder {
	
	public XMLNode buildXMLFromJasperReport(JasperReport report) {
		if (report == null) {
			return null;
		}
		return convertReportToXMLNode(report);
	}

	private XMLNode convertReportToXMLNode(JasperReport report) {
		if (hasFieldWithFieldDescription(report)) {
			XMLNode xmlNode = new XMLNode();
			report
					.getFields()
					.stream()
					.filter(this::isFieldDescriptionSet)
					.forEach(field -> addToXMLNode(field, xmlNode));
			return xmlNode;
		}
		return null;
	}

	private boolean hasFieldWithFieldDescription(JasperReport report) {
		return report.getFields().stream().anyMatch(this::isFieldDescriptionSet);
	}

	private boolean isFieldDescriptionSet(Field field) {
		return (field.getFieldDescription() != null) && !field.getFieldDescription().isBlank();
	}

	private void addToXMLNode(Field field, XMLNode xmlNode) {
		List<String> pathElements = new ArrayList<>(List.of(StringUtils.split(field.getFieldDescription(), "/")));
		if (xmlNode.getName() == null) {
			xmlNode.setName(pathElements.get(0));
		}
		pathElements.remove(0);
		while (!pathElements.isEmpty()) {
			String nodeName = pathElements.get(0);
			List<XMLNode> matchingNodes = xmlNode.findAllNodesByName(nodeName);
			if (matchingNodes.isEmpty()) {
				XMLNode newNode = new XMLNode().setName(nodeName);
				xmlNode.getNodes().add(newNode);
				xmlNode = newNode;
			} else {
				xmlNode = matchingNodes.get(0);
			}
			pathElements.remove(0);

		}
	}

}
