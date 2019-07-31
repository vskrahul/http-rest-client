package com.github.vskrahul.security.oauth1;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class HmacSha1Signature {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();

		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	public static String calculateRFC2104HMAC(String data, String key)
			throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		//return toHexString(mac.doFinal(data.getBytes()));
		return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes("UTF-8")));
	}

	public static void main(String[] args) throws Exception {
		String hmac = calculateRFC2104HMAC("POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&a%3Db%2520c%26encoded_form_data%3Dtrue%26oauth_consumer_key%3Dh51JkHBVzk5ZlJxVrUiBsP7O4%26oauth_nonce%3DRxQlJv%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1564598490%26oauth_version%3D1.0%26x%3Dy%252Bz", "jIFefRFUiie8LO5YT9URklgdDIdI4ealwSfv3Y3IanqRlMZsct&");

		System.out.println(hmac);
		assert hmac.equals("104152c5bfdca07bc633eebd46199f0255c9f49d");
	}
}
