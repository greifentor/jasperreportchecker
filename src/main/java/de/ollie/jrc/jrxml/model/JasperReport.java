package de.ollie.jrc.jrxml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Generated
@XmlRootElement(namespace = "")
@XmlAccessorType(XmlAccessType.FIELD) // That's vital to use the other annotations.
public class JasperReport {

	@XmlElement(name = "columnFooter")
	private List<ColumnFooter> columnFooter = new ArrayList<>();
	@XmlElement(name = "columnHeader")
	private List<ColumnHeader> columnHeader = new ArrayList<>();
	@XmlElement(name = "detail")
	private List<Detail> details = new ArrayList<>();
	@XmlElement(name = "field")
	private List<Field> fields = new ArrayList<>();
	@XmlElement(name = "lastPageFooter")
	private List<LastPageFooter> lastPageFooter = new ArrayList<>();
	@XmlElement(name = "pageFooter")
	private List<PageFooter> pageFooter = new ArrayList<>();
	@XmlElement(name = "pageHeader")
	private List<PageHeader> pageHeader = new ArrayList<>();
	@XmlElement(name = "parameter")
	private List<Parameter> parameters = new ArrayList<>();
	@XmlElement(name = "summary")
	private List<Summary> summary = new ArrayList<>();
	@XmlElement(name = "title")
	private List<Title> title = new ArrayList<>();
	@XmlElement(name = "variable")
	private List<Variable> variables = new ArrayList<>();

	public Optional<Field> findFieldByName(String name) {
		return fields.stream().filter(field -> field.getName().equals(name)).findFirst();
	}

	public List<String> findAllCalledReportsFrom() {
		List<String> subreportNames = new ArrayList<>();
		subreportNames.addAll(findAllCalledReportsFromBands(getColumnFooter()));
		subreportNames.addAll(findAllCalledReportsFromBands(getColumnHeader()));
		subreportNames.addAll(findAllCalledReportsFromBands(getDetails()));
		subreportNames.addAll(findAllCalledReportsFromBands(getLastPageFooter()));
		subreportNames.addAll(findAllCalledReportsFromBands(getPageFooter()));
		subreportNames.addAll(findAllCalledReportsFromBands(getPageHeader()));
		subreportNames.addAll(findAllCalledReportsFromBands(getSummary()));
		subreportNames.addAll(findAllCalledReportsFromBands(getTitle()));
		return subreportNames;
	}

	private List<String> findAllCalledReportsFromBands(List<? extends BandProvider> bandProviders) {
		return bandProviders
				.stream()
				.flatMap(bandProvider -> bandProvider.getBands().stream())
				.flatMap(subreportProvider -> subreportProvider.getSubreports().stream())
				.map(Subreport::getPlainCalledFileName)
				.collect(Collectors.toList());
	}

}