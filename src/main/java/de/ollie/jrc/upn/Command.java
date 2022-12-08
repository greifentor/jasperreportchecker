package de.ollie.jrc.upn;

import de.ollie.jrc.upn.model.CommandExpression;

public interface Command {

	CommandExpression getCommandExpression();

	ExpressionStack exec(ExpressionStack stack);

}
