package de.ollie.jrc.jrxml;

import java.io.PrintStream;

import de.ollie.jrc.xml.model.XMLNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class XMLWriter {

	private final NodeSampleDataGenerator nodeSampleDateGenerator;

	public String write(XMLNode rootNode, PrintStream printStream) {
		StringBuilder sb = new StringBuilder();
		if (rootNode == null) {
			return "";
		}
		String s = "<" + rootNode.getName() + ">";
		printStream.print(s);
		sb.append(s);
		if (rootNode.getNodes().isEmpty()) {
			s = String.valueOf(nodeSampleDateGenerator.getSampleDateFor(rootNode));
			printStream.print(s);
			sb.append(s);
		}
		for (XMLNode node : rootNode.getNodes()) {
			sb.append(write(node, printStream));
		}
		s = "</" + rootNode.getName() + ">";
		printStream.print(s);
		sb.append(s);
		return sb.toString();
	}

}
