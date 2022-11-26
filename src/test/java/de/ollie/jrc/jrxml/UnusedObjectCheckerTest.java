package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnusedObjectCheckerTest {
	
	private static final String JRXML_FILE_NAME =
			"src/test/resources/test-reports/UnusedObjectChecker-FieldsParametersAndVariables.jrxml";
	
	@InjectMocks
	private UnusedObjectChecker unitUnderTest;
	
	@Test
	void returnsAnUnusedField() {
		assertTrue(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, null)
						.contains("Field unusedField"));
	}

	@Test
	void returnsNothingIfTheUnusedFieldIsExcluded() {
		assertFalse(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, "$F{unusedField}")
						.contains("Field unusedField"));
	}

	@Test
	void returnsAnUnusedParameter() {
		assertTrue(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, null)
						.contains("Parameter unusedParameter"));
	}

	@Test
	void returnsNothingIfTheUnusedParameterIsExcluded() {
		assertFalse(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, "$P{unusedParameter}")
						.contains("Parameter unusedParameter"));
	}

	@Test
	void returnsAnUnusedVariable() {
		assertTrue(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, null)
						.contains("Variable unusedVariable"));
	}

	@Test
	void returnsNothingIfTheUnusedVariableIsExcluded() {
		assertFalse(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME, "$V{unusedVariable}")
						.contains("Variable unusedVariable"));
	}

	@Test
	void throwsAnExceptionPassingANonExistingFileName() {
		assertThrows(
				RuntimeException.class,
				() -> 
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables("/non/existing/file.name", null)
						.contains("Variable unusedVariable"));
	}

}
