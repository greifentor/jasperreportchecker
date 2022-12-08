package de.ollie.jrc.upn.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.upn.ExpressionStack;
import de.ollie.jrc.upn.ExpressionStack.PopOnEmptyStackException;
import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;

@ExtendWith(MockitoExtension.class)
class StartsWithCommandTest {

	@InjectMocks
	private StartsWithCommand unitUnderTest;

	@Nested
	class TestsOfMethod_getCommandExpression {

		@Test
		void returnsTheCorrectCommandExpression() {
			assertEquals(CommandExpression.STARTS_WITH, unitUnderTest.getCommandExpression());
		}

	}

	@Nested
	class TestsOfMethod_exec_Stack_Expression {

		@Test
		void returnsAStackWithASingleBooleanValue_passingAStackWithTwoStringValues() {
			// Prepare
			Value value0 = new Value().setType(Type.STRING).setValue("value0");
			Value value1 = new Value().setType(Type.STRING).setValue("value1");
			// Run
			Value returned = (Value) unitUnderTest.exec(ExpressionStack.of(value0, value1)).pop();
			// Run & Check
			assertEquals(Type.BOOLEAN, returned.getType());
		}

		@Test
		void returnsAStackWithASingleBooleanValue_passingAStackWithTwoStringValues_String0StartsWithString1() {
			// Prepare
			Value value0 = new Value().setType(Type.STRING).setValue("value0");
			Value value1 = new Value().setType(Type.STRING).setValue("val");
			// Run
			Value returned = (Value) unitUnderTest.exec(ExpressionStack.of(value0, value1)).pop();
			// Run & Check
			assertTrue(returned.getValueAsBoolean());
		}

		@Test
		void throwsAnException_passingAStackWithLessThanTwoElements() {
			assertThrows(PopOnEmptyStackException.class, () -> unitUnderTest.exec(ExpressionStack.of()));
		}

	}

}
