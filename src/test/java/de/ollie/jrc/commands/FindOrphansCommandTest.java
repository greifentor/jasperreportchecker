package de.ollie.jrc.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindOrphansCommandTest {

	@Test
	void returnsANoOrphansFoundReport_passingAPathWithNoReports() {
		// Prepare
		String expected = "0 Report Files scanned.\nNo Orphans Sub Reports Found!!!";
		String path = "src/main/java";
		// Run & Check
		assertEquals(expected, new FindOrphansCommand(path).getReport());
	}

	@Test
	void returnsACorrectOrphansFoundReport_passingAPathWithReports() {
		// Prepare
		String expected =
				"5 Report Files scanned.\n\n2 Orphans found.\n\nOrphans:\n- subreports/SubreportFindOrphansCommand-UnusedSubSubreport.jrxml\n- subreports/SubreportUsageCommand-Subreport02.jrxml";
		String path = "src/test/resources/test-reports/usage-test";
		// Run & Check
		assertEquals(expected, new FindOrphansCommand(path).getReport());
	}

	@Test
	void t() {
		System.out
				.println(
						new FindOrphansCommand(
								"C:\\workspace\\basisdienst-dokumente-service-templates\\src\\main\\docker\\data\\kita")
										.getReport());
	}

}