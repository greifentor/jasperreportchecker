package de.ollie.jrc.jrxml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import de.ollie.jrc.DirectoryScanner;
import de.ollie.jrc.jrxml.model.JasperReport;
import de.ollie.jrc.util.FileNames;

public class DirectoryReader {

	private String dir;
	private String dirPrefix;

	public DirectoryReader(String dir) {
		this.dir = dir;
		this.dirPrefix = FileNames.normalize(new File(dir).getAbsolutePath());
		this.dirPrefix = this.dirPrefix + (!this.dirPrefix.endsWith("/") ? "/" : "");
	}

	public Map<String, JasperReport> readAllReports() throws IOException, JAXBException {
		Map<String, JasperReport> map = new HashMap<>();
		for (File file : new DirectoryScanner().scan(new ArrayList<>(), dir, "*.jrxml")) {
			map
					.put(
							FileNames.normalize(file.getAbsolutePath()).replace(dirPrefix, ""),
							new FileReader(file.getAbsolutePath()).readFromFile());
		}
		return map;
	}

}
