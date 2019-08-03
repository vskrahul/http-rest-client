package com.github.vskrahul.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectUtilTest {

	@Test
	public void testIsStringEmpty() {
		assertFalse(ObjectUtil.isStringEmpty.test("NOT_EMPTY"));
		assertTrue(ObjectUtil.isStringEmpty.test("   "));
		assertTrue(ObjectUtil.isStringEmpty.test(""));
		assertTrue(ObjectUtil.isStringEmpty.test(null));
	}
}
