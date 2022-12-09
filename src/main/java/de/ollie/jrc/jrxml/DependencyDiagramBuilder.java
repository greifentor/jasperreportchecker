package de.ollie.jrc.jrxml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.util.FileNames;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DependencyDiagramBuilder {

	@AllArgsConstructor
	@Getter
	private class DependencyInformation {

		private String dependentReportName;
		private boolean root;
	}

	private String fileName;
	private PrintStream out;
	private Map<String, JasperReport> reports;
	private Map<String, List<DependencyInformation>> dependencyMapping;
	private StringBuilder output = new StringBuilder();

	public DependencyDiagramBuilder(String fileName, Map<String, JasperReport> reports, PrintStream out) {
		this.fileName = FileNames.normalize(fileName);
		this.out = out;
		this.reports = reports;
		if (this.fileName.startsWith("/")) {
			this.fileName = this.fileName.substring(1);
		}
	}

	public synchronized String build() {
		writeUMLStart();
		initializeDependencyMapping();
		writeRootComponentDefinitions();
		writeDependenciesAsUMLComponentDiagramToOut();
		writeUMLEnd();
		return output.toString();
	}

	private void writeUMLStart() {
		String start = "@startuml\n\n[" + fileName + "]\n";
		out.print(start);
		output.append(start);
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
			List<DependencyInformation> l = dependencyMapping.getOrDefault(reportName, new ArrayList<>());
			l
					.add(
							new DependencyInformation(
									dependencyName,
									Optional
											.ofNullable(reports.get(dependencyName))
											.map(this::isRootReport)
											.orElse(false)));
			dependencyMapping.put(reportName, l);
		});
	}

	private boolean isRootReport(JasperReport report) {
		return report
				.findPropertyByName("com.jaspersoft.studio.report.description")
				.filter(property -> property.getValue() != null)
				.map(property -> property.getValue().contains("(ROOT)"))
				.orElse(false);
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

	private void writeRootComponentDefinitions() {
		writeRootComponentDefinitions(fileName, new HashSet<>());
	}

	private void writeRootComponentDefinitions(String reportName, Set<String> alreadySetRoots) {
		dependencyMapping.getOrDefault(reportName, new ArrayList<>()).forEach(dependencyInformation -> {
			String dependencyReportName = dependencyInformation.getDependentReportName();
			if (dependencyInformation.isRoot() && !alreadySetRoots.contains(dependencyReportName)) {
				String s = "Component \"" + dependencyReportName + "\" <<ROOT>>\n";
				out.print(s);
				output.append(s);
				alreadySetRoots.add(dependencyReportName);
			}
			writeRootComponentDefinitions(dependencyReportName, alreadySetRoots);
		});
	}

	private void writeDependenciesAsUMLComponentDiagramToOut() {
		writeDependenciesAsUMLComponentDiagramToOut(fileName);
	}

	private void writeDependenciesAsUMLComponentDiagramToOut(String reportName) {
		dependencyMapping.getOrDefault(reportName, new ArrayList<>()).forEach(dependencyInformation -> {
			String s = "[" + reportName + "] <-- [" + dependencyInformation.getDependentReportName() + "]\n";
			out.print(s);
			output.append(s);
			writeDependenciesAsUMLComponentDiagramToOut(dependencyInformation.getDependentReportName());
		});
	}

	private void writeUMLEnd() {
		String s = "\n@enduml\n";
		out.print(s);
		output.append(s);
	}

}
