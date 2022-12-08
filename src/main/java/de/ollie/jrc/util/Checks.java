package de.ollie.jrc.util;

import java.util.function.Supplier;

public class Checks {

	Checks() {
		throw new UnsupportedOperationException();
	}

	public static void ensure(boolean condition, String message) throws IllegalArgumentException {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	public static <T extends Exception> void ensure(boolean condition, Supplier<T> exceptionSupplier) throws T {
		if (exceptionSupplier == null) {
			throw new NullPointerException("exception cannot be null.");
		}
		if (!condition) {
			throw exceptionSupplier.get();
		}
	}

}