package de.ollie.jrc.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.ollie.jrc.JasperReportsBundleReader;
import de.ollie.jrc.jrxml.model.JasperReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * Another approach to have a human readable code (which is also thread safe).
 * 
 * @author ollie (29.11.2022)
 *
 */
public class FindOrphansCommand {

	@AllArgsConstructor
	@Getter
	private static class OrphanSubReportData {

		private String fileName;
		private String reportName;

	}

	private final String path;

	private Map<String, JasperReport> jasperReports;
	private List<String> jasperReportFileNames;
	private List<OrphanSubReportData> identifiedOrphanSubReportData = new ArrayList<>();
	private int numberOfScannedReports = 0;
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
	}

	private boolean noOrphanSubReportsFound() {
		return identifiedOrphanSubReportData.isEmpty();
	}

	private void createNoOrphansFoundReport() {
		report = numberOfScannedReports + " Report Files scanned.\nNo Orphans Sub Reports Found!!!";
	}

	private void createOrphanSubReportsReport() {
		report = "TODO";
	}

	public String getReport() {
		return report;
	}
}
