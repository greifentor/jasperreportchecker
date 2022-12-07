package de.ollie.jrc.upn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenizerTest {

	@InjectMocks
	private Tokenizer unitUnderTest;

	@Test
	void throwsAnException_passingANullValue() {
		assertThrows(NullPointerException.class, () -> unitUnderTest.getTokens(null));
	}

	@Test
	void returnsAnEmptyList_passingAnEmptyString() {
		assertTrue(unitUnderTest.getTokens("").isEmpty());
	}

	@Test
	void returnsAListWithTheToken_passingAStringWithAToken_noQuotes() {
		String expected = "Blubs";
		List<String> returned = unitUnderTest.getTokens(expected);
		assertEquals(expected, returned.get(0));
	}

	@Test
	void returnsAListWithTheToken_passingAStringWithAToken_quotes() {
		String expected = "Blubs";
		List<String> returned = unitUnderTest.getTokens("'" + expected + "'");
		assertEquals(expected, returned.get(0));
	}

	@Test
	void returnsAListWithTheToken_passingAStringWithAToken_quotesWithSpaces() {
		String expected = "Bl u bs";
		List<String> returned = unitUnderTest.getTokens("'" + expected + "'");
		assertEquals(expected, returned.get(0));
	}

	@Test
	void returnsAListWithTheToken_passingAStringWithAToken_openQuotesWithSpaces() {
		String expected = "Bl u bs";
		List<String> returned = unitUnderTest.getTokens("'" + expected);
		assertEquals(expected, returned.get(0));
	}

	@Test
	void returnsAListWithTheTokens_passingAStringWithATokens_noQuotes() {
		// Prepare
		String s0 = "Blubs";
		String s1 = "Bla";
		List<String> expected = List.of(s0, s1);
		// Run
		List<String> returned = unitUnderTest.getTokens(s0 + " " + s1);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void returnsAListWithTheTokens_passingAStringWithATokens_quotes() {
		// Prepare
		String s0 = "Blubs";
		String s1 = "Bla";
		List<String> expected = List.of(s0, s1);
		// Run
		List<String> returned = unitUnderTest.getTokens("'" + s0 + "' '" + s1 + "'");
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void returnsAListWithTheTokens_passingAStringWithATokens_mixed() {
		// Prepare
		String s0 = "Blubs";
		String s1 = "Bla";
		String s2 = "Laber";
		String s3 = "Suelz";
		List<String> expected = List.of(s0, s1, s2, s3);
		// Run
		List<String> returned = unitUnderTest.getTokens(s0 + " '" + s1 + "' " + s2 + " '" + s3 + "'");
		// Check
		assertEquals(expected, returned);
	}

}
