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

	public XMLNode buildXMLFromJasperReport(JasperReport report, String subreportDirectory) {
		if (report == null) {
			return null;
		}
		return convertReportToXMLNode(report, subreportDirectory);
	}

	private XMLNode convertReportToXMLNode(JasperReport report, String subreportDirectory) {
		// if (hasFieldWithFieldDescription(report)) {
		XMLNode rootNode = new XMLNode();
		forEachFieldWithSetFieldDescriptionCallAnXMLNodeAdder(report, rootNode, "");
		forEachSubreportCallConversionAgain(report, subreportDirectory, rootNode);
		return rootNode;
		// }
		// return null;
	}

	private void forEachFieldWithSetFieldDescriptionCallAnXMLNodeAdder(JasperReport report, XMLNode xmlNode,
			String descriptionPrefix) {
		report
				.getFields()
				.stream()
				// .filter(this::isFieldDescriptionSet)
				.forEach(field -> new XMLNodeAdder(field, xmlNode, descriptionPrefix).addFieldAsXMLNode());
	}

	private boolean hasFieldWithFieldDescription(JasperReport report) {
		return report.getFields().stream().anyMatch(this::isFieldDescriptionSet);
	}

	private boolean isFieldDescriptionSet(Field field) {
		return (field.getFieldDescription() != null) && !field.getFieldDescription().isBlank();
	}

	private class XMLNodeAdder {

		private String descriptionPrefix;
		private String fieldPath;
		private PathElements pathElements;
		private XMLNode xmlNode;

		private XMLNodeAdder(Field field, XMLNode xmlNode, String descriptionPrefix) {
			this.descriptionPrefix = descriptionPrefix;
			this.fieldPath = (field.getFieldDescription() != null) && !field.getFieldDescription().isEmpty()
					? field.getFieldDescription()
					: field.getName();
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
					new PathElements(new ArrayList<>(List.of(StringUtils.split(descriptionPrefix + fieldPath, "/"))));
		}

		private void setNameForXMLNodeFromFirstPathElement() {
			String name = pathElements.getFirstElement();
			if (xmlNode.getName() == null) {
				xmlNode.setName(name);
			} else if (!xmlNode.getName().equals(name)) {
				throw new DifferentRootNamesException(
						"field description '" + descriptionPrefix
								+ fieldPath
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

	private void forEachSubreportCallConversionAgain(JasperReport report, String subreportDirectory, XMLNode rootNode) {
		report
				.getDetails()
				.stream()
				.flatMap(detail -> detail.getBands().stream())
				.flatMap(band -> band.getSubreports().stream())
				.forEach(subreport -> {
					try {
						System.out
								.println(
										"processing subreport: "
												+ getFileNameFromSubreportExpression(subreport, subreportDirectory));
						JasperReport r =
								new FileReader(getFileNameFromSubreportExpression(subreport, subreportDirectory))
										.readFromFile();
						forEachFieldWithSetFieldDescriptionCallAnXMLNodeAdder(
								r,
								rootNode,
								getDescriptionPrefix(subreport));
						forEachSubreportCallConversionAgain(r, subreportDirectory, rootNode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}

	private String getFileNameFromSubreportExpression(Subreport subreport, String subreportDirectory) {
		return subreport
				.getSubreportExpression()
				.replace(" ", "")
				.replace("$P{SUBREPORT_DIR}+\"", subreportDirectory + (!subreportDirectory.endsWith("/") ? "/" : ""))
				.replace(".jasper\"", ".jrxml")
				.replace("\\", "/");
	}

	private String getDescriptionPrefix(Subreport subreport) {
		String s = subreport
				.getDataSourceExpression()
				.replace("new net.sf.jasperreports.engine.JREmptyDataSource()", "")
				.replace(
						"((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).subDataSource(\"",
						"")
				.replace("((net.sf.jasperreports.engine.data.JRXmlDataSource)$P{REPORT_DATA_SOURCE}).dataSource(\"", "")
				.replace("\")", "");
		return s + (!s.endsWith("/") ? "/" : "");
	}

}
