package com.lanking.uxb.service.zuoye.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 学生错题缓存
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
@Service
public final class StuFallQuestionCacheService extends AbstractCacheService {

	private ValueOperations<String, String> valOpt;

	private static final String TEXTBOOK_FLAG_KEY = "t";

	private String getTextbookFlagKey(long userId, int textbookCode) {
		return assemblyKey(TEXTBOOK_FLAG_KEY, userId, textbookCode);
	}

	public void setTextbookFlag(long userId, int textbookCode, String flag) {
		valOpt.set(getTextbookFlagKey(userId, textbookCode), flag);
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

	@Override
	public String getNs() {
		return "sfq";
	}

	@Override
	public String getNsCn() {
		return "学生错题本";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valOpt = getRedisTemplate().opsForValue();
	}

}
