package com.github.vskrahul.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IOUtilTest {

	@Test
	public void testToBytes() {
		byte[] b = IOUtil.toBytes(new char[] {'r', 'a', 'h', 'u', 'l'});
		assertEquals("rahul", new String(b));
	}
	
	@Test(expected=NullPointerException.class)
	public void testToBytesNullCheck() {
		IOUtil.toBytes(null);
	}
	
	@Test
	public void testCombine() {
		char[] combined = IOUtil.combine(new char[] {'a', 'b'}, new char[] {'y', 'z'}, ':');
		assertArrayEquals("ab:yz".toCharArray(), combined);
	}
	
	@Test
	public void testWipe() {
		byte[] b_arr = "rahul".getBytes();
		int[] i_arr = new int[] {1,2,3,4, 5};
		char[] c_arr = "rahul".toCharArray();
		
		IOUtil.wipe(b_arr);
		IOUtil.wipe(i_arr);
		IOUtil.wipe(c_arr);
		
		assertArrayEquals(new byte[] {0, 0, 0, 0, 0}, b_arr);
		assertArrayEquals(new int[] {0, 0, 0, 0, 0}, i_arr);
		assertArrayEquals(new char[] {'\u0000','\u0000','\u0000','\u0000', '\u0000'}, c_arr);
	}
}
