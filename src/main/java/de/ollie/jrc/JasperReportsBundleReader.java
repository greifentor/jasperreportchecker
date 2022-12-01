package de.ollie.jrc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import de.ollie.jrc.jrxml.FileReader;
import de.ollie.jrc.jrxml.model.JasperReport;

public class JasperReportsBundleReader {

	public Map<String, JasperReport> findAllJasperReportsInPathByRelativeFileName(String path) {
		Map<String, JasperReport> result = new HashMap<>();
		String absolutePathName = backSlashesToSlashes(new File(path).getAbsolutePath());
		for (String fileName : readJasperReportFileNames(path)) {
			try {
				JasperReport jasperReport = new FileReader(fileName).readFromFile();
				result.put(cleanUpFileName(fileName, absolutePathName), jasperReport);
			} catch (IOException | JAXBException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private String backSlashesToSlashes(String s) {
		return s.replace("\\", "/");
	}

	private List<String> readJasperReportFileNames(String path) {
		return new FileNameProvider().getFileNamesFromCommandLineParameters(null, path, "*.jrxml");
	}

	private String cleanUpFileName(String fileName, String absolutePathName) {
		fileName = backSlashesToSlashes(fileName);
		fileName = fileName.replace(absolutePathName, "");
		return fileName.startsWith("/") ? fileName.substring(1) : fileName;
	}

}