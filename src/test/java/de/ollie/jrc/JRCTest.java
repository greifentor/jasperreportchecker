package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class JRCTest {

	private static final String XML_FILE_NAME = "src/test/resources/test-report/test-report.jrxml";

	@Test
	void runsWithoutException() {
		try {
			JRC.main(new String[] { "-f", XML_FILE_NAME });
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
	void noExceptionForNoFileParameters() {
		try {
			JRC.main(new String[] { "aString" });
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

}
