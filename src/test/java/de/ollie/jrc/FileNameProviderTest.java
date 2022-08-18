package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.cli.CommandLine;
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
	void returnsAnEmptyList_passingNoArguments() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[0]);
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(cmd).isEmpty());
	}

	@Test
	void returnsAnEmptyList_passingNoFileArguments() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "a string" });
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(cmd).isEmpty());
	}

	@Test
	void returnsAListWithAFileName_passingASingleFileName() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-f", FILE_NAME_0 });
		assertEquals(1, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
	}

	@Test
	void returnsAListWithAFileName_passingThePassedFileName() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-f", FILE_NAME_0 });
		assertEquals(FILE_NAME_0, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(0));
	}

	@Test
	void returnsAListWithTheFileNames_passingMoreThanOneFileName() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 });
		assertEquals(2, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
	}

	@Test
	void returnsAListWithTheTwoFileName_passingTwoFileName() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 });
		assertEquals(FILE_NAME_0, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(0));
		assertEquals(FILE_NAME_1, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(1));
	}

	@Test
	void returnsAListWithTheFourFileNames_passingDirectoryAndPattern() throws Exception {
		CommandLine cmd = new CommandLineParser()
				.parse(new String[] { "-d", "src/test/resources/test-report/", "-p", "*.jrxml" });
		assertEquals(4, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
		for (String fileName : unitUnderTest.getFileNamesFromCommandLineParameters(cmd)) {
			assertTrue(fileName.toLowerCase().endsWith(".jrxml"));
		}
	}

	@Test
	void returnsAnEmptyListWithFileNames_passingNoDirectoryButPattern() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-p", "*.jrxml" });
		assertTrue(
				unitUnderTest
						.getFileNamesFromCommandLineParameters(cmd)
						.get(0)
						.toLowerCase()
						.endsWith("fortestonly.jrxml"));
	}

	@Test
	void throwsARuntimeException_passingDirectoryWithoutAPattern() throws Exception {
		CommandLine cmd = new CommandLineParser().parse(new String[] { "-d", "src/test/resources/test-report/" });
		assertThrows(RuntimeException.class, () -> unitUnderTest.getFileNamesFromCommandLineParameters(cmd));
	}

}
