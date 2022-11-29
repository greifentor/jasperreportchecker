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
				"5 Report Files scanned.\n\nOrphans:\n- subreports/SubreportFindOrphansCommand-UnusedSubSubreport.jrxml";
		String path = "src/text/resources/test-reports/usage-test";
		// Run & Check
		assertEquals(expected, new FindOrphansCommand(path).getReport());
	}

}