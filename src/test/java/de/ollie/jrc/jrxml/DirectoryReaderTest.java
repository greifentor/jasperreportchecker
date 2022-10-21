package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DirectoryReaderTest {

	private static final String PATH = "src/test/resources/test-reports/usage-test";

	private DirectoryReader unitUnderTest;

	@BeforeEach
	void setUp() {
		unitUnderTest = new DirectoryReader(PATH);
	}

	@Nested
	class TestsOfMethod_readAllReports {

		@Test
		void returnsAMapContainingAReportFromThePATH() throws Exception {
			assertNotNull(unitUnderTest.readAllReports().get("SubreportUsageCommand-Main01.jrxml"));
		}

		@Test
		void returnsAMapContainingAReportFromASubdirectoryOfPATH() throws Exception {
			System.out.println(unitUnderTest.readAllReports());
			assertNotNull(unitUnderTest.readAllReports().get("subreports/SubreportUsageCommand-Subreport01.jrxml"));
		}

	}

}
