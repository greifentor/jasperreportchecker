package de.ollie.jrc.jrxml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
@XmlAccessorType(XmlAccessType.FIELD)
public class Band {

	@XmlElement(name = "staticText")
	private List<StaticText> staticTexts = new ArrayList<>();
	@XmlElement(name = "subreport")
	private List<Subreport> subreports = new ArrayList<>();
	@XmlElement(name = "textField")
	private List<TextField> textFields = new ArrayList<>();

	public List<TextElement> findAllTextElements() {
		List<TextElement> textElements = new ArrayList<>();
		textElements.addAll(findAllTextElements(staticTexts));
		textElements.addAll(findAllTextElements(textFields));
		return textElements;
	}

	private List<TextElement> findAllTextElements(List<? extends TextElementProvider> textElementProviders) {
		return textElementProviders.stream().map(TextElementProvider::getTextElement).collect(Collectors.toList());
	}

}
