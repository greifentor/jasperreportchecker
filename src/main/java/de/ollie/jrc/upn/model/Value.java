package de.ollie.jrc.upn.model;

import static de.ollie.jrc.util.Checks.ensure;

import de.ollie.jrc.upn.ExpressionStack.UnsuitableTypeRequestedByPopException;
import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
public class Value implements Expression {

	private String value;
	private Type type;

	public Boolean getValueAsBoolean() {
		ensure(type == Type.BOOLEAN, () -> new UnsuitableTypeRequestedByPopException(Type.STRING, Type.BOOLEAN, null));
		return Boolean.valueOf(value);
	}

	@Override
	public String getToken() {
		return value + "$" + type.name();
	}

}