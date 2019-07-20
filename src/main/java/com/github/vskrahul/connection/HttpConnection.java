package com.github.vskrahul.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.github.vskrahul.method.Method;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.response.Response;

public class HttpConnection implements Connection {

	private HttpURLConnection connection;
	
	private HttpRequest request;
	
	private HttpResponse response;
	
	public HttpConnection() {
		Method.values();
	}
	
	public Connection get(String url) throws IOException {
		this.request = new HttpRequest(Method.GET);
		openConnection(url);
		return this;
	}
	
	public Connection header(String key, String value) {
		this.connection.addRequestProperty(key, value);
		return this;
	}
	
	public Connection header(Map<String, String> headers) {
		headers.entrySet().stream().forEach(e -> this.connection.addRequestProperty(e.getKey(), e.getValue()));
		return this;
	}
	
	public Connection body(String body) {
		this.request.setBody(body);
		return this;
	}
	
	@Override
	public Response execute() {
		
		try {
			this.connection.connect();
		} catch(IOException e) {
			
		}
		
		return null;
	}
	
	private Connection openConnection(String url) throws IOException {
		try {
			URL _url = new URL(url);
			this.connection = (HttpURLConnection)_url.openConnection();
		} catch(IOException e) {
			throw e;
		}
		
		return this;
	}
}