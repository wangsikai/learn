package com.lanking.cloud.component.db.support.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.google.common.collect.Maps;

@SuppressWarnings({ "unchecked", "rawtypes" })
public final class Hibernates {

	public static void init(Object proxy) {
		Hibernate.initialize(proxy);
	}

	public static int getInt(Object o) {
		if (o == null) {
			return 0;
		}
		return ((Number) o).intValue();
	}

	public static long getLong(Object o) {
		if (o == null) {
			return 0;
		}
		return ((Number) o).longValue();
	}

	public static float getFloat(Object o) {
		if (o == null) {
			return 0;
		}
		return ((Number) o).floatValue();
	}

	public static double getDouble(Object o) {
		if (o == null) {
			return 0;
		}
		return ((Number) o).doubleValue();
	}

	public static Map<String, Integer> toIntMap(List list) {
		Map<String, Integer> map = Maps.newHashMap();
		for (Object[] arr : (List<Object[]>) list) {
			map.put((String) arr[0], getInt(arr[1]));
		}
		return map;
	}

	public static Map<String, Float> toFloatMap(List list) {
		Map<String, Float> map = Maps.newHashMap();
		for (Object[] arr : (List<Object[]>) list) {
			map.put((String) arr[0], getFloat(arr[1]));
		}
		return map;
	}

	public static Map<String, Double> toDoubleMap(List list) {
		Map<String, Double> map = Maps.newHashMap();
		for (Object[] arr : (List<Object[]>) list) {
			map.put((String) arr[0], getDouble(arr[1]));
		}
		return map;
	}

	private Hibernates() {
	}
}
