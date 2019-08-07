/**
 * 
 */
package com.github.vskrahul.twitter;

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
public class TwitterApplication {

	public static void main(String args[]) {
		
		String consumerKey = "changeit";
		String consumerSecret = "changeit";
		String rex = "oauth_token=(?<oauthToken>[^&]+)&oauth_token_secret=(?<oauthTokenSecret>[^&]+)";
		
							
		HttpResponse requestTokenResponse = HttpClientBuilder.builder()
														.post("https://api.twitter.com/oauth/request_token")
														.oauth1(Oauth1.build()
																.consumerKey(consumerKey)
																.consumerSecret(consumerSecret)
																.signatureMethod(OauthConstants.HMAC_SHA1))
														.trace()
														.execute();
		
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(requestTokenResponse.get());
		
		String requestToken = "";
		
		if(m.find()) {
			requestToken = m.group(1);
		}
		
		/**
		 * RESOURCE OWNER needs to be redirected to https://api.twitter.com/oauth/authorize?oauth_token={requestToken}
		 * And, authorize the CLIENT.
		 * Then, Authorization redirects the USER-AGENT to the configured redirect URL with OAuth verifier.
		 * Copy the OAuth verifier value and put in below variable 'oauthVerfier'.
		 * 
		 */
		String oauthVerifier = "edit_this_in_debugger";
		
		HttpResponse accessTokenResponse = HttpClientBuilder.builder()
													.post("https://api.twitter.com/oauth/access_token?oauth_verifier=" + oauthVerifier)
													.oauth1(Oauth1.build()
																.consumerKey(consumerKey)
																.consumerSecret(consumerSecret)
																.token(requestToken)
																.tokenSecret(oauthVerifier)
																.signatureMethod(OauthConstants.HMAC_SHA1))
													.trace()
													.execute();
		
		p = Pattern.compile(rex);
		m = p.matcher(accessTokenResponse.get());
		
		String accessToken = "";
		String accessTokenSecret = "";
		
		if(m.find()) {
			accessToken = m.group(1);
			accessTokenSecret = m.group(2);
		}
		
		HttpClientBuilder.builder()
							.get("https://api.twitter.com/1.1/followers/ids.json")
							.oauth1(Oauth1.build()
										.consumerKey(consumerKey)
										.consumerSecret(consumerSecret)
										.token(accessToken)
										.tokenSecret(accessTokenSecret)
										.signatureMethod(OauthConstants.HMAC_SHA1))
							.trace()
							.execute();
	}
}