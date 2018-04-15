package com.lanking.uxb.service.question.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 提供习题相关缓存操作
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月15日
 */
@Service
public class BaseQuestionCacheService extends AbstractCacheService {
	private ValueOperations<String, String> codeOpt;
	private static final String QUESTION_KEY = "bs-q";
	private static final String QUESTION_CODE_KEY = "code";

	@Override
	public String getNs() {
		return QUESTION_KEY;
	}

	@Override
	public String getNsCn() {
		return "base-习题";
	}

	private String getCodeKey(String prefix) {
		return assemblyKey(QUESTION_CODE_KEY, prefix);
	}

	public String getMaxCode(String prefix) {
		return codeOpt.get(getCodeKey(prefix));
	}

	public String getNextCode(String prefix, String currentCode) {
		String newCode = "";
		if (StringUtils.isNotBlank(currentCode)) {
			if (currentCode.length() < 9) {
				currentCode = currentCode + String.format("%0" + (9 - currentCode.length()) + "d", 1);
			}
			char p = currentCode.charAt(2);
			String s = currentCode.substring(3, currentCode.length());
			if ("999999".equals(s)) {
				newCode = prefix + (char) ((int) p + 1) + "000001";
			} else {
				newCode = prefix + p + String.format("%06d", Integer.parseInt(s) + 1);
			}
		}
		return newCode;
	}

	public String setCode(String prefix, String newCode) {
		String maxCode = getMaxCode(prefix);
		if (newCode.equals(maxCode)) {
			newCode = this.getNextCode(prefix, newCode);
		}
		codeOpt.set(getCodeKey(prefix), newCode);
		return newCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		codeOpt = getRedisTemplate().opsForValue();
	}
}
