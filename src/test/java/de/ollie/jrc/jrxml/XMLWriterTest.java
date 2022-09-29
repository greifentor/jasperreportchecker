package de.ollie.jrc.jrxml;

import java.io.PrintStream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class XMLWriterTest {

	@Mock
	private PrintStream printStream;

	@InjectMocks
	private XMLWriter unitUnderTest;

}
