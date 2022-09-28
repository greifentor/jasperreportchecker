package de.ollie.jrc.jrxml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
@XmlRootElement(namespace = "")
@XmlAccessorType(XmlAccessType.FIELD) // That's vital to use the other annotations.
public class JasperReport {

	@XmlElement(name = "field")
	private List<Field> fields = new ArrayList<>();
	@XmlElement(name = "parameter")
	private List<Parameter> parameters = new ArrayList<>();
	@XmlElement(name = "variable")
	private List<Variable> variables = new ArrayList<>();

	public Optional<Field> findFieldByName(String name) {
		return fields.stream().filter(field -> field.getName().equals(name)).findFirst();
	}

}