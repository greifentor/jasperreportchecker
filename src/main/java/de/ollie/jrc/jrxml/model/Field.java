package de.ollie.jrc.jrxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {

	@XmlAttribute(name = "class")
	private String cls;
	@XmlAttribute
	private String name;

	private String fieldDescription;

}
