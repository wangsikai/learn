package com.alibaba.fastjson;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class LongSerializerTest {

	long longValue = 301463343992946688L;

	@Test
	public void testLong() {
		assertEquals("\"301463343992946688\"", JSON.toJSONString(longValue));
	}

	@Test
	public void testList() {
		List<Long> longList = Lists.newArrayList(longValue);
		assertEquals("[\"301463343992946688\"]", JSON.toJSONString(longList));
	}

	@Test
	public void testSet() {
		Set<Long> longSet = Sets.newHashSet(longValue);
		assertEquals("[\"301463343992946688\"]", JSON.toJSONString(longSet));
	}

	@Test
	public void testArray() {
		Long[] longArray = { longValue };
		assertEquals("[\"301463343992946688\"]", JSON.toJSONString(longArray));
	}

	@Test
	public void testMap() {
		Map<Long, Long> longMap = Maps.newHashMap();
		longMap.put(longValue, longValue);
		assertEquals("{\"301463343992946688\":\"301463343992946688\"}", JSON.toJSONString(longMap));

	}

	@Test
	public void testJavaBean() {
		User user = new User();
		user.setId(longValue);
		assertEquals("{\"id\":\"301463343992946688\"}", JSON.toJSONString(user));

	}
}
