package de.ollie.jrc.xml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
public class XMLNode {

	private String className;
	private String name;
	private List<XMLNode> nodes = new ArrayList<>();

	public List<XMLNode> findAllNodesByName(String name) {
		return nodes.stream().filter(node -> node.getName().equals(name)).collect(Collectors.toList());
	}

}