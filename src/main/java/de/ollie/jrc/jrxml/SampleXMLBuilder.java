package de.ollie.jrc.jrxml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.Subreport;
import de.ollie.jrc.util.PathElements;
import de.ollie.jrc.xml.exception.DifferentRootNamesException;
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
			XMLNode rootNode = new XMLNode();
			forEachFieldWithSetFieldDescriptionCallAnXMLNodeAdder(report, rootNode);
			forEachSubreportCallConversionAgain(report);
			return rootNode;
		}
		return null;
	}

	private void forEachFieldWithSetFieldDescriptionCallAnXMLNodeAdder(JasperReport report, XMLNode xmlNode) {
		report
				.getFields()
				.stream()
				.filter(this::isFieldDescriptionSet)
				.forEach(field -> new XMLNodeAdder(field, xmlNode).addFieldAsXMLNode());
	}

	private boolean hasFieldWithFieldDescription(JasperReport report) {
		return report.getFields().stream().anyMatch(this::isFieldDescriptionSet);
	}

	private boolean isFieldDescriptionSet(Field field) {
		return (field.getFieldDescription() != null) && !field.getFieldDescription().isBlank();
	}

	private class XMLNodeAdder {

		private Field field;
		private PathElements pathElements;
		private XMLNode xmlNode;

		private XMLNodeAdder(Field field, XMLNode xmlNode) {
			this.field = field;
			this.xmlNode = xmlNode;
		}

		private void addFieldAsXMLNode() {
			splitFieldDescriptionBySlashesToPathElements();
			setNameForXMLNodeFromFirstPathElement();
			removeFirstPathElement();
			while (hasMorePathElements()) {
				findOrCreateNodeForFirstPathElementAndAddToCurrentNode();
				removeFirstPathElement();
			}
		}

		private void splitFieldDescriptionBySlashesToPathElements() {
			pathElements =
					new PathElements(new ArrayList<>(List.of(StringUtils.split(field.getFieldDescription(), "/"))));
		}

		private void setNameForXMLNodeFromFirstPathElement() {
			String name = pathElements.getFirstElement();
			if (xmlNode.getName() == null) {
				xmlNode.setName(name);
			} else if (!xmlNode.getName().equals(name)) {
				throw new DifferentRootNamesException(
						"field description '" + field.getFieldDescription()
								+ "' should start with:"
								+ xmlNode.getName());
			}
		}

		private void removeFirstPathElement() {
			pathElements.removeFirstElement();
		}

		private boolean hasMorePathElements() {
			return pathElements.hasMoreElements();
		}

		private void findOrCreateNodeForFirstPathElementAndAddToCurrentNode() {
			String nodeName = pathElements.getFirstElement();
			List<XMLNode> matchingNodes = xmlNode.findAllNodesByName(nodeName);
			if (matchingNodes.isEmpty()) {
				XMLNode newNode = new XMLNode().setName(nodeName);
				xmlNode.getNodes().add(newNode);
				xmlNode = newNode;
			} else {
				xmlNode = matchingNodes.get(0);
			}
		}

	}

	private void forEachSubreportCallConversionAgain(JasperReport report) {
		report
				.getDetails()
				.stream()
				.flatMap(detail -> detail.getBands().stream())
				.flatMap(band -> band.getSubreports().stream())
				.forEach(subreport -> {
					try {
					JasperReport r = new FileReader(getFileNameFromSubreportExpression(subreport)).readFromFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}

	private String getFileNameFromSubreportExpression(Subreport subreport) {
		String s = subreport.getSubreportExpression();
		String subreportDir = ""
		// process ${SUBREPORT_DIR}
		// "XMLBuilderChecker-XML-WithSubreport.jasper"
		return s;
	}

}
