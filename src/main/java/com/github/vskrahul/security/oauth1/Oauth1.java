package com.github.vskrahul.security.oauth1;

import static com.github.vskrahul.security.oauth1.OauthConstants.HMAC_SHA1;
import static com.github.vskrahul.security.oauth1.OauthConstants.NONCE_CHARS;
import static com.github.vskrahul.security.oauth1.OauthConstants.NONCE_LEGTH;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_CONSUMER_KEY;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_NONCE;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_SIGNATURE;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_SIGNATURE_METHOD;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_TIMESTAMP;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_TOKEN;
import static com.github.vskrahul.security.oauth1.OauthConstants.OAUTH_VERSION;
import static com.github.vskrahul.security.oauth1.OauthConstants.REALM;
import static com.github.vskrahul.security.oauth1.OauthConstants.v1_0;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.vskrahul.exception.OauthException;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.security.crypto.HmacSha1Signature;
import com.github.vskrahul.util.ObjectUtil;

public class Oauth1 {

	private String consumerKey;

	private String consumerSecret;

	/**
	 * While requesting for:- 
	 * request token -> this will be empty
	 * access_token -> this will be oauth_token 
	 * resource -> this will be access_token
	 */
	private String token;

	/**
	 * While requesting for:- 
	 * request token -> this will be empty 
	 * access_token -> this will be oauth_token_secret 
	 * resource -> this will be access_token_secret
	 */
	private String tokenSecret;

	private String signatureMethod;

	private String signature;

	private String realm;
	
	private String nonce;
	
	private String timestamp;
	
	private Oauth1() {
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

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

	public String authorization(HttpRequest request) {
		
		this.timestamp = new Long(System.currentTimeMillis() / 1000).toString();
		this.nonce = nonce(NONCE_LEGTH);
		
		String encodedBaseUri = encodedBaseUri(request);
		String normalize = percentEncode(normalize(request));
		
		String baseString = encodedBaseUri + "&" + normalize;

		System.out.println(baseString);

		/*
		 * Calculate the signature using customer_secret & token_secret
		 */
		this.signature = HmacSha1Signature.base64Sign(baseString, signingKey());
		this.signature = percentEncode(this.signature);

		
		StringBuilder authorization = authorizationHeader();
		
		return authorization.toString();
	}
	
	/**
	 * Construct base string URI
	 * as per <a href='https://tools.ietf.org/html/rfc5849#section-3.4.1.2'>rfc5849#section-3.4.1.2</a>.
	 * 
	 * @param request {@link HttpRequest}
	 * @return encoded base URI
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	private String encodedBaseUri(HttpRequest request) {
		
		URL url = null;
		URI uri = null;
		
		try {
			url = new URL(request.url());
			uri = new URI(url.getProtocol().toLowerCase(), null
					,url.getHost().toLowerCase()
					,url.getDefaultPort() == url.getPort() ? -1 : url.getPort()
					,url.getPath()
					, null, null);
		} catch(MalformedURLException | URISyntaxException e) {
			throw new OauthException(e.getMessage());
		}
		
		return percentEncode(request.getMethod().name().toUpperCase()) 
							+ "&" 
							+ percentEncode(uri.toString());
	}

	/**
	 * Normalize request & header parameters as follows.
	 * 
	 * <ol>
	 *  <li>Prepare key value pair for all query parameters
	 *  <li>Prepare key value pair for all form parameters which are single-part only
	 *  <li>Prepare key value pair for keys passing in {@code Authorization} header except {@code realm} & {@code oauth_signature}
	 *  <li>do {@link #percentEncode(String)} on all key and value separately
	 *  <li>Sort the list with natural ordering by key, value.
	 *  <li>Concatenate key & value using '=' e.g.; encodedKey=encodedValue
	 *  <li>Concatenate all pair of encoded pair of key & value using '&'
	 * </ol>
	 * 
	 * <b>NOTE: exclude parameters with empty values</b>
	 * 
	 * @param request {@link HttpRequest}
	 * @return normalized value
	 */
	private String normalize(HttpRequest request) {
		List<Oauth1.KeyValue> keyVal = new ArrayList<>();

		List<Oauth1.KeyValue> kv1 = request.getQueryParam().stream()
				.map(q -> new KeyValue(percentEncode(q.getKey()), percentEncode(q.getValue()))).collect(Collectors.toList());

		List<Oauth1.KeyValue> kv2 = request.getParam().stream()
				.map(q -> new KeyValue(percentEncode(q.getKey()), percentEncode(q.getValue()))).collect(Collectors.toList());

		keyVal.addAll(kv1);
		keyVal.addAll(kv2);
		
		if(ObjectUtil.nonEmptyString.test(this.consumerKey))
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_CONSUMER_KEY), percentEncode(this.consumerKey)));
		
		if(ObjectUtil.nonEmptyString.test(this.token))
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_TOKEN), percentEncode(this.token)));
			
		if(ObjectUtil.nonEmptyString.test(this.signatureMethod))	
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_SIGNATURE_METHOD), percentEncode(this.signatureMethod)));
		
		if(ObjectUtil.nonEmptyString.test(this.nonce))
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_NONCE), percentEncode(this.nonce)));
		
		if(ObjectUtil.nonEmptyString.test(this.timestamp))
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_TIMESTAMP), percentEncode(this.timestamp)));
		
		if(ObjectUtil.nonEmptyString.test(v1_0))
			keyVal.add(new Oauth1.KeyValue(percentEncode(OAUTH_VERSION), percentEncode(v1_0)));

		Collections.sort(keyVal);

		return keyVal.stream().map(kv -> kv.key + "=" + kv.value).collect(Collectors.joining("&"));
	}

	/**
	 * Generate alpha-numeric nonce of given legth.
	 * 
	 * @param length length of nonce
	 * @return random nonce
	 */
	private String nonce(int length) {
		String result = "";
        for (int i = 0; i < length; ++i) {
        	double random = Math.random() * NONCE_CHARS.length() + 1;
            int rnum = (int) Math.floor(random);
            result += NONCE_CHARS.substring(rnum, rnum+1);
        }
        return result;
	}

	/**
	 * <p>
	 * Although they are disallowed within the URI syntax, we include here a
	 * description of those US-ASCII characters that have been excluded and the
	 * reasons for their exclusion.
	 * <p>
	 * The control characters in the US-ASCII coded character set are not used
	 * within a URI, both because they are non-printable and because they are likely
	 * to be misinterpreted by some control mechanisms.
	 * <p>
	 * control = &lt;US-ASCII coded characters 00-1F and 7F hexadecimal&gt;
	 * <p>
	 * The space character is excluded because significant spaces may disappear and
	 * insignificant spaces may be introduced when URI are transcribed or typeset or
	 * subjected to the treatment of word- processing programs. Whitespace is also
	 * used to delimit URI in many contexts.
	 * <p>
	 * space = &lt;US-ASCII coded character 20 hexadecimal&gt;
	 * <p>
	 * The angle-bracket "<" and ">" and double-quote (") characters are excluded
	 * because they are often used as the delimiters around URI in text documents
	 * and protocol fields. The character "#" is excluded because it is used to
	 * delimit a URI from a fragment identifier in URI references (Section 4). The
	 * percent character "%" is excluded because it is used for the encoding of
	 * escaped characters.
	 * <p>
	 * delims = "<" | ">" | "#" | "%" | <">
	 * <p>
	 * Encode US-ASCII characters so that every context which intercept the URI
	 * won't misinterpret them.
	 * 
	 * <p>
	 * Other characters are excluded because gateways and other transport agents are
	 * known to sometimes modify such characters, or they are used as delimiters.
	 * <p>
	 * unwise = "{" | "}" | "|" | "\" | "^" | "[" | "]" | "`"
	 * <p>
	 * Data corresponding to excluded characters must be escaped in order to be
	 * properly represented within a URI.
	 * 
	 * @param value URI component needs to be encoded
	 * @return encoded value
	 * @see <a href='https://tools.ietf.org/html/rfc5849#section-3.6'>percent encoding</a>
	 */
	private String percentEncode(String value) {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new OauthException(e.getMessage());
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
	
	/**
	 * Prepare signing to sign the base string
	 * 
	 * @return signing key
	 */
	private String signingKey() {
		String signaturKey = "";
		
		if(ObjectUtil.nonEmptyString.test(this.consumerSecret))
			signaturKey += percentEncode(this.consumerSecret);
		
		signaturKey += "&";
		
		if(ObjectUtil.nonEmptyString.test(this.tokenSecret))
			signaturKey += percentEncode(this.tokenSecret);

		return signaturKey;
	}
	
	private StringBuilder authorizationHeader() {
		StringBuilder authorization = new StringBuilder();
		
		authorization.append(OAUTH).append(" ");
		
		if(ObjectUtil.nonEmptyString.test(this.realm))
			authorization.append(REALM).append("=").append(this.realm).append(",");
		
		Objects.requireNonNull(this.consumerKey);
		Objects.requireNonNull(this.timestamp);
		Objects.requireNonNull(this.nonce);
		Objects.requireNonNull(this.signature);
		
		authorization.append(OAUTH_CONSUMER_KEY).append("=").append(this.consumerKey).append(",");
		
		if(ObjectUtil.nonEmptyString.test(this.token))
			authorization.append(OAUTH_TOKEN).append("=").append(this.token).append(",");
		
		authorization.append(OAUTH_SIGNATURE_METHOD).append("=").append(HMAC_SHA1).append(",");
		authorization.append(OAUTH_TIMESTAMP).append("=").append(this.timestamp).append(",");
		authorization.append(OAUTH_NONCE).append("=").append(this.nonce).append(",");
		authorization.append(OAUTH_VERSION).append("=").append(v1_0).append(",");
		authorization.append(OAUTH_SIGNATURE).append("=").append(this.signature);
		
		return authorization;
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
