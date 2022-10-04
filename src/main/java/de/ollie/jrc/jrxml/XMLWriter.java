package de.ollie.jrc.jrxml;

import java.io.PrintStream;

import de.ollie.jrc.xml.model.XMLNode;

public class XMLWriter {

	public void write(XMLNode rootNode, PrintStream printStream) {
		if (rootNode == null) {
			return;
		}
		printStream.print("<" + rootNode.getName() + ">");
		for (XMLNode node : rootNode.getNodes()) {
			write(node, printStream);
		}
		printStream.print("</" + rootNode.getName() + ">");
	}

}
