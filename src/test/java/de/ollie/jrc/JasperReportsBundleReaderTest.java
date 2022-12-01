package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.jrxml.model.JasperReport;

@ExtendWith(MockitoExtension.class)
public class JasperReportsBundleReaderTest {

	@InjectMocks
	private JasperReportsBundleReader unitUnderTest;

	@Nested
	class TestsOfMethod_findAllJasperReportsInPathByRelativeFileName_String {

		@Test
		void returnsAnEmptyMap_passingAPathWithNoReportFiles() {
			assertTrue(unitUnderTest.findAllJasperReportsInPathByRelativeFileName("src/main/java").isEmpty());
		}

		@Test
		void returnsAMapWithCorrectReportCount_passingAPathWithReportFiles() {
			assertEquals(
					6,
					unitUnderTest
							.findAllJasperReportsInPathByRelativeFileName("src/test/resources/test-reports/usage-test")
							.size());
		}

		@Test
		void returnsAMapWithCorrectReport_passingAPathWithReportFiles() {
			Map<String, JasperReport> reports = unitUnderTest
					.findAllJasperReportsInPathByRelativeFileName("src/test/resources/test-reports/usage-test");
			assertEquals(
					"subreport01",
					reports.get("subsubreports/SubreportUsageCommand-SubSubreport.jrxml").getName());
		}

	}

}
