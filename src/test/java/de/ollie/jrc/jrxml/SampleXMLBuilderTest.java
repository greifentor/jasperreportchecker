package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

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

	private static final String CLASS_NAME = "class.Name";
	private static final String SUBREPORT_DIR = "subreport/dir";

	@InjectMocks
	private SampleXMLBuilder unitUnderTest;

	private Field createField(String fieldDescription) {
		return new Field().setFieldDescription(fieldDescription);
	}

	private List<Field> createFields(String... fieldDescriptions) {
		List<Field> fields = new ArrayList<>();
		for (int i = 0, leni = fieldDescriptions.length; i < leni; i = i + 2) {
			fields.add(new Field().setName(fieldDescriptions[i]).setCls(fieldDescriptions[i + 1]));
		}
		return fields;
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
			assertNull(unitUnderTest.buildXMLFromJasperReport(null, SUBREPORT_DIR));
		}

		@Test
		void returnsANullValue_passingAnEmptyJasperReport() {
			assertEquals(new XMLNode(), unitUnderTest.buildXMLFromJasperReport(new JasperReport(), SUBREPORT_DIR));
		}

		@Test
		void returnsFieldName_passingAJasperReportWithAFieldWithFieldDescriptionNull() {
			String name = "name";
			assertEquals(
					new XMLNode().setName(name),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(List.of(new Field().setName(name))),
									SUBREPORT_DIR));
		}

		@Test
		void returnsANullPointer_passingAJasperReportWithAFieldWithFieldDescriptionEmpty() {
			String name = "name";
			assertEquals(
					new XMLNode().setName(name),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(List.of(createField("").setName(name))),
									SUBREPORT_DIR));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithAFieldIncludingAFieldDescription() {
			assertEquals(
					new XMLNode().setName("root").setNodes(List.of(createXMLNode("field"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(List.of(createField("/root/field"))),
									SUBREPORT_DIR));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithTwoFieldsIncludingAFieldDescription() {
			assertEquals(
					new XMLNode().setName("root").setNodes(List.of(createXMLNode("field0"), createXMLNode("field1"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(createFields("/root/field0", null, "/root/field1", null)),
									SUBREPORT_DIR));
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
															null,
															"/root/field1",
															null,
															"/root/others/other0",
															null,
															"/root/others/other1",
															null)),
									SUBREPORT_DIR));
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
											.setFields(
													createFields(
															"/" + root0 + "/field0",
															null,
															fieldDescription1,
															null)),
									SUBREPORT_DIR));
			assertEquals("field description '" + fieldDescription1 + "' should start with:" + root0, e.getMessage());
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithADeepComplexStructureOfAField() {
			assertEquals(
					createXMLNode(
							"one",
							createXMLNode(
									"two",
									createXMLNode("three", createXMLNode("four").setClassName(CLASS_NAME)),
									createXMLNode("threeAndAHalf").setClassName(CLASS_NAME))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(
													createFields(
															"/one/two/three/four",
															CLASS_NAME,
															"/one/two/threeAndAHalf",
															CLASS_NAME)),
									SUBREPORT_DIR));
		}

	}

}