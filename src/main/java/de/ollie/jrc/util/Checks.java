package de.ollie.jrc.util;

public class Checks {

	public static void ensure(boolean condition, String message) throws IllegalArgumentException {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T extends Exception> void ensure(boolean condition, T exception) throws T {
		if (exception == null) {
			throw new NullPointerException("exception cannot be null.");
		}
		if (!condition) {
			throw exception;
		}
	}

}