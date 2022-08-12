package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileNameProviderTest {

	private static final String FILE_NAME_0 = "fileName0";
	private static final String FILE_NAME_1 = "fileName1";

	@InjectMocks
	private FileNameProvider unitUnderTest;

	@Test
	void returnsAnEmptyList_passingNoArgumentsAsNull() throws Exception {
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(null).isEmpty());
	}

	@Test
	void returnsAnEmptyList_passingNoArguments() throws Exception {
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(new String[0]).isEmpty());
	}

	@Test
	void returnsAnEmptyList_passingNoFileArguments() throws Exception {
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(new String[] { "a string" }).isEmpty());
	}

	@Test
	void returnsAListWithAFileName_passingASingleFileName() throws Exception {
		assertEquals(1, unitUnderTest.getFileNamesFromCommandLineParameters(new String[] { "-f", FILE_NAME_0 }).size());
	}

	@Test
	void returnsAListWithAFileName_passingThePassedFileName() throws Exception {
		assertEquals(
				FILE_NAME_0,
				unitUnderTest.getFileNamesFromCommandLineParameters(new String[] { "-f", FILE_NAME_0 }).get(0));
	}

	@Test
	void returnsAListWithTheFileNames_passingMoreThanOneFileName() throws Exception {
		assertEquals(
				2,
				unitUnderTest
						.getFileNamesFromCommandLineParameters(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 })
						.size());
	}

	@Test
	void returnsAListWithTheTwoFileName_passingTwoFileName() throws Exception {
		assertEquals(
				FILE_NAME_0,
				unitUnderTest
						.getFileNamesFromCommandLineParameters(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 })
						.get(0));
		assertEquals(
				FILE_NAME_1,
				unitUnderTest
						.getFileNamesFromCommandLineParameters(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 })
						.get(1));
	}

}
