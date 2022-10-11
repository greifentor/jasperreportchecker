package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
	void constructorThrowsAnException() {
		assertThrows(UnsupportedOperationException.class, () -> new JRC());
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
		String fileName = "not/existing/f.ile";
		JRC.main(new String[] { "check", "-f", fileName });
		assertTrue(baos.toString().contains("NoSuchFileException:"));
	}

	@Test
	void passFileWithNoUnusedObject_printsAMessage() {
		String fileName = "src/test/resources/test-report/UnusedObjectChecker-NoUnused.jrxml";
		JRC.main(new String[] { "check", "-f", fileName });
		assertTrue(
				baos
						.toString()
						.contains("No unused field, parameter or variable found."));
	}

	@Test
	void passFileWithNoUnusedObjectAndSuppressNothingFoundMessage_printsNothing() {
		String fileName = "src/test/resources/test-report/UnusedObjectChecker-NoUnused.jrxml";
		JRC.main(new String[] { "check", "-f", fileName, "-snfm" });
		System.out.println("\"" + baos.toString() + "\"");
		assertTrue(baos.toString().isEmpty());
	}

	@Test
	void passParametersForAXMLCall_printsTheXML() {
		String fileName = "src/test/resources/test-report/XMLBuilderChecker-XML-SimpleTest.jrxml";
		JRC.main(new String[] { "xml", "-f", fileName });
		assertEquals(
				"<root><commons><usedField></usedField></commons></root>",
				baos.toString().replace("\r", "").replace("\n", ""));
	}

	@Test
	void passParametersForAXMLCallWithSubreport_datasourceRootPassed_printsTheXML() {
		String fileName = "src/test/resources/test-report/XMLBuilderChecker-XML-WithSubreport.jrxml";
		JRC.main(new String[] { "xml", "-f", fileName, "-sd", "src/test/resources/test-report/" });
		assertEquals(
				"<root><commons><usedField></usedField><subreportField></subreportField></commons></root>",
				baos.toString().replace("\r", "").replace("\n", ""));
	}

	@Test
	void test() {
		String dir =
				"C:\\workspace\\basisdienst-dokumente-service-templates\\src\\main\\docker\\data\\kita\\efoeb-bedarfsbescheid\\";
		JRC.main(new String[] { "check", "-d", dir, "-p", "*.jrxml" });
		System.out.println(baos.toString());
	}

	@Test
	void test0() {
		String dir =
				"C:/workspace/basisdienst-dokumente-service-templates/src/main/docker/data/kita/efoeb-bedarfsbescheid/";
		JRC.main(new String[] { "check", "-d", dir, "-p", "*.jrxml", "-snfm" });
		System.out.println(baos.toString());
	}
	
}