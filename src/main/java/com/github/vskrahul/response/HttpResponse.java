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

	public String getBody() {
		return body;
	}

	public Map<String, List<String>> getHeader() {
		return header;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public String getStatus() {
		return status;
	}
}