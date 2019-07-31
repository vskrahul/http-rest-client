package com.github.vskrahul.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class IOUtil {

	public static byte[] toBytes(char[] chars) {
	  CharBuffer charBuffer = CharBuffer.wrap(chars);
	  ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	  byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
	  wipe(byteBuffer.array());
	  return bytes;
	}
	
	public static char[] combine(char[] arr1, char[] arr2, char c) {
		
		char[] result = new char[arr1.length + arr2.length + 1];
		
		int k = 0;
		
		for(int i = 0; i < arr1.length; i++) {
			result[k++] = arr1[i];
		}
		
		result[k++] = c;
		
		for(int i = 0; i < arr2.length; i++) {
			result[k++] = arr2[i];
		}
		
		wipe(arr1);
		wipe(arr2);
		
 		return result;
	}
	
	public static void wipe(char[] arr) {
		Arrays.fill(arr, '\u0000');
	}
	
	public static void wipe(byte[] arr) {
		Arrays.fill(arr, (byte)0);
	}
	
	public static void wipe(int[] arr) {
		Arrays.fill(arr, 0);
	}
}
