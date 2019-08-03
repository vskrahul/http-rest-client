package com.github.vskrahul.security.oauth1;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;

@RunWith(MockitoJUnitRunner.class)
public class Oauth1Test {

	@Mock
	private HttpRequest request;
	
	@Test
	public void testBuild() throws Exception {
		assertNotEquals(Oauth1.build(), Oauth1.build());
		assertNotNull(Oauth1.build());
	}
	
	@Test
	public void testAuthorization() {
		
		when(request.getMethod()).thenReturn(HttpMethod.POST);
		when(request.url()).thenReturn("https://api.twitter.com/oauth/request_token");
		
		Oauth1 oauth = Oauth1.build()
				.consumerKey("h51JkHBVzk5ZlJxVrUiBsP7O4")
				.consumerSecret("jIFefRFUiie8LO5YT9URklgdDIdI4ealwSfv3Y3IanqRlMZsct")
				.signatureMethod(OauthConstants.HMAC_SHA1);
		
		String authorization = oauth.authorization(this.request);
		
		assertNotNull(authorization);
	}
}