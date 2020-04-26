[![Maven Central](https://img.shields.io/maven-central/v/com.github.vskrahul/http-rest-client.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.vskrahul%22%20AND%20a:%22http-rest-client%22)

# http-rest-client
An API to call HTTP REST full end-points which are secured by:-
1. Basic Authentication
2. URL with user profile (not recommended)
3. OAuth1

Project contains `TwitterApplication.java' to call Twitter API which secured with OAuth-1.0.

## HTTP Methods supported

### GET

```java

HttpResponse response = HttpClientBuilder.builder()
				.get("URL")
				.queryParam(key, value)
				.basicAuth(username, passwordCharArray) // if secured with basic authentication
				.oauth1(Oauth1.build()
					.consumerKey(consumerKey)
					.consumerSecret(consumerSecret)
					.token(accessToken)
					.tokenSecret(accessTokenSecret)
					.signatureMethod(OauthConstants.HMAC_SHA1)) //if secured with OAuth 1.0
				.trace() //if you want to log complete HTTP Response
				.execute();


```

### POST

```java

HttpResponse response = HttpClientBuilder.builder()
				.post("URL")
				.queryParam(key, value)
				.param(key, value)
				.body(body)
				.basicAuth(username, passwordCharArray) // if secured with basic authentication
				.oauth1(Oauth1.build()
					.consumerKey(consumerKey)
					.consumerSecret(consumerSecret)
					.token(accessToken)
					.tokenSecret(accessTokenSecret)
					.signatureMethod(OauthConstants.HMAC_SHA1)) //if secured with OAuth 1.0
				.trace() //if you want to log complete HTTP Response
				.execute();
                  
```
