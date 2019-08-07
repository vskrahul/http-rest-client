package com.github.vskrahul.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectUtilTest {

	@Test
	public void testIsStringEmpty() {
		assertTrue(ObjectUtil.nonEmptyString.test("NOT_EMPTY"));
		assertFalse(ObjectUtil.nonEmptyString.test("   "));
		assertFalse(ObjectUtil.nonEmptyString.test(""));
		assertFalse(ObjectUtil.nonEmptyString.test(null));
	}
}
