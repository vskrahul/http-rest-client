package com.github.vskrahul.security.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class HmacSha1SignatureTest {

	@Test 
	public void testHexSign() {
		String sign = HmacSha1Signature.hexSign("data", "key");
		System.out.println(HmacSha1Signature.base64Sign("data", "key"));
		assertNotNull(sign);
		assertEquals("104152c5bfdca07bc633eebd46199f0255c9f49d", sign);
	}
	
	@Test
	public void testBase64Sign() {
		String sign = HmacSha1Signature.base64Sign("data", "key");
		assertNotNull(sign);
		assertEquals("EEFSxb/coHvGM+69RhmfAlXJ9J0=", sign);
	}
}