package de.ollie.jrc.jrxml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import de.ollie.jrc.util.FileNames;
import lombok.Data;
import lombok.Generated;

@Data
@Generated
@XmlAccessorType(XmlAccessType.FIELD)
public class Subreport {

	@XmlElement(name = "dataSourceExpression")
	private String dataSourceExpression;
	@XmlElement(name = "subreportExpression")
	private String subreportExpression;
	@XmlElement(name = "subreportParameter")
	private List<SubreportParameter> subreportParameters = new ArrayList<>();

	public String getCalledFileName(String subreportDirectory) {
		return FileNames
				.normalize(getSubreportExpression())
				.replace(" ", "")
				.replace("$P{SUBREPORT_DIR}+\"", subreportDirectory + (!subreportDirectory.endsWith("/") ? "/" : ""))
				.replace(".jasper\"", ".jrxml");
	}

	public String getPlainCalledFileName() {
		return FileNames
				.normalize(getSubreportExpression())
				.replace(" ", "")
				.replace("$P{SUBREPORT_DIR}+\"", "")
				.replace(".jasper\"", ".jrxml");
	}

}
