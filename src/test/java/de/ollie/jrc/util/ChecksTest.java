package de.ollie.jrc.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChecksTest {

	@Nested
	class TestsOIfConstructor {

		@Test
		void throwsAnException() {
			assertThrows(UnsupportedOperationException.class, () -> new Checks());
		}

	}

}
