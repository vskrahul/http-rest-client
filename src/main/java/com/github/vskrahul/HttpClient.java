/**
 * 
 */
package com.github.vskrahul;

import com.github.vskrahul.connection.HttpConnection;

/**
 * @author Rahul.Vishvakarma
 *
 */
public class HttpClient {

	public static void main(String args[]) throws Exception {
		
		HttpConnection connection = new HttpConnection();
		
		connection.post("http://localhost:8080/post").body("Hello sir").execute();
	}
}