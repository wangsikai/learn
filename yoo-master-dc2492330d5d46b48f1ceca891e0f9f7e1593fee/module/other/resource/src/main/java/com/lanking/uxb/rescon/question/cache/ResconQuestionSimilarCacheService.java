package com.lanking.uxb.rescon.question.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
public class ResconQuestionSimilarCacheService extends AbstractCacheService {
	private ValueOperations<String, String> strOpt;
	private SetOperations<String, String> setOpt;

	@Override
	public String getNs() {
		return "rq-qs";
	}

	@Override
	public String getNsCn() {
		return "资源平台-相似题";
	}

	/**
	 * 当前正在处理的相似题组（所有）
	 */
	private static final String ALL_SIMILAR_QUESTIONS_KEY = "a";

	/**
	 * 当前用户正在处理的相似题组
	 */
	private static final String USER_SIMILAR_QUESTIONS_KEY = "u";

	/**
	 * 正在删除的相似题组
	 */
	private static final String DELETE_SIMILAR_QUESTIONS_KEY = "dt";

	private String getSimilarQuestionsKey(Long userId) {
		if (userId == null) {
			return assemblyKey(ALL_SIMILAR_QUESTIONS_KEY);
		} else {
			return assemblyKey(USER_SIMILAR_QUESTIONS_KEY, userId);
		}
	}

	private String getDeleteSimilarQuestionsKey() {
		return assemblyKey(DELETE_SIMILAR_QUESTIONS_KEY);
	}

	/**
	 * 获得用户正在处理的题组集合.
	 * 
	 * @param vendorUserId
	 *            处理人
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getUserSimilar(long vendorUserId) {
		String allKey = this.getSimilarQuestionsKey(null);
		String userKey = this.getSimilarQuestionsKey(vendorUserId);

		String userCahce = strOpt.get(userKey); // 当前用户正在处理的题组
		Set<String> allCahce = setOpt.members(allKey); // 所有用户正在处理的题组
		if (CollectionUtils.isEmpty(allCahce) || !allCahce.contains(userCahce)) {
			userCahce = null;
			getRedisTemplate().delete(userKey);
		}

		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("userCahce", userCahce);
		map.put("allCahce", allCahce);
		return map;
	}

	/**
	 * 设置用户相似题组缓存.
	 * 
	 * @param vendorUserId
	 *            处理人
	 * @param similarId
	 *            相似题组ID
	 */
	public void setUserSimilar(long vendorUserId, String questionSimilarMD5) {
		String allKey = this.getSimilarQuestionsKey(null);
		String userKey = this.getSimilarQuestionsKey(vendorUserId);

		strOpt.set(userKey, questionSimilarMD5);
		setOpt.add(allKey, questionSimilarMD5);
	}

	/**
	 * 清除用户相似题组缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 */
	@SuppressWarnings("unchecked")
	public void invalidUserSimilar(long vendorUserId) {
		String allKey = this.getSimilarQuestionsKey(null);
		String userKey = this.getSimilarQuestionsKey(vendorUserId);

		String userSimilarQuestionMD5Cahce = strOpt.get(userKey); // 当前用户正在处理的题组
		if (StringUtils.isNotBlank(userSimilarQuestionMD5Cahce)) {
			getRedisTemplate().delete(userKey);

			Set<String> allCahce = setOpt.members(allKey); // 所有用户正在处理的题组
			if (CollectionUtils.isNotEmpty(allCahce)) {
				setOpt.remove(allKey, userSimilarQuestionMD5Cahce);
			}
		}
	}

	/**
	 * 获得相似题组删除缓存.
	 * 
	 * @return
	 */
	public Set<String> getDeleteSimilar() {
		String deleteKey = this.getDeleteSimilarQuestionsKey();
		Set<String> allCahce = setOpt.members(deleteKey);
		Set<String> result = new HashSet<String>();
		long time = System.currentTimeMillis();
		if (CollectionUtils.isNotEmpty(allCahce)) {
			for (String value : allCahce) {
				String[] values = value.split("_");
				if (values.length > 1 && time - Long.parseLong(values[1]) > 60 * 1000) {
					setOpt.remove(deleteKey, value);
				} else {
					result.add(values[0]);
				}
			}
		}
		return result;
	}

	/**
	 * 设置相似题组删除缓存.
	 * 
	 * @param similarId
	 *            相似题组ID
	 */
	public void setDeleteSimilar(String questionSimilarMD5) {
		String deleteKey = this.getDeleteSimilarQuestionsKey();
		setOpt.add(deleteKey, questionSimilarMD5 + "_" + System.currentTimeMillis());
	}

	/**
	 * 清除相似题组删除缓存.
	 * 
	 * @param vendorUserId
	 *            校验员
	 */
	public void invalidDeleteSimilar(String questionSimilarMD5) {
		String deleteKey = this.getDeleteSimilarQuestionsKey();
		Set<String> allCahce = setOpt.members(deleteKey);
		if (CollectionUtils.isNotEmpty(allCahce)) {
			for (String value : allCahce) {
				if (value.indexOf(questionSimilarMD5) != -1) {
					setOpt.remove(deleteKey, value);
					break;
				}
			}
		}
	}

	/**
	 * 清除所有用户相似题目缓存.
	 */
	@SuppressWarnings("unchecked")
	public void invalidAllUserSimilar() {
		String allKey = this.getSimilarQuestionsKey(null);
		getRedisTemplate().delete(allKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		strOpt = getRedisTemplate().opsForValue();
		setOpt = getRedisTemplate().opsForSet();
	}
}
