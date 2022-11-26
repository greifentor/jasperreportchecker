package de.ollie.jrc.gui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.util.FileUtil;

@ExtendWith(MockitoExtension.class)
public class CorrectFileSelectionCheckerTest {

	public static final String INVALID_FILE_NAME = "invalid.something";
	public static final String PATTERN = "pattern";
	public static final String VALID_FILE_NAME = "valid.jrxml";

	@Mock
	private FileUtil fileUtil;

	@InjectMocks
	private CorrectFileSelectionChecker unitUnderTest;

	@BeforeEach
	void setUp() {
		CorrectFileSelectionChecker.fileUtil = fileUtil;
	}

	@Nested
	class TestsOfMethod_isValid_String_String {

		@Test
		void returnsFalse_passingFileNameAndPatternAsNull() {
			assertFalse(unitUnderTest.isValid(null, null));
		}

		@Test
		void returnsFalse_passingFileNameAndPatternAsAnEmptyString() {
			assertFalse(unitUnderTest.isValid("", ""));
		}

		@Nested
		class FileNameIsSetAndNotADirectory {

			@Test
			void returnTrue_passingAValidFileNameButPatternAsAnEmptyString() {
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(false);
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				assertTrue(unitUnderTest.isValid(VALID_FILE_NAME, ""));
			}

			@Test
			void returnsFalse_passingAnInvalidFileNameAndPatternAsAnEmptyString() {
				when(fileUtil.isExisting(INVALID_FILE_NAME)).thenReturn(false);
				assertFalse(unitUnderTest.isValid(INVALID_FILE_NAME, ""));
			}

			@Test
			void returnsTrue_passingAValidFileNameButPatternAsNull() {
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(false);
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				assertTrue(unitUnderTest.isValid(VALID_FILE_NAME, null));
			}

			@Test
			void returnsFalse_passingAnInvalidFileNameAndPatternAsNull() {
				when(fileUtil.isExisting(INVALID_FILE_NAME)).thenReturn(false);
				assertFalse(unitUnderTest.isValid(INVALID_FILE_NAME, null));
			}

			@Test
			void returnsFalse_passingAnValidFileNameAndAPattern() {
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(false);
				assertFalse(unitUnderTest.isValid(VALID_FILE_NAME, PATTERN));
			}
		}

		@Nested
		class FileNameIsSetAndADirectory {

			@Test
			void returnsTrue_passingAnValidDirectoryNameAndAPattern() {
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(true);
				assertTrue(unitUnderTest.isValid(VALID_FILE_NAME, PATTERN));
			}

			@Test
			void returnsFalse_passingAnValidDirectoryNameAndPatternIsAnEmptyString() {
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(true);
				assertFalse(unitUnderTest.isValid(VALID_FILE_NAME, ""));
			}

			@Test
			void returnsFalse_passingAnValidDirectoryNameAndPatternIsNull() {
				when(fileUtil.isExisting(VALID_FILE_NAME)).thenReturn(true);
				when(fileUtil.isADirectory(VALID_FILE_NAME)).thenReturn(true);
				assertFalse(unitUnderTest.isValid(VALID_FILE_NAME, null));
			}

		}

	}
}
