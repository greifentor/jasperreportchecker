package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class JRCTest {

	private static final String XML_FILE_NAME = "src/test/resources/test-report/test-report.jrxml";

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
	void noExceptionForEmptyParameters() {
		try {
			JRC.main(new String[0]);
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	@Test
	void noExceptionForHelpParameters() {
		try {
			JRC.main(new String[] { "help" });
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

	@Test
	void noExceptionForNoFileParameters() {
		try {
			JRC.main(new String[] { "check", "aString" });
		} catch (Exception e) {
			fail("should not throw an exception, but was: " + e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}

}
