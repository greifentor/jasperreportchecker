package de.ollie.jrc.jrxml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.util.FileNames;

public class DependencyDiagramBuilder {

	private String fileName;
	private PrintStream out;
	private Map<String, JasperReport> reports;
	private Map<String, List<String>> dependencyMapping;

	public DependencyDiagramBuilder(String fileName, Map<String, JasperReport> reports, PrintStream out) {
		this.fileName = FileNames.normalize(fileName);
		this.out = out;
		this.reports = reports;
		if (this.fileName.startsWith("/")) {
			this.fileName = this.fileName.substring(1);
		}
	}

	public synchronized void build() {
		writeUMLStart();
		initializeDependencyMapping();
		writeDependenciesAsUMLComponentDiagramToOut();
		writeUMLEnd();
	}

	private void writeUMLStart() {
		out.println("@startuml");
		out.println();
		out.println("[" + fileName + "]");
	}

	private void initializeDependencyMapping() {
		createANewDependencyMap();
		forEachReportDo((dependencyName, report) -> {
			List<String> calledReports = report.findAllCalledReportsFrom();
			setDependencyForEachCalledReportForPassedName(calledReports, dependencyName);
		});
	}

	private void createANewDependencyMap() {
		dependencyMapping = new HashMap<>();
	}

	private void forEachReportDo(BiConsumer<String, JasperReport> reportConsumer) {
		reports.keySet().forEach(key -> reportConsumer.accept(key, reports.get(key)));
	}

	private void setDependencyForEachCalledReportForPassedName(List<String> calledReports, String dependencyName) {
		calledReports.forEach(reportName -> {
			List<String> l = dependencyMapping.getOrDefault(reportName, new ArrayList<>());
			l.add(dependencyName);
			dependencyMapping.put(reportName, l);
		});
	}

	// OLI: That's for the whole directory --- too much ;)
//	private void writeDependenciesAsUMLComponentDiagramToOut() {
//		for (String reportName : dependencyMapping.keySet()) {
//			dependencyMapping
//					.getOrDefault(reportName, new ArrayList<>())
//					.forEach(
//							dependentReportName -> out
//									.println("[" + reportName + "] <-- [" + dependentReportName + "]"));
//		}
//	}

	private void writeDependenciesAsUMLComponentDiagramToOut() {
		writeDependenciesAsUMLComponentDiagramToOut(fileName);
	}

	private void writeDependenciesAsUMLComponentDiagramToOut(String reportName) {
		dependencyMapping.getOrDefault(reportName, new ArrayList<>()).forEach(dependentReportName -> {
			out.println("[" + reportName + "] <-- [" + dependentReportName + "]");
			writeDependenciesAsUMLComponentDiagramToOut(dependentReportName);
		});
	}

	private void writeUMLEnd() {
		out.println();
		out.println("@enduml");
	}

}
