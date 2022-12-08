package de.ollie.jrc.upn;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.upn.ExpressionStack.PopOnEmptyStackException;
import de.ollie.jrc.upn.model.Expression;

@ExtendWith(MockitoExtension.class)
public class ExpressionStackTest {

	@Mock
	private Expression expression1;
	@Mock
	private Expression expression2;

	@InjectMocks
	private ExpressionStack unitUnderTest;

	@Nested
	class TestsOfMethod_of_Expressions {

		@Test
		void throwsAnException_passingANullValue() {
			assertThrows(NullPointerException.class, () -> ExpressionStack.of(null));
		}

		@Test
		void throwsAnException_passingAnExpressionAsNullValue() {
			assertThrows(NullPointerException.class, () -> ExpressionStack.of((Expression) null));
		}

		@Test
		void createsAStackWithThePassedExpression_passingAnExpression() {
			assertSame(expression1, ExpressionStack.of(expression1).peek());
		}

		@Test
		void createsAStackWithTheTwoPassedExpression_passingAnTwoExpression() {
			assertSame(2, ExpressionStack.of(expression1, expression2).size());
		}

	}

	@Nested
	class TestsOfMethod_pop {

		@Test
		void returnsTheExpressionFromTheTopOfTheStack() {
			// Prepare
			unitUnderTest.push(expression1);
			unitUnderTest.push(expression2);
			// Run & Check
			assertSame(expression2, unitUnderTest.pop());
		}

		@Test
		void leftStackEmpty_popTheLastElement() {
			unitUnderTest.push(expression2);
			unitUnderTest.pop();
			assertTrue(unitUnderTest.isEmpty());
		}

		@Test
		void throwsAnException_callingPopOnAnEmptyStack() {
			assertThrows(PopOnEmptyStackException.class, () -> unitUnderTest.pop());
		}

		@Test
		void throwsAnExceptionWithAReferenceToTheStack_callingPopOnAnEmptyStack() {
			PopOnEmptyStackException e = assertThrows(PopOnEmptyStackException.class, () -> unitUnderTest.pop());
			assertSame(unitUnderTest, e.getStack());
		}

	}

}
