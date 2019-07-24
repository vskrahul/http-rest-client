package com.github.vskrahul.client;

import java.io.IOException;
import java.util.Map;

import com.github.vskrahul.connection.HttpConnection;
import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;

public class HttpClient {

	private HttpConnection connection;
	
	private HttpRequest request;
	
	HttpClient() {
		this.connection = new HttpConnection();
	}
	
	public HttpClient get(String url) throws IOException {
		this.request = new HttpRequest(HttpMethod.GET, url);
		return this;
	}
	
	public HttpClient post(String url) throws IOException {
		this.request = new HttpRequest(HttpMethod.POST, url);
		return this;
	}
	
	public HttpClient header(String key, String value) {
		this.request.getHeader().add(key, value);
		return this;
	}
	
	public HttpClient header(Map<String, String> headers) {
		this.request.getHeader().add(headers);
		return this;
	}
	
	public HttpClient body(String body) throws IOException {
		this.request.setBody(body);
		return this;
	}
	
	public HttpClient param(String key, String value) {
		this.request.param(key, value);
		return this;
	}
	
	public HttpClient queryParam(String key, String value) {
		this.request.queryParam(key, value);
		return this;
	}
	
	public HttpClient trace() {
		this.request.trace();
		return this;
	}
	
	public HttpResponse execute() throws IOException {
		return this.connection.execute(this.request);
	}
}