package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.xml.bind.UnmarshalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileReaderTest {

	private static final String XML_FILE_NAME = "src/test/resources/test-reports/test-report.jrxml";

	private FileReader unitUnderTest;

	@BeforeEach
	void setUp() {
		unitUnderTest = new FileReader(XML_FILE_NAME);
	}

	@Nested
	class TestsOfMethod_readFromFile_String {

		@Test
		void readsTheTestReportWithoutExceptions() {
			try {
				unitUnderTest.readFromFile();
			} catch (Exception e) {
				fail(
						"should not throw any exception, but throws: " + e.getClass().getSimpleName()
								+ ": "
								+ e.getMessage());
			}
		}

		@Test
		void returnsAJasperReportObject() throws Exception {
			assertNotNull(unitUnderTest.readFromFile());
		}

		@Test
		void returndJasperReportHasFields() throws Exception {
			assertFalse(unitUnderTest.readFromFile().getFields().isEmpty());
		}

		@Test
		void returndJasperReportHasParameters() throws Exception {
			assertFalse(unitUnderTest.readFromFile().getParameters().isEmpty());
		}

		@Test
		void returndJasperReportHasVariables() throws Exception {
			assertFalse(unitUnderTest.readFromFile().getVariables().isEmpty());
		}

		@Test
		void throwsAnExceptionForNoJasperReportFiles() throws Exception {
			assertThrows(
					UnmarshalException.class,
					() -> new FileReader("src/test/resources/test-reports/NoJasperReport.jrxml").readFromFile());
		}

		@Test
		void setTheNameOfTheFieldCorrectly() throws Exception {
			assertTrue(
					unitUnderTest
							.readFromFile()
							.getFields()
							.stream()
							.anyMatch(field -> field.getName().equals("usedField")));
		}

		@Test
		void setTheDescriptionOfTheFieldCorrectly() throws Exception {
			assertEquals(
					"aFieldDescription",
					unitUnderTest
							.readFromFile()
							.findFieldByName("usedField")
							.get()
							.getFieldDescription());
		}

		@Test
		void setTheFontNameForATextElementOfTheTextFieldCorrectly_ifThereIsOneSet() throws Exception {
			assertEquals(
					"Monospaced",
					unitUnderTest
							.readFromFile()
							.getDetails()
							.get(0)
							.getBands()
							.get(0)
							.getTextFields()
							.get(1)
							.getTextElement()
							.getFont()
							.getFontName());
		}

		@Test
		void setNullForTheWholeTextElementOfTheTextField_ifThereIsNoChange() throws Exception {
			assertNull(
					unitUnderTest
							.readFromFile()
							.getDetails()
							.get(0)
							.getBands()
							.get(0)
							.getTextFields()
							.get(0)
							.getTextElement());
		}

		@Test
		void setTheFontNameForATextElementOfTheStaticTextCorrectly_ifThereIsOneSet() throws Exception {
			assertEquals(
					"Monospaced",
					unitUnderTest
							.readFromFile()
							.getDetails()
							.get(0)
							.getBands()
							.get(0)
							.getStaticTexts()
							.get(1)
							.getTextElement()
							.getFont()
							.getFontName());
		}

		@Test
		void setNullForTheWholeTextElementOfTheStaticText_ifThereIsNoChange() throws Exception {
			assertNull(
					unitUnderTest
							.readFromFile()
							.getDetails()
							.get(0)
							.getBands()
							.get(0)
							.getStaticTexts()
							.get(0)
							.getTextElement());
		}

	}

}