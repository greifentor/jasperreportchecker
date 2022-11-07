package de.ollie.jrc.jrxml;

import java.util.HashSet;
import java.util.Set;

import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.TextElement;

public class FontLister {

	public static final String DEFAULT = "default";

	public Set<String> getUsedFontNames(JasperReport jasperReport) {
		if (jasperReport == null) {
			return new HashSet<>();
		}
		Set<String> fontNames = new HashSet<>();
		jasperReport
				.findAllBands()
				.stream()
				.flatMap(band -> band.findAllTextElements().stream())
				.map(this::getFontName)
				.forEach(fontNames::add);
		return fontNames;
	}

	private String getFontName(TextElement textElement) {
		return (textElement == null) || (textElement.getFont() == null) || (textElement.getFont().getFontName() == null)
				? DEFAULT
				: textElement.getFont().getFontName();
	}

}
