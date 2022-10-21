package de.ollie.jrc.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

// May incapsulate a real logger in the future ;) 
@RequiredArgsConstructor
public class Logger {

	private final String name;

	public static Logger getLogger(String name) {
		return new Logger(name);
	}

	public void info(String message) {
		String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD HH:mm:SS.N"));
		System.out.println(String.format("%20s - %40s - INFO  - %s", time, name, message));
	}

}