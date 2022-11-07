package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.jrxml.model.JasperReport;

@ExtendWith(MockitoExtension.class)
public class FontListerTest {

	private static final String XML_FILE_NAME = "src/test/resources/test-reports/test-report.jrxml";

	@InjectMocks
	private FontLister unitUnderTest;

	@Nested
	class TestsOfMethod_getUsedFontNames {

		@Test
		void returnsAnEmptySet_passingANullValue() {
			assertTrue(unitUnderTest.getUsedFontNames(null).isEmpty());
		}

		@Test
		void returnsAnEmptySet_passingAEmptyJasperReport() {
			assertTrue(unitUnderTest.getUsedFontNames(new JasperReport()).isEmpty());
		}

		@Test
		void returnsSetWithTheFontNames_passingAJasperReportWithContent() throws Exception {
			JasperReport jasperReport = new FileReader(XML_FILE_NAME).readFromFile();
			assertFalse(unitUnderTest.getUsedFontNames(jasperReport).isEmpty());
		}

		@Test
		void returnsSetWithTheCorrectSize_passingAJasperReportWithContent() throws Exception {
			JasperReport jasperReport = new FileReader(XML_FILE_NAME).readFromFile();
			assertEquals(2, unitUnderTest.getUsedFontNames(jasperReport).size());
		}

		@Test
		void returnsASetWithTheFontNamesContainingDEFAULT_passingAJasperReportWithContentWithDefaultFont()
				throws Exception {
			JasperReport jasperReport = new FileReader(XML_FILE_NAME).readFromFile();
			assertTrue(unitUnderTest.getUsedFontNames(jasperReport).contains(FontLister.DEFAULT));
		}

		@Test
		void returnsASetWithTheFontNamesContainingTheFontName_passingAJasperReportWithContentWithFont()
				throws Exception {
			JasperReport jasperReport = new FileReader(XML_FILE_NAME).readFromFile();
			assertTrue(unitUnderTest.getUsedFontNames(jasperReport).contains("Monospaced"));
		}

	}

}
