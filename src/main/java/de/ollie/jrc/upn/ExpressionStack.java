package de.ollie.jrc.upn;

import static de.ollie.jrc.util.Checks.ensure;

import java.util.Stack;

import de.ollie.jrc.upn.model.Expression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;
import lombok.Getter;

public class ExpressionStack {

	@Getter
	public static class NotAValueExpressionException extends RuntimeException {

		private ExpressionStack stack;

		public NotAValueExpressionException(ExpressionStack stack) {
			super("Expression is not a value!");
			this.stack = stack;
		}

	}

	@Getter
	public static class PopOnEmptyStackException extends RuntimeException {

		private ExpressionStack stack;

		public PopOnEmptyStackException(ExpressionStack stack) {
			super("Stack is empty!");
			this.stack = stack;
		}

	}

	@Getter
	public static class UnsuitableTypeRequestedByPopException extends RuntimeException {

		private Type expectedType;
		private ExpressionStack stack;
		private Type valueType;

		public UnsuitableTypeRequestedByPopException(Type expectedType, Type valueType, ExpressionStack stack) {
			super("Unsuitable type requested by pop. Expected:" + expectedType.name() + ", Value:" + valueType.name());
			this.expectedType = expectedType;
			this.stack = stack;
			this.valueType = valueType;
		}

	}

	private final Stack<Expression> stack = new Stack<>();

	public static ExpressionStack of(Expression... expressions) {
		ExpressionStack stack = new ExpressionStack();
		for (Expression expression : expressions) {
			stack.push(expression);
		}
		return stack;
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public Expression peek() {
		return stack.peek();
	}

	public Expression pop() {
		ensure(!stack.isEmpty(), () -> new PopOnEmptyStackException(this));
		return stack.pop();
	}

	public String popString() {
		Expression expression = pop();
		ensure(expression instanceof Value, () -> new NotAValueExpressionException(this));
		Value value = (Value) expression;
		ensure(
				value.getType() == Type.STRING,
				() -> new UnsuitableTypeRequestedByPopException(Type.STRING, value.getType(), this));
		return value.getValue();
	}

	public Expression push(Expression expression) {
		ensure(expression != null, () -> new NullPointerException("expression to push cannot be null!"));
		return stack.push(expression);
	}

	public int size() {
		return stack.size();
	}

}
