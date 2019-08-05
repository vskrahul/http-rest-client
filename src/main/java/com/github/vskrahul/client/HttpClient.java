package com.github.vskrahul.client;

import java.io.IOException;
import java.util.Map;

import com.github.vskrahul.connection.HttpConnection;
import com.github.vskrahul.header.HttpHeaderConstants;
import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.security.basic.BasicAuthentication;
import com.github.vskrahul.security.oauth1.Oauth1;
import com.github.vskrahul.security.oauth1.OauthConstants;

public class HttpClient {

	private HttpConnection connection;
	
	private HttpRequest request;
	
	HttpClient() {
		this.connection = new HttpConnection();
	}
	
	public HttpClient get(String url) {
		this.request = new HttpRequest(HttpMethod.GET, url);
		return this;
	}
	
	public HttpClient post(String url) {
		this.request = new HttpRequest(HttpMethod.POST, url);
		return this;
	}
	
	/**
	 * Parse the HTTP URL and grab {@code userinfo} or {@code queryParams} if available.
	 * 
	 * <p>
	 * Generalized URI format
	 * 
	 * scheme://authority/path[?queryParams][#fragment]
	 * 
	 * <ul>
	 * 	<li>scheme = sequence of characters beginning with a letter and followed by any combination of letters, digits, plus ("+"), period ("."), or hyphen ("-")
	 * 	<li>authority = [ userinfo "@" ] host [ ":" port ]
	 * 	<li>
	 * </ul>
	 * 
	 * @param url http endpoint
	 */
	public void parseUrl(String url) {
		
	}
	
	/**
	 * Prepare and add {@code Authorization} in the request
	 * 
	 * <p>
	 * <b>Prerequisite:-</b> Adding any header, query, form or urlencoded data will be ignore after this call.
	 * 
	 * @param oauth {@link Oauth1}
	 * @return {@link HttpClient}
	 */
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
		this.header(HttpHeaderConstants.AUTHORIZATION
									,BasicAuthentication.basicAuthentication(username, password));
		return this;
	}
	
	public HttpClient trace() {
		this.request.trace();
		return this;
	}
	
	public HttpResponse execute() {
		return this.connection.execute(this.request);
	}
}