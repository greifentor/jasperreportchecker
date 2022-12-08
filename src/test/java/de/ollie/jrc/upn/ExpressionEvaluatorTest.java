package de.ollie.jrc.upn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;

@ExtendWith(MockitoExtension.class)
public class ExpressionEvaluatorTest {

	@InjectMocks
	private ExpressionEvaluator unitUnderTest;

	@Nested
	class TestsOfMethod_evaluate_List_Expression {

		@Test
		void returnsTheCorrectResult_passingAValidExpression() {
			// Prepare
			Value value0 = new Value().setType(Type.STRING).setValue("value");
			Value value1 = new Value().setType(Type.STRING).setValue("val");
			// Run & Check
			assertTrue(
					((Value) unitUnderTest.evaluate(List.of(value0, value1, CommandExpression.STARTS_WITH)).pop())
							.getValueAsBoolean());
		}

	}

}