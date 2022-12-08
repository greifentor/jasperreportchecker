package de.ollie.jrc.upn.command;

import de.ollie.jrc.upn.Command;
import de.ollie.jrc.upn.ExpressionStack;
import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;

public class StartsWithCommand implements Command {

	@Override
	public CommandExpression getCommandExpression() {
		return CommandExpression.STARTS_WITH;
	}

	@Override
	public ExpressionStack exec(ExpressionStack stack) {
		String pattern = stack.popString();
		String source = stack.popString();
		stack.push(new Value().setType(Type.BOOLEAN).setValue("" + source.startsWith(pattern)));
		return stack;
	}

}
