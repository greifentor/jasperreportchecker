package de.ollie.jrc.upn.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.upn.ExpressionStack.UnsuitableTypeRequestedByPopException;

@ExtendWith(MockitoExtension.class)
public class ValueTest {

	@Nested
	class TestsOfMethod_getAsBoolean {

		@Test
		void throwsAnException_callingTheMethodOnNotBooleanValue() {
			Value value = new Value().setType(Type.STRING).setValue("string");
			assertThrows(UnsuitableTypeRequestedByPopException.class, () -> value.getValueAsBoolean());
		}

		@Test
		void returnsTrue_callingTheMethodWithATrueBooleanValue() {
			Value value = new Value().setType(Type.BOOLEAN).setValue("true");
			assertTrue(value.getValueAsBoolean());
		}

		@Test
		void returnsFalse_callingTheMethodWithAFalseBooleanValue() {
			Value value = new Value().setType(Type.BOOLEAN).setValue("false");
			assertFalse(value.getValueAsBoolean());
		}

		@Test
		void returnsFalse_callingTheMethodWithANonBooleanValue() {
			Value value = new Value().setType(Type.BOOLEAN).setValue(";op");
			assertFalse(value.getValueAsBoolean());
		}

	}

}