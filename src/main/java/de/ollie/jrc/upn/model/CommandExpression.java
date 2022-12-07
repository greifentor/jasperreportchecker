package de.ollie.jrc.upn.model;

import lombok.Generated;

@Generated
public enum CommandExpression implements Expression {

	STARTS_WITH("STARTS_WITH");

	private String token;

	private CommandExpression(String token) {
		this.token = token;
	}

	@Override
	public String getToken() {
		return token;
	}

}
