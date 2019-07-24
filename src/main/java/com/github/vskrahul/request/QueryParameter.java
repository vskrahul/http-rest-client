package com.github.vskrahul.request;

public class QueryParameter {

	String key;
	
	String value;

	public QueryParameter(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	@Override
	public final String toString() {
		return String.format("%s=%s", key, value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
