package com.lanking.cloud.sdk.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

	public static String[] subarray(String[] array, int startIndex) {
		if (array == null) {
			return EMPTY_STRING_ARRAY;
		} else if (startIndex <= 0) {
			return array;
		}
		int newSize = array.length - startIndex;
		if (newSize <= 0) {
			return EMPTY_STRING_ARRAY;
		}
		String[] subArray = new String[newSize];
		System.arraycopy(array, startIndex, subArray, 0, newSize);
		return subArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> asList(T... a) {
		return a == null ? Collections.<T> emptyList() : Arrays.asList(a);
	}

	private ArrayUtils() {
	}
}
