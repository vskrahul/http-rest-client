package com.github.vskrahul.request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.vskrahul.method.HttpMethod;

public class HttpRequest {

	private String body;
	
	private List<Parameter> param;
	
	private List<QueryParameter> queryParam;
	
	private HttpHeader header;
	
	private HttpMethod method;
	
	private String url;
	
	private boolean trace;
	
	public HttpRequest(HttpMethod method, String url) {
		this.method = method;
		this.url = url;
		this.header = new HttpHeader();
		this.param = new ArrayList<>();
		this.queryParam = new ArrayList<>();
	}

	public String getBody() {
		body = param.stream()
				.map(Parameter::toString)
				.collect(Collectors.joining("&"));
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public HttpMethod getMethod() {
		return method;
	}
	
	public void param(String key, String value) {
		param.add(new Parameter(key, value));
	}
	
	public void queryParam(String key, String value) {
		queryParam.add(new QueryParameter(key, value));
	}
	
	public List<Parameter> getParam() {
		return param;
	}

	public List<QueryParameter> getQueryParam() {
		return queryParam;
	}

	public boolean traceFlag() {
		return trace;
	}
	
	public void trace() {
		trace = true;
	}
	
	public String url() {
		return this.url;
	}
}