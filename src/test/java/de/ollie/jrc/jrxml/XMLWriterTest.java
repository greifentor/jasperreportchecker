package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

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
		void t() {
			unitUnderTest
					.write(
							new XMLNode()
									.setName("nodes")
									.setNodes(List.of(new XMLNode().setName("node"), new XMLNode().setName("node"))),
							System.out);
		}

	}

}
