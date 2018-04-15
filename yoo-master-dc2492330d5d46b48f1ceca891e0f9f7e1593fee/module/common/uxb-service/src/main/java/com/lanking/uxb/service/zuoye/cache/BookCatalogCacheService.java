package com.lanking.uxb.service.zuoye.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 书章节节点缓存数据子节点数据结构
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@Service
public class BookCatalogCacheService extends AbstractCacheService {
	private ZSetOperations<String, String> cataIdOps;
	private static final String CATALOGS_KEY = "ca";

	private String getCatalogsKey(long bookVersionId, long levelOneCatalogId) {
		return assemblyKey(CATALOGS_KEY, bookVersionId, levelOneCatalogId);
	}

	/**
	 * 加入书章节节点顺序
	 *
	 * @param bookVersionId
	 *            书本版本号
	 * @param levelOneCatalogId
	 *            第一层目录id
	 * @param cataId
	 *            章节节点id
	 */
	public void push(Long bookVersionId, Long levelOneCatalogId, Long cataId, Integer sequence) {
		cataIdOps.add(getCatalogsKey(bookVersionId, levelOneCatalogId), cataId.toString(), sequence);
	}

	/**
	 * 得到章节顺序列表
	 *
	 * @param bookVersionId
	 *            书本版本号
	 * @param levelOneCatalogId
	 *            第一层目录id
	 * @return {@link List}
	 */
	public List<Long> getCatalogs(long bookVersionId, long levelOneCatalogId) {
		Set<ZSetOperations.TypedTuple<String>> values;
		values = cataIdOps.rangeByScoreWithScores(getCatalogsKey(bookVersionId, levelOneCatalogId), 0, Long.MAX_VALUE);
		List<Long> ids = new ArrayList<Long>(values.size());
		for (ZSetOperations.TypedTuple<String> v : values) {
			ids.add(Long.valueOf(v.getValue()));
		}

		return ids;
	}

	/**
	 * 批量获得数据
	 *
	 * @param bookVersionId
	 *            书本版本号
	 * @param levelOneCatalogIds
	 *            第一层目录id列表
	 * @return {@link Map}
	 */
	public Map<Long, List<Long>> getCatalogs(long bookVersionId, Collection<Long> levelOneCatalogIds) {
		Map<Long, List<Long>> retMap = new HashMap<Long, List<Long>>(levelOneCatalogIds.size());
		for (Long id : levelOneCatalogIds) {
			List<Long> values = getCatalogs(bookVersionId, id);
			if (values.size() == 0) {
				return Maps.newHashMap();
			}
			retMap.put(id, values);
		}

		return retMap;
	}

	@Override
	public String getNs() {
		return "bcc";
	}

	@Override
	public String getNsCn() {
		return "书本章节缓存";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		cataIdOps = getRedisTemplate().opsForZSet();
	}
}
