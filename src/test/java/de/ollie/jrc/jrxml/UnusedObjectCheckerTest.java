package de.ollie.jrc.jrxml;

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
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME)
						.contains("Field unusedField"));
	}

	@Test
	void returnsAnUnusedParameter() {
		assertTrue(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME)
						.contains("Parameter unusedParameter"));
	}

	@Test
	void returnsAnUnusedVariable() {
		assertTrue(
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables(JRXML_FILE_NAME)
						.contains("Variable unusedVariable"));
	}

	@Test
	void throwsAnExceptionPassingANonExistingFileName() {
		assertThrows(
				RuntimeException.class,
				() -> 
				unitUnderTest
						.checkForUnusedFieldsParametersAndVariables("/non/existing/file.name")
						.contains("Variable unusedVariable"));
	}

}
