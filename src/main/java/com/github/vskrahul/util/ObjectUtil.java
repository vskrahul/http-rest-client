package com.github.vskrahul.util;

import java.util.Objects;
import java.util.function.Predicate;

public class ObjectUtil {
	
	public static final Predicate<String> nonEmptyString = s -> !Objects.isNull(s) && !"".equals(s.trim());
	
	public static <R> R get(R[] arr, int idx, R defaultValue) {
		if(arr.length > idx) {
			return arr[idx];
		}
		return defaultValue;
	}
}