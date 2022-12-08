package de.ollie.jrc.upn.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
public class Value implements Expression {

	private String value;
	private Type type;

	public Boolean asBoolean() {
		return false;
	}

	@Override
	public String getToken() {
		return value + "$" + type.name();
	}

}