/**
 * 
 */
package com.github.vskrahul;

import com.github.vskrahul.client.HttpClientBuilder;
import com.github.vskrahul.security.oauth1.Oauth1;
import com.github.vskrahul.security.oauth1.OauthConstants;

/**
 * @author Rahul.Vishvakarma
 *
 */
public class HttpClientApp {

	public static void main(String args[]) throws Exception {
		
		/*HttpClientBuilder.builder()
							.post("https://api.twitter.com/oauth/request_token")
							.header(OauthConstants.AUTHORIZATION, "OAuth oauth_consumer_key=h51JkHBVzk5ZlJxVrUiBsP7O4,oauth_signature_method=HMAC-SHA1,oauth_timestamp=1564562203,oauth_nonce=wZZfFN,oauth_version=1.0,oauth_signature=b8s9rGyhfuRRXX31OtShlursQeI%3D")
							.trace()
							.execute();*/
		
		
		Oauth1 oauth = Oauth1.build()
							.consumerKey("h51JkHBVzk5ZlJxVrUiBsP7O4")
							.consumerSecret("jIFefRFUiie8LO5YT9URklgdDIdI4ealwSfv3Y3IanqRlMZsct")
							.signatureMethod(OauthConstants.HMAC_SHA1);
							
		HttpClientBuilder.builder()
								.post("https://api.twitter.com/oauth/request_token")
								.oauth1(oauth)
								.trace()
								.execute();
	}
}