package de.ollie.jrc.upn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.ollie.jrc.upn.command.StartsWithCommand;
import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Expression;
import de.ollie.jrc.upn.model.Value;

public class ExpressionEvaluator {

	private Map<CommandExpression, Function<ExpressionStack, ExpressionStack>> acceptedCommands = new HashMap<>();

	private static final Command startsWithCommand = new StartsWithCommand();

	public ExpressionEvaluator() {
		acceptedCommands.put(CommandExpression.STARTS_WITH, stack -> startsWithCommand.exec(stack));
	}

	public ExpressionStack evaluate(List<Expression> expressions) {
		ExpressionStack valueStack = ExpressionStack.of();
		for (Expression expression : expressions) {
			if (expression instanceof Value) {
				valueStack.push(expression);
			} else {
				valueStack = acceptedCommands.get(expression).apply(valueStack);
			}
		}
		return valueStack;
	}

}