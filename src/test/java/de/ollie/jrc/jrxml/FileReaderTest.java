package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import javax.xml.bind.UnmarshalException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileReaderTest {

	private static final String XML_FILE_NAME = "src/test/resources/test-report/test-report.jrxml";

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
					() -> new FileReader("src/test/resources/test-report/NoJasperReport.jrxml").readFromFile());
		}

	}

}