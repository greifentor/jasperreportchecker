package de.ollie.jrc.jrxml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import lombok.Data;
import lombok.Generated;

@Data
@Generated
@XmlAccessorType(XmlAccessType.FIELD)
public class TextField implements ReportAndTextElementProvider {

	private ReportElement reportElement;
	private TextElement textElement;

}
