package com.github.vskrahul.util;

import java.util.Objects;
import java.util.function.Predicate;

public class ObjectUtil {
	
	public static final Predicate<String> isStringEmpty = s -> Objects.isNull(s) || "".equals(s.trim());
	
}