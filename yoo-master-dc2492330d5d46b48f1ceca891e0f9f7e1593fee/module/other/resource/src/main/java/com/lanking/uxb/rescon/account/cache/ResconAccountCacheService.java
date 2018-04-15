package com.lanking.uxb.rescon.account.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@SuppressWarnings("unchecked")
@Service
public class ResconAccountCacheService extends AbstractCacheService {

	private ValueOperations<String, Long> longOpt;
	private ValueOperations<String, String> strOpt;

	/**
	 * 登录错误次数key
	 */
	private static final String LOGIN_WRONG_TIME_KEY = "w";

	/**
	 * 校验员当前校验题目池key
	 */
	private static final String USER_CHECK_QUESTIONS_KEY = "qs";

	/**
	 * 当前转换知识点题目池key
	 */
	private static final String USER_KNOWLEDGE_QUESTIONS_KEY = "qk";

	@Override
	public String getNs() {
		return "rc-a";
	}

	@Override
	public String getNsCn() {
		return "资源平台-账户";
	}

	private String getLoginWrongTimeKey(String token) {
		return assemblyKey(LOGIN_WRONG_TIME_KEY, token);
	}

	private String getUserCheckQuestionsKey() {
		return USER_CHECK_QUESTIONS_KEY;
	}

	private String getUserKnowledgeQuestionsKey() {
		return USER_KNOWLEDGE_QUESTIONS_KEY;
	}

	public void incrLoginWrongTime(String token) {
		String key = getLoginWrongTimeKey(token);
		Long time = longOpt.get(key);
		if (time == null) {
			longOpt.set(key, 1L);
		} else {
			longOpt.set(key, time + 1L);
		}
	}

	public Long getLoginWrongTime(String token) {
		Long time = longOpt.get(getLoginWrongTimeKey(token));
		if (time == null) {
			return 0L;
		}
		return time;
	}

	public void invalidLoginWrongTime(String token) {
		getRedisTemplate().delete(getLoginWrongTimeKey(token));
	}

	/**
	 * 获得用户校验题目缓存.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getUserCheck() {
		String key = getUserCheckQuestionsKey();
		String value = strOpt.get(key);
		if (value == null) {
			return new HashMap<String, String>();
		} else {
			return JSON.parseObject(value, Map.class);
		}
	}

	/**
	 * 设置用户校验题目缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 * @param questionId
	 *            题目
	 */
	@SuppressWarnings("rawtypes")
	public void setUserCheck(long vendorUserId, long questionId) {
		String key = getUserCheckQuestionsKey();
		Map map = this.getUserCheck();
		map.put(String.valueOf(vendorUserId), String.valueOf(questionId));
		strOpt.set(key, JSON.toJSONString(map));
	}

	/**
	 * 清除用户校验题目缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 */
	@SuppressWarnings("rawtypes")
	public void invalidUserCheck(long vendorUserId) {
		String key = getUserCheckQuestionsKey();
		Map map = this.getUserCheck();
		map.remove(String.valueOf(vendorUserId));
		strOpt.set(key, JSON.toJSONString(map));
	}

	/**
	 * 清除所有用户校验题目缓存.
	 */
	@SuppressWarnings("rawtypes")
	public void invalidAllUserCheck() {
		String key = getUserCheckQuestionsKey();
		strOpt.set(key, JSON.toJSONString(new HashMap()));
	}

	/**
	 * 获得用户转换知识点题目缓存.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getUserKnowledge() {
		String key = getUserKnowledgeQuestionsKey();
		String value = strOpt.get(key);
		if (value == null) {
			return new HashMap<String, String>();
		} else {
			return JSON.parseObject(value, Map.class);
		}
	}

	/**
	 * 设置用户转换知识点题目缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 * @param questionId
	 *            题目
	 */
	@SuppressWarnings("rawtypes")
	public void setUserKnowledge(long vendorUserId, long questionId) {
		String key = getUserKnowledgeQuestionsKey();
		Map map = this.getUserKnowledge();
		map.put(String.valueOf(vendorUserId), String.valueOf(questionId));
		strOpt.set(key, JSON.toJSONString(map));
	}

	/**
	 * 清除用户转换知识点题目缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 */
	@SuppressWarnings("rawtypes")
	public void invalidUserKnowledge(long vendorUserId) {
		String key = getUserKnowledgeQuestionsKey();
		Map map = this.getUserKnowledge();
		map.remove(String.valueOf(vendorUserId));
		strOpt.set(key, JSON.toJSONString(map));
	}

	/**
	 * 清除所有用户转换知识点题目缓存.
	 */
	@SuppressWarnings("rawtypes")
	public void invalidAllUserKnowledge() {
		String key = getUserKnowledgeQuestionsKey();
		strOpt.set(key, JSON.toJSONString(new HashMap()));
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
		strOpt = getRedisTemplate().opsForValue();
	}
}
