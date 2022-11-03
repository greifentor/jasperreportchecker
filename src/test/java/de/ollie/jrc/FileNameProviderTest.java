package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.CommandLineParser.CommandLineData;

@ExtendWith(MockitoExtension.class)
class FileNameProviderTest {

	private static final String FILE_NAME_0 = "fileName0";
	private static final String FILE_NAME_1 = "fileName1";

	@InjectMocks
	private FileNameProvider unitUnderTest;

	@Test
	void returnsAnEmptyList_passingNoArguments() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[0]).parse();
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(cmd).isEmpty());
	}

	@Test
	void returnsAnEmptyList_passingNoFileArguments() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "a string" }).parse();
		assertTrue(unitUnderTest.getFileNamesFromCommandLineParameters(cmd).isEmpty());
	}

	@Test
	void returnsAListWithAFileName_passingASingleFileName() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-f", FILE_NAME_0 }).parse();
		assertEquals(1, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
	}

	@Test
	void returnsAListWithAFileName_passingThePassedFileName() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-f", FILE_NAME_0 }).parse();
		assertEquals(FILE_NAME_0, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(0));
	}

	@Test
	void returnsAListWithTheFileNames_passingMoreThanOneFileName() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 }).parse();
		assertEquals(2, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
	}

	@Test
	void returnsAListWithTheTwoFileName_passingTwoFileName() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-f", FILE_NAME_0 + "," + FILE_NAME_1 }).parse();
		assertEquals(FILE_NAME_0, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(0));
		assertEquals(FILE_NAME_1, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).get(1));
	}

	@Test
	void returnsAListWithTheFourFileNames_passingDirectoryAndPattern() throws Exception {
		CommandLineData cmd =
				new CommandLineParser(new String[] { "-d", "src/test/resources/test-reports/", "-p", "*.jrxml" })
						.parse();
		assertEquals(13, unitUnderTest.getFileNamesFromCommandLineParameters(cmd).size());
		for (String fileName : unitUnderTest.getFileNamesFromCommandLineParameters(cmd)) {
			assertTrue(fileName.toLowerCase().endsWith(".jrxml"));
		}
	}

	@Test
	void returnsAnEmptyListWithFileNames_passingNoDirectoryButPattern() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-p", "*.jrxml" }).parse();
		assertTrue(
				unitUnderTest
						.getFileNamesFromCommandLineParameters(cmd)
						.get(0)
						.toLowerCase()
						.endsWith("fortestonly.jrxml"));
	}

	@Test
	void throwsARuntimeException_passingDirectoryWithoutAPattern() throws Exception {
		CommandLineData cmd = new CommandLineParser(new String[] { "-d", "src/test/resources/test-report/" }).parse();
		assertThrows(RuntimeException.class, () -> unitUnderTest.getFileNamesFromCommandLineParameters(cmd));
	}

}
