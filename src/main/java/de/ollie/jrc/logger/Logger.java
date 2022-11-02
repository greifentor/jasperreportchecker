package de.ollie.jrc.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

// May incapsulate a real logger in the future ;) 
@RequiredArgsConstructor
public class Logger {

	public enum LogLevel {
		ERROR,
		INFO;
	}

	private final String name;

	public static Logger getLogger(String name) {
		return new Logger(name);
	}

	public void error(String message, Throwable throwable) {
		writeMessage(LogLevel.ERROR, message);
		if (throwable != null) {
			throwable.printStackTrace();
		}
	}

	private void writeMessage(LogLevel logLevel, String message) {
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.N"));
		time = (time.length() > 25 ? time.substring(0, 25) : time);
		String nameCut = (name.length() > 25 ? name.substring(0, 25) : name);
		System.out.println(String.format("%25s - %40s - %5s - %s", time, nameCut, logLevel.name(), message));
	}

	public void info(String message) {
		writeMessage(LogLevel.INFO, message);
	}

}