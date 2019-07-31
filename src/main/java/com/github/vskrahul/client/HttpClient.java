package com.github.vskrahul.client;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import com.github.vskrahul.connection.HttpConnection;
import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.security.oauth1.Oauth1;
import com.github.vskrahul.security.oauth1.OauthConstants;
import com.github.vskrahul.util.IOUtil;

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
	
	public HttpClient oauth1(Oauth1 oauth) {
		try {
			header(OauthConstants.AUTHORIZATION, oauth.authorization(this.request));
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
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
	
	public HttpClient basicAuth(String username, char[] password) {
		
		byte[] pass = IOUtil.toBytes(IOUtil.combine(username.toCharArray(), password, ':'));
		
		String value = Base64.getEncoder().encodeToString(pass);
		
		this.header("Authorization", "Basic " + value);
		
		IOUtil.wipe(pass);
		IOUtil.wipe(password);
		
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