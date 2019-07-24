package com.github.vskrahul.request;

public class Parameter {

	private String key;
	
	private String value;
	
	public Parameter(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public final String toString() {
		return String.format("%s=%s", key, value);
	}	
}