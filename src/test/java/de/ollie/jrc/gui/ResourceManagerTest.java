package de.ollie.jrc.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.gui.ResourceManager.Localization;

@ExtendWith(MockitoExtension.class)
class ResourceManagerTest {

	private static final String EXISTING_RESOURCE_ID = "language.name";

	private ResourceManager unitUnderTest;

	@BeforeEach
	void setUp() {
		unitUnderTest = new ResourceManager();
	}

	@Nested
	class TestsOfMethod_getString_Localization_String {

		@Test
		void throwsAnException_passingLocalizationAsNull() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getString(null, EXISTING_RESOURCE_ID));
		}

		@Test
		void throwsAnException_passingResourceIdAsNull() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.getString(Localization.DE, null));
		}

		@Test
		void returnsTheResourceId_passingAnUnexistingResourceId() {
			String resourceId = ";op";
			assertEquals(resourceId, unitUnderTest.getString(Localization.DE, resourceId));
		}

		@Test
		void returnsTheCorrectString_passingAnExistingResourceId() {
			assertEquals("Deutsch", unitUnderTest.getString(Localization.DE, EXISTING_RESOURCE_ID));
		}

	}

}
