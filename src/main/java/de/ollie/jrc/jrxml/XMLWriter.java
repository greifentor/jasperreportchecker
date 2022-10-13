package de.ollie.jrc.jrxml;

import java.io.PrintStream;

import de.ollie.jrc.xml.model.XMLNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class XMLWriter {

	private final NodeSampleDataGenerator nodeSampleDateGenerator;

	public void write(XMLNode rootNode, PrintStream printStream) {
		if (rootNode == null) {
			return;
		}
		printStream.print("<" + rootNode.getName() + ">");
		if (rootNode.getNodes().isEmpty()) {
			printStream.print(nodeSampleDateGenerator.getSampleDateFor(rootNode));
		}
		for (XMLNode node : rootNode.getNodes()) {
			write(node, printStream);
		}
		printStream.print("</" + rootNode.getName() + ">");
	}

}
