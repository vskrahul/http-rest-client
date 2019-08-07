package com.github.vskrahul.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import com.github.vskrahul.connection.HttpConnection;
import com.github.vskrahul.exception.HttpException;
import com.github.vskrahul.header.HttpHeaderConstants;
import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.security.basic.BasicAuthentication;
import com.github.vskrahul.security.oauth1.Oauth1;
import com.github.vskrahul.security.oauth1.OauthConstants;
import com.github.vskrahul.util.ObjectUtil;

public class HttpClient {

	private HttpConnection connection;
	
	private HttpRequest request;
	
	HttpClient() {
		this.connection = new HttpConnection();
	}
	
	public HttpClient get(String url) {
		this.request = new HttpRequest(HttpMethod.GET, url);
		parseUrl(url);
		return this;
	}
	
	public HttpClient post(String url) {
		this.request = new HttpRequest(HttpMethod.POST, url);
		parseUrl(url);
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
	 * 	<li>queryParams = ?[key=value&key=value]
	 * </ul>
	 * 
	 * @param url http endpoint
	 */
	private void parseUrl(String url) {
		Objects.requireNonNull(url);
		URL _url = null;
		
		try {
			_url = new URL(url);
			
			if(Objects.nonNull(_url.getUserInfo())) {
				String[] userInfo = _url.getUserInfo().split(":");
				basicAuth(ObjectUtil.get(userInfo, 0, ""), ObjectUtil.get(userInfo, 1, "").toCharArray());
			}
			if(Objects.nonNull(_url.getQuery()))
				queryParam(_url.getQuery());
			
		} catch(MalformedURLException e) {
			throw new HttpException(e.getMessage());
		}
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
	
	private HttpClient queryParam(String query) {
		
		String[] key_value = query.split("&");
		
		if(key_value != null)
			for(String kv : key_value) {
				this.request.queryParam(
							 ObjectUtil.get(kv.split("="), 0, "")
							,ObjectUtil.get(kv.split("="), 1, "")
						);
			}
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