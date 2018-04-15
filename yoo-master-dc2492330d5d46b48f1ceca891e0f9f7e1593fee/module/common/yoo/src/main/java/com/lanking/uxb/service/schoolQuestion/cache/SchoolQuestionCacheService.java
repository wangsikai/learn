package com.lanking.uxb.service.schoolQuestion.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 学校题库相关缓存接口
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年7月28日
 */
@Service
public final class SchoolQuestionCacheService extends AbstractCacheService {

	private ValueOperations<String, String> valOpt;

	private static final String TEXTBOOK_FLAG_KEY = "t";

	private String getTextbookFlagKey(long schoolId, int textbookCode) {
		return assemblyKey(TEXTBOOK_FLAG_KEY, schoolId, textbookCode);
	}

	public void setTextbookFlag(long schoolId, int textbookCode, String flag) {
		valOpt.set(getTextbookFlagKey(schoolId, textbookCode), flag);
	}

	public void setTextbookFlag(long schoolId, List<Integer> textbookCodes, String flag) {
		for (Integer textbookCode : textbookCodes) {
			valOpt.set(getTextbookFlagKey(schoolId, textbookCode), flag);
		}
	}

	public String getTextbookFlag(long schoolId, int textbookCode) {
		return valOpt.get(getTextbookFlagKey(schoolId, textbookCode));
	}

	public Map<Integer, String> mgetTextbookFlag(long schoolId, List<Integer> textbookCodes) {
		Map<Integer, String> map = Maps.newHashMap();
		List<String> keys = new ArrayList<String>(textbookCodes.size());
		Map<String, Integer> keyM = Maps.newHashMap();
		for (Integer textbookCode : textbookCodes) {
			keys.add(getTextbookFlagKey(schoolId, textbookCode));
			keyM.put(getTextbookFlagKey(schoolId, textbookCode), textbookCode);
		}
		List<String> flags = valOpt.multiGet(keys);
		int index = 0;
		for (String key : keys) {
			map.put(keyM.get(key), flags.get(index));
			index++;
		}
		return map;
	}

	@Override
	public String getNs() {
		return "sq";
	}

	@Override
	public String getNsCn() {
		return "学校题库";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valOpt = getRedisTemplate().opsForValue();
	}

}
