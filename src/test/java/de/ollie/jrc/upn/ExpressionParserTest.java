package de.ollie.jrc.upn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.upn.model.CommandExpression;
import de.ollie.jrc.upn.model.Expression;
import de.ollie.jrc.upn.model.Type;
import de.ollie.jrc.upn.model.Value;

@ExtendWith(MockitoExtension.class)
public class ExpressionParserTest {

	@InjectMocks
	private ExpressionParser unitUnderTest;

	@Nested
	class TestsOfMethod_parse_String {

		@Test
		void throwsAnException_passingANullValue() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.parse(null));
		}

		@Test
		void returnsAnEmptyList_passingAnEmptyString() {
			assertTrue(unitUnderTest.parse("").isEmpty());
		}

		@Test
		void returnsAListWithARelatedValueExpression_passingAStringWithAStringValue_NotQuoted() {
			// Prepare
			String value = "Blubs";
			Value expected = new Value().setType(Type.STRING).setValue(value);
			// Run
			List<Expression> returned = unitUnderTest.parse(value);
			// Check
			assertEquals(expected, returned.get(0));
		}

		@Test
		void returnsAListWithARelatedValueExpression_passingAStringWithAStringValue_Quoted() {
			// Prepare
			String value = "Blu bs";
			Value expected = new Value().setType(Type.STRING).setValue(value);
			// Run
			List<Expression> returned = unitUnderTest.parse("'" + value + "'");
			// Check
			assertEquals(expected, returned.get(0));
		}

		@ParameterizedTest
		@EnumSource(value = CommandExpression.class)
		void returnsAListWithARelatedCommandExpression_passingAStringWithACommand_STARTS_WITH(
				CommandExpression expression) {
			List<Expression> expected = List.of(expression);
			List<Expression> returned = unitUnderTest.parse(expression.getToken());
			assertEquals(expected, returned);
		}

	}

}
