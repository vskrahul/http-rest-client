/**
 * 
 */
package com.github.vskrahul.security.basic;

import java.util.Base64;

import com.github.vskrahul.util.IOUtil;

/**
 * @author Rahul Vishvakarma
 *
 */
public class BasicAuthentication {

	public static String basicAuthentication(String username, char[] password) {
		byte[] pass = IOUtil.toBytes(IOUtil.combine(username.toCharArray(), password, ':'));
		
		String value = Base64.getEncoder().encodeToString(pass);
		
		IOUtil.wipe(pass);
		IOUtil.wipe(password);
		
		return "Basic " + value;
	}
}
