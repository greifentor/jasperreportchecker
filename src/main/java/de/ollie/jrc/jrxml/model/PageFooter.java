package de.ollie.jrc.jrxml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
@XmlAccessorType(XmlAccessType.FIELD)
public class PageFooter implements BandProvider {

	@XmlElement(name = "band")
	private List<Band> bands = new ArrayList<>();

}
