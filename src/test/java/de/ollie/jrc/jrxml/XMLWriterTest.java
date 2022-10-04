package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.xml.model.XMLNode;

@ExtendWith(MockitoExtension.class)
public class XMLWriterTest {

	@Mock
	private PrintStream printStream;

	@InjectMocks
	private XMLWriter unitUnderTest;

	private XMLNode createNode(String name) {
		return new XMLNode().setName(name);
	}

	@Nested
	class TestsOfMethod_write_XMLNode_PrintStream {

		@Test
		void throwsAnException_passingANullValueAsPrintStream() {
			XMLNode xmlNode = new XMLNode();
			assertThrows(NullPointerException.class, () -> unitUnderTest.write(xmlNode, null));
		}

		@Test
		void writesNothing_passingANullValueAsXMLNode() {
			// Run
			unitUnderTest.write(null, printStream);
			// Check
			verifyNoInteractions(printStream);
		}

		@Test
		void printsACorrectXMLString_passingASingleXMLNodeWithName() {
			// Prepare
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			// Run
			unitUnderTest.write(createNode("node"), ps);
			// Check
			assertEquals("<node></node>", os.toString());
		}

		@Test
		void printsACorrectXMLString_passingAComplexXMLNodeWithName() {
			// Prepare
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(os);
			// Run
			unitUnderTest
					.write(
							createNode("root")
									.setNodes(
											List
													.of(
															createNode("name"),
															createNode("address")
																	.setNodes(
																			List
																					.of(
																							createNode("street"),
																							createNode("city"))))),
							ps);
			// Check
			assertEquals("<root><name></name><address><street></street><city></city></address></root>", os.toString());
		}

	}

}
