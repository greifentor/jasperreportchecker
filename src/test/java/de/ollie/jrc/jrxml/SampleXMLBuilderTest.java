package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.xml.exception.DifferentRootNamesException;
import de.ollie.jrc.xml.model.XMLNode;

@ExtendWith(MockitoExtension.class)
public class SampleXMLBuilderTest {

	@InjectMocks
	private SampleXMLBuilder unitUnderTest;

	private Field createField(String fieldDescription) {
		return new Field().setFieldDescription(fieldDescription);
	}

	private List<Field> createFields(String... fieldDescriptions) {
		return List.of(fieldDescriptions).stream().map(s -> createField(s)).collect(Collectors.toList());
	}

	private XMLNode createXMLNode(String name) {
		return new XMLNode().setName(name);
	}

	private XMLNode createXMLNode(String name, XMLNode... nodes) {
		return new XMLNode().setName(name).setNodes(List.of(nodes));
	}

	@Nested
	class buildXMLFromJasperReport_JasperReport {

		@Test
		void returnsANullValue_passingANullValue() {
			assertNull(unitUnderTest.buildXMLFromJasperReport(null));
		}

		@Test
		void returnsANullValue_passingAnEmptyJasperReport() {
			assertNull(unitUnderTest.buildXMLFromJasperReport(new JasperReport()));
		}

		@Test
		void returnsANullValue_passingAJasperReportWithAFieldWithFieldDescriptionNull() {
			assertNull(unitUnderTest.buildXMLFromJasperReport(new JasperReport().setFields(List.of(new Field()))));
		}

		@Test
		void returnsANullPointer_passingAJasperReportWithAFieldWithFieldDescriptionEmpty() {
			assertNull(unitUnderTest.buildXMLFromJasperReport(new JasperReport().setFields(List.of(createField("")))));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithAFieldIncludingAFieldDescription() {
			assertEquals(
					new XMLNode().setName("root").setNodes(List.of(createXMLNode("field"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(List.of(createField("/root/field")))));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithTwoFieldsIncludingAFieldDescription() {
			assertEquals(
					new XMLNode().setName("root").setNodes(List.of(createXMLNode("field0"), createXMLNode("field1"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(createFields("/root/field0", "/root/field1"))));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithAMoreComplexStructureOfFields() {
			assertEquals(
					new XMLNode()
							.setName("root")
							.setNodes(
									List
											.of(
													createXMLNode("field0"),
													createXMLNode("field1"),
													createXMLNode(
															"others",
															createXMLNode("other0"),
															createXMLNode("other1")))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(
													createFields(
															"/root/field0",
															"/root/field1",
															"/root/others/other0",
															"/root/others/other1"))));
		}

		@Test
		void throwsAnException_passingAJasperReportWithFieldsWithDifferentRootTagsInTheFieldDescription() {
			// Prepare
			String root0 = "root0";
			String fieldDescription1 = "/root1/field1";
			// Run & Check
			DifferentRootNamesException e = assertThrows(
					DifferentRootNamesException.class,
					() -> unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(createFields("/" + root0 + "/field0", fieldDescription1))));
			assertEquals("field description '" + fieldDescription1 + "' should start with:" + root0, e.getMessage());
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithADeepComplexStructureOfAField() {
			assertEquals(
					createXMLNode(
							"one",
							createXMLNode(
									"two",
									createXMLNode("three", createXMLNode("four")),
									createXMLNode("threeAndAHalf"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(createFields("/one/two/three/four", "/one/two/threeAndAHalf"))));
		}

	}

}