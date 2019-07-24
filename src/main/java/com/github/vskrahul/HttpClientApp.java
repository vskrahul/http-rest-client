/**
 * 
 */
package com.github.vskrahul;

import com.github.vskrahul.client.HttpClientBuilder;
import com.github.vskrahul.response.HttpResponse;

/**
 * @author Rahul.Vishvakarma
 *
 */
public class HttpClientApp {

	public static void main(String args[]) throws Exception {
		
		HttpResponse resp = HttpClientBuilder.builder()
							.get("http://localhost:8080/get/no-query")
							.header("Test", "Query GET with no query parameters.")
							//.trace()
							.execute();
		System.out.println(resp.toString());
		
		System.out.println(Runtime.getRuntime().availableProcessors());
		
		/*HttpClientBuilder.builder()
							.get("http://localhost:8080/get/query")
							.queryParam("fname", "Rahul")
							.queryParam("lname", "Vsk")
							.header("Test", "Query GET with no query parameters.")
							.trace()
							.execute();
		
		HttpClientBuilder.builder()
							.post("http://localhost:8080/post/form-url")
							.param("fname", "Rahul")
							.param("lname", "Vsk")
							.header("Content-Type","application/x-www-form-urlencoded")
							//.trace()
							.execute();*/
	}
}