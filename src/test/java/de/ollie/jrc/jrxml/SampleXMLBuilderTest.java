package de.ollie.jrc.jrxml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.jrc.jrxml.model.Field;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.xml.model.XMLNode;

@ExtendWith(MockitoExtension.class)
public class SampleXMLBuilderTest {

	@InjectMocks
	private SampleXMLBuilder unitUnderTest;

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
			assertNull(
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport().setFields(List.of(new Field().setFieldDescription("")))));
		}

		@Test
		void returnsACorrectXMLNode_passingAJasperReportWithAFieldIncludingAFieldDescription() {
			assertEquals(new XMLNode().setName("root").setNodes(List.of(new XMLNode().setName("field"))),
					unitUnderTest
							.buildXMLFromJasperReport(
									new JasperReport()
											.setFields(List.of(new Field().setFieldDescription("/root/field")))));
		}

	}

}