package de.ollie.jrc.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileUtilTest {

	private static final String NAME = "name";

	@InjectMocks
	private FileUtil unitUnderTest;

	@Nested
	class TestsOfMethod_idADirectory_String {

		@Test
		void returnsTrue_passingADirectoryName(@TempDir Path tempDir) throws Exception {
			// Prepare
			Path path = Path.of(tempDir.toString(), NAME);
			Files.createDirectory(path);
			// Run & Check
			assertTrue(unitUnderTest.isADirectory(path.toString()));
		}

		@Test
		void returnsFalse_passingAFileName(@TempDir Path tempDir) throws Exception {
			// Prepare
			Path path = Path.of(tempDir.toString(), NAME);
			Files.createFile(path);
			// Run & Check
			assertFalse(unitUnderTest.isADirectory(path.toString()));
		}

		@Test
		void returnsFalse_passingANotExistingFileName(@TempDir Path tempDir) throws Exception {
			assertFalse(unitUnderTest.isADirectory(Path.of(tempDir.toString(), NAME).toString()));
		}

	}

	@Nested
	class TestsOfMethod_idExisting_String {

		@Test
		void returnsTrue_passingNameOfAnExistingFile(@TempDir Path tempDir) throws Exception {
			// Prepare
			Path path = Path.of(tempDir.toString(), NAME);
			Files.createFile(path);
			// Run & Check
			assertTrue(unitUnderTest.isExisting(path.toString()));
		}

		@Test
		void returnsFalse_passingNameOfAnUnexistingFile(@TempDir Path tempDir) throws Exception {
			Path path = Path.of(tempDir.toString(), NAME);
			assertFalse(unitUnderTest.isExisting(path.toString()));
		}
	}

}
