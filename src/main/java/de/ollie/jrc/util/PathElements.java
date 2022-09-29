package de.ollie.jrc.util;

import java.util.ArrayList;
import java.util.List;

public class PathElements {

	private List<String> elements = new ArrayList<>();

	public PathElements(List<String> pathElements) {
		this.elements = pathElements;
	}

	public boolean hasMoreElements() {
		return !elements.isEmpty();
	}

	public String getFirstElement() {
		return elements.get(0);
	}

	public void removeFirstElement() {
		elements.remove(0);
	}

}
