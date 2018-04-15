package com.lanking.uxb.service.search.api;

import java.util.Map;

import com.google.common.collect.Maps;
import com.lanking.cloud.sdk.bean.IndexTypeable;

/**
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月6日
 */
public final class IndexInfo {
	public static Map<IndexTypeable, String> typeScripts = Maps.newHashMap();
	public static Map<IndexTypeable, IndexHandle> typeHandles = Maps.newHashMap();
}
