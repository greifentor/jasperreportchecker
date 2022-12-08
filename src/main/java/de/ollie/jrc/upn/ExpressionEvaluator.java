package de.ollie.jrc.upn;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Expression;

public class ExpressionEvaluator {

	private Map<CommandExpression, Function<Stack<Expression>, Stack<Expression>>> acceptedCommands = new HashMap<>();

	public ExpressionEvaluator() {
		acceptedCommands.put(CommandExpression.STARTS_WITH, stack -> stack);
	}

}