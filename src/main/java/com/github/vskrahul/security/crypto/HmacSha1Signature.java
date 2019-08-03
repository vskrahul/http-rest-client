package com.github.vskrahul.security.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.github.vskrahul.exception.OauthException;
import com.github.vskrahul.security.oauth1.OauthConstants;

public class HmacSha1Signature {

    public static String hexSign(String data, String key) {
        Formatter formatter = new Formatter();

        byte[] bytes = sign(data, key);
        
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    
    public static String base64Sign(String data, String key) {
    	byte[] bytes = sign(data, key);
    	return Base64.getEncoder().encodeToString(bytes);
    }
	
	private static byte[] sign(String data, String key) {
		byte[] sign = null;
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), OauthConstants.HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(OauthConstants.HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			sign = mac.doFinal(data.getBytes("UTF-8"));
		} catch(UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
			new OauthException(String.format("Unable to sign %s with key:%s", data, key));
		}
		return sign;
	}
}