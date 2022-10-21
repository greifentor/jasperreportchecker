package de.ollie.jrc.jrxml;

import de.ollie.jrc.xml.model.XMLNode;

public class NodeSampleDataGenerator {

	static final String BOOLEAN_CLASS_NAME = "java.util.Boolean";
	static final String STRING_CLASS_NAME = "java.util.String";

	public static final String UNKNOWN = "!!!UNKNOWN!!!";

	public Object getSampleDateFor(XMLNode node) {
		if (BOOLEAN_CLASS_NAME.equals(node.getClassName())) {
			return true;
		} else if (STRING_CLASS_NAME.equals(node.getClassName())) {
			return node.getName().toUpperCase();
		} else if (node.getClassName() == null) {
			return null;
		}
		return UNKNOWN;
	}

}
