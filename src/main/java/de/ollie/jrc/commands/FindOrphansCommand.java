package de.ollie.jrc.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.ollie.jrc.JasperReportsBundleReader;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.jrxml.model.Property;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * Another approach to have a human readable code (which is also thread safe).
 * 
 * @author ollie (29.11.2022)
 *
 */
public class FindOrphansCommand {

	static final String ROOT_DOCUMENT_IDENTIFIER = "(ROOT)";

	private static final String REPORT_DESCRIPTION_PROPERTY_NAME = "com.jaspersoft.studio.report.description";

	@AllArgsConstructor
	@Getter
	private static class OrphanSubReportData {

		private String fileName;
		private String reportName;

	}

	private final String path;

	private Map<String, JasperReport> jasperReports;
	private List<OrphanSubReportData> identifiedOrphanSubReportData = new ArrayList<>();
	private String report;

	public FindOrphansCommand(String path) {
		this.path = path;
		readReportsFromPath();
		identifyOrphanSubReports();
		if (noOrphanSubReportsFound()) {
			createNoOrphansFoundReport();
		} else {
			createOrphanSubReportsReport();
		}
	}

	private void readReportsFromPath() {
		jasperReports = new JasperReportsBundleReader().findAllJasperReportsInPathByRelativeFileName(path);
	}

	private void identifyOrphanSubReports() {
		Set<String> allKeys = new HashSet<>(jasperReports.keySet());
		for (Entry<String, JasperReport> jasperReportEntry : jasperReports.entrySet()) {
			jasperReportEntry.getValue().findAllCalledReportsFrom().forEach(allKeys::remove);
		}
		allKeys
				.stream()
				.filter(s -> !isRootDocument(jasperReports.get(s)))
				.forEach(
						s -> identifiedOrphanSubReportData
								.add(new OrphanSubReportData(s, jasperReports.get(s).getName())));
	}

	private boolean isRootDocument(JasperReport jasperReport) {
		return jasperReport
				.findPropertyByName(REPORT_DESCRIPTION_PROPERTY_NAME)
				.map(Property::getValue)
				.map(value -> value.contains(ROOT_DOCUMENT_IDENTIFIER))
				.orElse(false);
	}

	private boolean noOrphanSubReportsFound() {
		return identifiedOrphanSubReportData.isEmpty();
	}

	private void createNoOrphansFoundReport() {
		report = jasperReports.size() + " Report Files scanned.\nNo Orphans Sub Reports Found!!!";
	}

	private void createOrphanSubReportsReport() {
		report = jasperReports.size() + " Report Files scanned.\n\n" //
				+ identifiedOrphanSubReportData.size()
				+ " Orphans found.\n\n" //
				+ "Orphans:\n" //
				+ identifiedOrphanSubReportData
						.stream()
						.map(orphanData -> "- " + orphanData.getFileName())
						.sorted()
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("");
	}

	public String getReport() {
		return report;
	}
}
