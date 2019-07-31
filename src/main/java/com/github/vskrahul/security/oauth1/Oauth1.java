package com.github.vskrahul.security.oauth1;

import static com.github.vskrahul.security.oauth1.OauthConstants.*;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.vskrahul.request.HttpRequest;

public class Oauth1 {

	private String consumerKey;

	private String consumerSecret;

	/**
	 * While requesting for:- request token -> this will be empty access_token ->
	 * this will be oauth_token resource -> this will be access_token
	 */
	private String token;

	/**
	 * While requesting for:- request token -> this will be empty access_token ->
	 * this will be oauth_token_secret resource -> this will be access_token_secret
	 */
	private String tokenSecret;

	private String signatureMethod;

	private String signature;

	private String realm;
	
	private String nonce;
	
	private String timestamp;

	public static Oauth1 build() {
		return new Oauth1();
	}

	public Oauth1 consumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
		return this;
	}

	public Oauth1 consumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
		return this;
	}

	public Oauth1 token(String token) {
		this.token = token;
		return this;
	}

	public Oauth1 tokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
		return this;
	}

	public Oauth1 signatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
		return this;
	}

	public Oauth1 realm(String realm) {
		this.realm = realm;
		return this;
	}

	public String authorization(HttpRequest request) throws Exception {
		
		this.timestamp = new Long(System.currentTimeMillis() / 1000).toString();
		this.nonce = nonce(NONCE_LEGTH);
		
		String encodedBaseUri = encodedBaseUri(request);
		String normalize = encode(normalize(request));
		
		String baseString = encodedBaseUri + "&" + normalize;

		System.out.println(baseString);

		/*
		 * Calculate the signature using customer_secret & token_secret
		 */
		this.signature = HmacSha1Signature.calculateRFC2104HMAC(baseString, encode(this.consumerSecret) + '&');
		this.signature = encode(this.signature);

		
		StringBuilder authorization = new StringBuilder();
		
		authorization.append(OAUTH).append(" ")
						.append(OAUTH_CONSUMER_KEY).append("=").append(this.consumerKey).append(",")
						.append(OAUTH_SIGNATURE_METHOD).append("=").append(HMAC_SHA1).append(",")
						.append(OAUTH_TIMESTAMP).append("=").append(this.timestamp).append(",")
						.append(OAUTH_NONCE).append("=").append(this.nonce).append(",")
						.append(OAUTH_VERSION).append("=").append(v1_0).append(",")
						.append(OAUTH_SIGNATURE).append("=").append(this.signature);
						
		System.out.println(authorization.toString());
		return authorization.toString();
	}
	
	private String encodedBaseUri(HttpRequest request) throws Exception {
		
		URL url = new URL(request.url());

		URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);

		if (url.getQuery() != null)
			for (String q : url.getQuery().split("&")) {
				String[] query = q.split("=");
				request.queryParam(query[0], query[1]);
			}
		
		String encodedUri = request.getMethod().name().toUpperCase() + "&" + encode(uri.toString());
		
		return encodedUri;
	}

	private String normalize(HttpRequest request) {
		List<Oauth1.KeyValue> keyVal = new ArrayList<>();

		List<Oauth1.KeyValue> kv1 = request.getQueryParam().stream()
				.map(q -> new KeyValue(encode(q.getKey()), encode(q.getValue()))).collect(Collectors.toList());

		List<Oauth1.KeyValue> kv2 = request.getParam().stream()
				.map(q -> new KeyValue(encode(q.getKey()), encode(q.getValue()))).collect(Collectors.toList());

		keyVal.addAll(kv1);
		keyVal.addAll(kv2);
		
		keyVal.add(new Oauth1.KeyValue(encode(OAUTH_CONSUMER_KEY), encode(this.consumerKey)));
		//keyVal.add(new Oauth1.KeyValue(encode(OAUTH_TOKEN), encode(this.token == null ? "" : this.token)));
		keyVal.add(new Oauth1.KeyValue(encode(OAUTH_SIGNATURE_METHOD), encode(this.signatureMethod)));
		keyVal.add(new Oauth1.KeyValue(encode(OAUTH_NONCE), encode(this.nonce)));
		keyVal.add(new Oauth1.KeyValue(encode(OAUTH_TIMESTAMP), encode(this.timestamp)));
		keyVal.add(new Oauth1.KeyValue(OAUTH_VERSION, encode(v1_0)));

		Collections.sort(keyVal);

		return keyVal.stream().map(kv -> kv.key + "=" + kv.value).collect(Collectors.joining("&"));
	}

	private String nonce(int length) throws Exception {
		String result = "";
        for (int i = 0; i < length; ++i) {
        	double random = Math.random() * NONCE_CHARS.length() + 1;
            int rnum = (int) Math.floor(random);
            result += NONCE_CHARS.substring(rnum, rnum+1);
        }
        return result;
	}

	private static String encode(String value) {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(value, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sb = "";
		char focus;
		for (int i = 0; i < encoded.length(); i++) {
			focus = encoded.charAt(i);
			if (focus == '*') {
				sb += "%2A";
			} else if (focus == '+') {
				sb += "%20";
			} else if (focus == '%' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '7'
					&& encoded.charAt(i + 2) == 'E') {
				sb += '~';
				i += 2;
			} else {
				sb += focus;
			}
		}
		return sb.toString();
	}

	private static class KeyValue implements Comparable<KeyValue> {
		String key;
		String value;

		public KeyValue(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int compareTo(KeyValue kv) {

			if (this.key.equals(kv.key)) {
				return this.value.compareTo(kv.value);
			} else {
				return this.key.compareTo(kv.key);
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			KeyValue kv = (KeyValue) obj;
			return this.key.equals(kv.key) && this.value.equals(kv.value);
		}
	}
}
