package com.lanking.uxb.service.fallible.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 教师错题缓存操作接口
 * 
 * @since 2.2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月2日
 */
@Service
public final class TeacherFallibleCacheService extends AbstractCacheService {

	private ValueOperations<String, String> valOpt;

	private static final String TEXTBOOK_FLAG_KEY = "t";
	private static final String TEXTBOOK_LATEST_KEY = "tl";

	private String getTextbookFlagKey(long userId, int textbookCode) {
		return assemblyKey(TEXTBOOK_FLAG_KEY, userId, textbookCode);
	}

	public void setTextbookFlag(long userId, int textbookCode, String flag) {
		valOpt.set(getTextbookFlagKey(userId, textbookCode), flag);
		valOpt.set(assemblyKey(TEXTBOOK_LATEST_KEY, userId, textbookCode / 1000000), String.valueOf(textbookCode));
	}

	public String getTextbookFlag(long userId, int textbookCode) {
		return valOpt.get(getTextbookFlagKey(userId, textbookCode));
	}

	public Map<Integer, String> mgetTextbookFlag(long userId, List<Integer> textbookCodes) {
		Map<Integer, String> map = Maps.newHashMap();
		List<String> keys = new ArrayList<String>(textbookCodes.size());
		Map<String, Integer> keyM = Maps.newHashMap();
		for (Integer textbookCode : textbookCodes) {
			keys.add(getTextbookFlagKey(userId, textbookCode));
			keyM.put(getTextbookFlagKey(userId, textbookCode), textbookCode);
		}
		List<String> flags = valOpt.multiGet(keys);
		int index = 0;
		for (String key : keys) {
			map.put(keyM.get(key), flags.get(index));
			index++;
		}
		return map;
	}

	public int getTextbookCurrent(long userId, int textbookCategoryCode) {
		String textbook = valOpt.get(assemblyKey(TEXTBOOK_LATEST_KEY, userId, textbookCategoryCode));
		return textbook == null ? 0 : Integer.parseInt(textbook);
	}

	@Override
	public String getNs() {
		return "tfq";
	}

	@Override
	public String getNsCn() {
		return "教师错题本";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valOpt = getRedisTemplate().opsForValue();
	}

}
