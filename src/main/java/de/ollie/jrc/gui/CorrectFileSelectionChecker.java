package de.ollie.jrc.gui;

import de.ollie.jrc.util.FileUtil;

public class CorrectFileSelectionChecker {

	static FileUtil fileUtil = new FileUtil();

	public boolean isValid(String fileName, String pattern) {
		if (isBothParametersAreNullOrEmpty(fileName, pattern) || !fileUtil.isExisting(fileName)) {
			return false;
		}
		return isDirectoryAndPatternSet(fileName, pattern) || isCorrectFileNameAndPatternNotSet(fileName, pattern);
	}

	private boolean isBothParametersAreNullOrEmpty(String fileName, String pattern) {
		return isNullOrEmpty(fileName) && isNullOrEmpty(pattern);
	}

	private boolean isNullOrEmpty(String s) {
		return (s == null) || s.isEmpty();
	}

	private boolean isDirectoryAndPatternSet(String fileName, String pattern) {
		return fileUtil.isADirectory(fileName) && !isNullOrEmpty(pattern);
	}

	private boolean isCorrectFileNameAndPatternNotSet(String fileName, String pattern) {
		return !fileUtil.isADirectory(fileName) && isNullOrEmpty(pattern) && fileName.toLowerCase().endsWith(".jrxml");
	}

}
