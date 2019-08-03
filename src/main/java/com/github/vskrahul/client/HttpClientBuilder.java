package com.github.vskrahul.client;

public class HttpClientBuilder {
	
	private HttpClientBuilder () {
		
	}
	
	public static HttpClient builder() {
		return new HttpClient();
	}	
}