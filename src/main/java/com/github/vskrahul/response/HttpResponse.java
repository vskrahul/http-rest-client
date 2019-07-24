package com.github.vskrahul.response;

import java.util.List;
import java.util.Map;

public class HttpResponse {

	private String body;
	
	private Map<String, List<String>> header;
	
	private Integer statusCode;
	
	private String status;
	
	public HttpResponse(String body, Map<String, List<String>> header, Integer statusCode, String status) {
		this.body = body;
		this.header = header;
		this.statusCode = statusCode;
		this.status = status;
	}
	
	@Override
	public String toString() {
		return String.format("response=%s, statusCode=%d", body, statusCode);
	}
	
	public String get() {
		return body;
	}

	public Map<String, List<String>> header() {
		return header;
	}

	public Integer statusCode() {
		return statusCode;
	}

	public String status() {
		return status;
	}
}