package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.xml.model.XMLNode;

@ExtendWith(MockitoExtension.class)
public class NodeSampleDataGeneratorTest {

	private static final String NODE_NAME = "nodename";

	@InjectMocks
	private NodeSampleDataGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_getSampleDateFor_XMLNode {

		@Test
		void throwsAnException_passingANullValue() {
			assertThrows(NullPointerException.class, () -> unitUnderTest.getSampleDateFor(null));
		}

		@Test
		void returnsANullValue_passingANodeWithNoTypeInformation() {
			assertNull(unitUnderTest.getSampleDateFor(new XMLNode().setName(NODE_NAME)));
		}

		@Test
		void returnsABooleanTRUE_passingANodeWithTypeBoolean() {
			assertEquals(
					Boolean.TRUE,
					unitUnderTest
							.getSampleDateFor(
									new XMLNode()
											.setName(NODE_NAME)
											.setClassName(NodeSampleDataGenerator.BOOLEAN_CLASS_NAME)));
		}

		@Test
		void returnsAUpperCaseNodeName_passingANodeWithTypeString() {
			assertEquals(
					NODE_NAME.toUpperCase(),
					unitUnderTest
							.getSampleDateFor(
									new XMLNode()
											.setName(NODE_NAME)
											.setClassName(NodeSampleDataGenerator.STRING_CLASS_NAME)));
		}

		@Test
		void returnsASpecialUnknownString_passingANodeWithAnUnknownTypeString() {
			assertEquals(
					NodeSampleDataGenerator.UNKNOWN,
					unitUnderTest.getSampleDateFor(new XMLNode().setName(NODE_NAME).setClassName(";op")));
		}

	}

}
