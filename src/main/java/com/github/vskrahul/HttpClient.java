/**
 * 
 */
package com.github.vskrahul;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Rahul.Vishvakarma
 *
 */
public class HttpClient {

	private HttpClient() {
		
	}
	
	public static HttpClient build() {
		
		HttpClient client = new HttpClient();
		
		
		return client;
	}
	
	public HttpClient get(String url) {
		
		URL _url = null;
		
		try {
			_url = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)_url.openConnection();
			connection.setConnectTimeout(0);
			connection.setReadTimeout(0);
		} catch(IOException e) {
			
		}
		
		return this;
	}
	
	public HttpClient header(String key, String value) {
		
		
		
		return this;
	}
}