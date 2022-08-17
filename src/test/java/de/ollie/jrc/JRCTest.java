package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JRCTest {

	private static final String XML_FILE_NAME =
			"src/test/resources/test-report/UnusedObjectChecker-FieldsParametersAndVariables.jrxml";

	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	@BeforeEach
	void prepareByteArrayOutputStream() {
		baos = new ByteArrayOutputStream();
		JRC.out = new PrintStream(baos);
	}

	@Test
	void runsWithoutException() {
		try {
			JRC.main(new String[] { "check", "-f", XML_FILE_NAME });
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	@Test
	void noExceptionForNullParameters() {
		try {
			JRC.main(null);
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	@Test
	void passParameters_listsAUsageInformation() throws Exception {
		JRC.main(new String[0]);
		assertTrue(baos.toString().startsWith("\nUsage:"));
	}

	@Test
	void passHelpCommand_listsAUsageInformation() throws Exception {
		JRC.main(new String[] { "help" });
		assertTrue(baos.toString().startsWith("\nUsage:"));
	}

	@Test
	void passNoFileParameters_printsAnErrorMessage() {
		JRC.main(new String[] { "check", "aString" });
		assertTrue(baos.toString().startsWith("\nNo matching files found!"));
	}

	@Test
	void passWrongOption_printsAnErrorMessage() {
		JRC.main(new String[] { "check", "-x", "aString" });
		assertTrue(baos.toString().startsWith("ParseException:"));
	}

	@Test
	void passNoExistingFile_printsAnErrorMessage() {
		String fileName = ":o)";
		JRC.main(new String[] { "check", "-f", fileName });
		assertTrue(baos.toString().startsWith("\nProcessing: " + fileName + "\nRuntimeException:"));
	}

	@Test
	void passFileWithNoUnusedObject_printsAnErrorMessage() {
		String fileName = "src/test/resources/test-report/UnusedObjectChecker-NoUnused.jrxml";
		JRC.main(new String[] { "check", "-f", fileName });
		System.out.println(baos.toString());
		assertTrue(
				baos
						.toString()
						.startsWith("\nProcessing: " + fileName + "\nNo unused field, parameter or variable found."));
	}

}