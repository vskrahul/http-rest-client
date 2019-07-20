package com.github.vskrahul.header;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpHeader {

	private Map<String, String> values;
	
	public HttpHeader() {
		this.values = new LinkedHashMap<>();
	}
	
	public void add(String key, String value) {
		this.values.put(key, value);
	}
	
	public void add(Map<String, String> values) {
		this.values.putAll(values);
	}
}