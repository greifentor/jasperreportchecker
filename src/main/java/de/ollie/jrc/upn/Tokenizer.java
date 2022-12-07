package de.ollie.jrc.upn;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

	public static final Tokenizer INSTANCE = new Tokenizer();

	public List<String> getTokens(String s) {
		List<String> tokens = new ArrayList<>();
		StringBuilder token = new StringBuilder();
		boolean quotedMode = false;
		while (!s.isEmpty()) {
			String firstChar = s.substring(0, 1);
			s = s.substring(1);
			if ((" ".equals(firstChar) && !token.isEmpty() && !quotedMode)) {
				tokens.add(token.toString());
				token = new StringBuilder();
			} else if ("'".equals(firstChar)) {
				if (quotedMode) {
					tokens.add(token.toString());
					token = new StringBuilder();
					quotedMode = false;
				} else {
					token = new StringBuilder();
					quotedMode = true;
				}
			} else if (s.isEmpty()) {
				token.append(firstChar);
				tokens.add(token.toString());
			} else {
				if (!" ".equals(firstChar) || quotedMode) {
					token.append(firstChar);
				}
			}
		}
		return tokens;
	}

}
