/**
 * 
 */
package com.github.vskrahul;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.vskrahul.client.HttpClientBuilder;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.security.oauth1.Oauth1;
import com.github.vskrahul.security.oauth1.OauthConstants;

/**
 * @author Rahul.Vishvakarma
 *
 */
public class HttpClientApp {

	public static void main(String args[]) {
		
		/*HttpClientBuilder.builder()
							.post("https://api.twitter.com/oauth/request_token")
							.header(OauthConstants.AUTHORIZATION, "OAuth oauth_consumer_key=h51JkHBVzk5ZlJxVrUiBsP7O4,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1564562203,oauth_nonce=wZZfFN,oauth_version=1.0,oauth_signature=b8s9rGyhfuRRXX31OtShlursQeI%3D")
							.trace()
							.execute();*/
		
		
		Oauth1 oauth = Oauth1.build()
							.consumerKey("h51JkHBVzk5ZlJxVrUiBsP7O4")
							.consumerSecret("jIFefRFUiie8LO5YT9URklgdDIdI4ealwSfv3Y3IanqRlMZsct")
							.signatureMethod(OauthConstants.HMAC_SHA1);
							
		HttpResponse resp = HttpClientBuilder.builder()
								.post("https://api.twitter.com/oauth/request_token")
								.oauth1(oauth)
								.trace()
								.execute();
		
		Pattern p = Pattern.compile("oauth_token=(?<oauth_token>.+)&oauth_token_secret=(?<oauth_token_secret>.+)&oauth_callback_confirmed=(?<oauth_callback_confirmed>.+)");
		Matcher m = p.matcher(resp.get());
		
		String tokenKey = "";
		String tokenSecret = "";
		
		if(m.find()) {
			tokenKey = m.group(1);
			tokenSecret = m.group(2);
		}
		
		String verifier = "";
		
		oauth.token(tokenKey).tokenSecret(tokenSecret);
		
		resp = HttpClientBuilder.builder()
								.get("https://api.twitter.com/oauth/authorize")
								.queryParam("oauth_token", tokenKey)
								.oauth1(oauth)
								.trace()
								.execute();
		
		System.out.println(resp.get());
		
		/*
		resp = HttpClientBuilder.builder()
								.get("https://api.twitter.com/1.1/followers/list.json")
								.oauth1(oauth)
								.trace()
								.execute();
		
		System.out.println(resp.get());*/
	}
}