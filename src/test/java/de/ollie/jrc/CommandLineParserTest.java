package de.ollie.jrc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.CommandLineParser.CommandLineData;

@ExtendWith(MockitoExtension.class)
class CommandLineParserTest {

	private static final String DIRECTORY = "directory";
	private static final String FILE_NAME = "fileName";
	private static final String FILE_NAME_PATTERN = "fileNamePattern";
	private static final boolean LIST_OUTPUT = true;
	private static final String OUTPUT_FILE_NAME = "outputFileName";
	private static final String SUBREPORT_DIRECTORY = "subreportDirectory";
	private static final boolean SUPPRESS_MESSAGE_FOR_FILE_HAVING_NO_UNUSED_OBJECTS = true;

	@InjectMocks
	private CommandLineParser unitUnderTest;

	@Nested
	class TestsOfMethod_parse_ArrayOfStrings {

		@Test
		void returnsADefaultCommandLineDataObject_passingACommandLineWithNoArguments() throws Exception {
			assertEquals(new CommandLineData(), new CommandLineParser(new String[0]).parse());
		}

		@Test
		void returnsAFullyLoadedCommandLineDataObject_passingACommandLineWithAllArguments() throws Exception {
			CommandLineData expected = new CommandLineData()
					.setDirectory(DIRECTORY)
					.setFileName(FILE_NAME)
					.setFileNamePattern(FILE_NAME_PATTERN)
					.setListOutput(LIST_OUTPUT)
					.setOutputFileName(OUTPUT_FILE_NAME)
					.setSubreportDirectory(SUBREPORT_DIRECTORY)
					.setSuppressMessageForFileHavingNoUnusedObjects(SUPPRESS_MESSAGE_FOR_FILE_HAVING_NO_UNUSED_OBJECTS);
			assertEquals(
					expected,
					new CommandLineParser(
							new String[] {
									"-d",
									DIRECTORY,
									"-f",
									FILE_NAME,
									"-p",
									FILE_NAME_PATTERN,
									"-l",
									"-o",
									OUTPUT_FILE_NAME,
									"-sd",
									SUBREPORT_DIRECTORY,
									"-snfm" }).parse());
		}


	}

}
