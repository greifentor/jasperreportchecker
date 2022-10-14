package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.ollie.jrc.jrxml.NodeSampleDataGenerator;

class JRCTest {

	private static final String XML_FILE_NAME =
			"src/test/resources/test-reports/UnusedObjectChecker-FieldsParametersAndVariables.jrxml";
	private static final String UNKNOWN = NodeSampleDataGenerator.UNKNOWN;

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

	@Nested
	class TestsOfCommand_check {

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
			String fileName = "src/test/resources/test-reports/UnusedObjectChecker-NoUnused.jrxml";
			JRC.main(new String[] { "check", "-f", fileName });
			assertTrue(baos.toString().contains("No unused field, parameter or variable found."));
		}

		@Test
		void passFileWithNoUnusedObjectAndSuppressNothingFoundMessage_printsNothing() {
			String fileName = "src/test/resources/test-reports/UnusedObjectChecker-NoUnused.jrxml";
			JRC.main(new String[] { "check", "-f", fileName, "-snfm" });
			System.out.println("\"" + baos.toString() + "\"");
			assertTrue(baos.toString().isEmpty());
		}

	}

	@Nested
	class TestsOfCommand_xml {

		@Test
		void passParametersForAXMLCall_printsTheXML() {
			String fileName = "src/test/resources/test-reports/XMLBuilderChecker-XML-SimpleTest.jrxml";
			JRC.main(new String[] { "xml", "-f", fileName });
			assertEquals(
					"<root><commons><usedField>" + UNKNOWN + "</usedField></commons></root>",
					baos.toString().replace("\r", "").replace("\n", ""));
		}

		@Test
		void passParametersForAXMLCallWithSubreport_datasourceRootWithRootXMLPathPassed_printsTheXML() {
			String fileName =
					"src/test/resources/test-reports/XMLBuilderChecker-XML-WithSubreport-RootPathPassed.jrxml";
			JRC.main(new String[] { "xml", "-f", fileName, "-sd", "src/test/resources/test-reports/" });
			assertEquals(
					"<root><commons><usedField>" + UNKNOWN
							+ "</usedField><subreportField>"
							+ UNKNOWN
							+ "</subreportField></commons></root>",
					baos.toString().replace("\r", "").replace("\n", ""));
		}

		@Test
		void passParametersForAXMLCallWithSubreport_datasourceOwnWithOwnXMLPathPassed_printsTheXML() {
			String fileName = "src/test/resources/test-reports/XMLBuilderChecker-XML-WithSubreport-OwnPathPassed.jrxml";
			JRC.main(new String[] { "xml", "-f", fileName, "-sd", "src/test/resources/test-reports/" });
			assertEquals(
					"<root><commons><usedField>" + UNKNOWN
							+ "</usedField><subreport><subreportField0>"
							+ UNKNOWN
							+ "</subreportField0><sub><path><subreportField1>"
							+ UNKNOWN
							+ "</subreportField1></path></sub></subreport></commons></root>",
					baos.toString().replace("\r", "").replace("\n", ""));
		}

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