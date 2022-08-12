package de.ollie.jrc.jrxml;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.ollie.jrc.jrxml.model.JasperReport;

public class FileReader {

	private String jrxml;
	private String jrxmlFileName;
	private List<String> lines;

	public FileReader(String jrxmlFileName) {
		this.jrxmlFileName = jrxmlFileName;
	}

	public synchronized JasperReport readFromFile() throws IOException, JAXBException {
		readJRXMLFileToString();
		return convertJRXMLToJasperReportObject();
	}

	private void readJRXMLFileToString() throws IOException {
		readJRXMLFileLineByLine();
		cleanUpJasperReportTag();
		reduceLinesToASingleString();
	}

	private void readJRXMLFileLineByLine() throws IOException {
		lines = Files.readAllLines(Path.of(jrxmlFileName));
	}

	private void cleanUpJasperReportTag() {
		int i = findFirstLineWithJasperReportTag();
		lines.remove(i);
		lines.add(i, "<jasperReport>");
	}

	private int findFirstLineWithJasperReportTag() {
		for (int i = 0, leni = lines.size(); i < leni; i++) {
			if (lines.get(i).startsWith("<jasperReport")) {
				return i;
			}
		}
		return -1;
	}

	private void reduceLinesToASingleString() {
		jrxml = lines.stream().reduce((line0, line1) -> line0 + "\n" + line1).orElse("");
	}

	private JasperReport convertJRXMLToJasperReportObject() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(JasperReport.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (JasperReport) jaxbUnmarshaller.unmarshal(new StringReader(jrxml));
	}

}
