package com.lanking.cloud.sdk.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
public final class CollectionUtils {

	public static boolean isEmpty(Collection coll) {
		return (coll == null || coll.isEmpty());
	}

	public static boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	public static boolean isNotEmpty(Collection coll) {
		return !isEmpty(coll);
	}

	public static boolean isNotEmpty(Map map) {
		return !isEmpty(map);
	}

	public static <T> List<T> trimNull(List<T> list) {
		List<T> newList = new ArrayList<T>(list.size());
		for (T item : list) {
			if (item != null) {
				newList.add(item);
			}
		}
		return newList;
	}

	public static <T> List<T> merge(Collection<? extends Collection<T>> colls) {
		if (colls.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> all = Lists.newArrayList();
		for (Collection<T> ids : colls) {
			if (isNotEmpty(ids)) {
				all.addAll(ids);
			}
		}
		return all;
	}

	public static String listToString(Collection<String> list, String split) {
		int size = 0;
		if (list != null) {
			size = list.size();
		}
		if (size > 0) {
			StringBuilder sb = new StringBuilder();
			int index = 0;
			for (String value : list) {
				sb.append(value);
				if (index < list.size() - 1) {
					sb.append(split);
				}
				index++;
			}
			return sb.toString();
		} else {
			return StringUtils.EMPTY;
		}
	}

	private CollectionUtils() {
	}
}
