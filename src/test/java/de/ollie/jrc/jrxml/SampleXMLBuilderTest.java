package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	@InjectMocks
	private SampleXMLBuilder unitUnderTest;

	private Field createField(String fieldDescription) {
		return new Field().setFieldDescription(fieldDescription);
	}

	private XMLNode createXMLNode(String name) {
		return new XMLNode().setName(name);
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
									new JasperReport()
											.setFields(
													List
															.of(
																	createField("/root/field0"),
																	createField("/root/field1")))));
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
													createXMLNode("others")
															.setNodes(
																	List
																			.of(
																					createXMLNode("other0"),
																					createXMLNode("other1"))))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(
													List
															.of(
																	createField("/root/field0"),
																	createField("/root/field1"),
																	createField("/root/others/other0"),
																	createField("/root/others/other1")))));
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
													List
															.of(
																	createField("/" + root0 + "/field0"),
																	createField(fieldDescription1)))));
			assertEquals("field description '" + fieldDescription1 + "' should start with:" + root0, e.getMessage());
		}

	}

}