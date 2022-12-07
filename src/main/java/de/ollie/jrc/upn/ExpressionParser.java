package de.ollie.jrc.upn;

import static de.ollie.jrc.util.Checks.ensure;

import java.util.List;
import java.util.stream.Collectors;

import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Expression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;

public class ExpressionParser {

	public List<Expression> parse(String s) {
		ensure(s != null, "String to parse cannot be null.");
		return Tokenizer.INSTANCE.getTokens(s).stream().map(this::getExpression).collect(Collectors.toList());
	}

	private Expression getExpression(String token) {
		for (Expression expression : CommandExpression.values()) {
			if (expression.getToken().equals(token)) {
				return expression;
			}
		}
		return new Value().setType(Type.STRING).setValue(token);
	}

}
