package com.lanking.uxb.service.recommend.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
public class TeaRecommendCacheService extends AbstractCacheService {
	private ValueOperations<String, String> stringOpt;

	// 推荐习题
	private static final String QUESTIONS = "questions";

	@Override
	public String getNs() {
		return "tea-red";
	}

	@Override
	public String getNsCn() {
		return "教师推荐相关";
	}

	/**
	 * 获取习题推荐的Key.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @return
	 */
	public String getQuestionRecommendKey(long teacherId) {
		return assemblyKey(QUESTIONS, String.valueOf(teacherId));
	}

	/**
	 * 获取推荐习题.
	 * 
	 * @param teacherId
	 * @return
	 */
	public Map<String, Object> getQuestionRecommend(long teacherId) {
		String value = stringOpt.get(this.getQuestionRecommendKey(teacherId));
		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			JSONObject obj = JSONObject.parseObject(value);
			long timestamp = Long.parseLong(obj.get("timestamp").toString());

			long sectionCode = 0;
			if (obj.get("sectionCode") != null) {
				sectionCode = Long.parseLong(obj.get("sectionCode").toString());
			}
			JSONArray array = obj.getJSONArray("questionIds");
			List<Long> questionIds = new ArrayList<Long>(array.size());
			for (int i = 0; i < array.size(); i++) {
				questionIds.add(array.getLong(i));
			}
			JSONArray sourceArray = obj.getJSONArray("sources");
			List<String> sources = null;
			if (sourceArray != null) {
				sources = new ArrayList<String>(sourceArray.size());
				for (int i = 0; i < sourceArray.size(); i++) {
					sources.add(sourceArray.getString(i));
				}
			}
			JSONArray recommendTypeArray = obj.getJSONArray("recommendTypes");
			List<Integer> recommendTypes = null;
			if (recommendTypeArray != null) {
				recommendTypes = new ArrayList<Integer>(recommendTypeArray.size());
				for (int i = 0; i < recommendTypeArray.size(); i++) {
					recommendTypes.add(recommendTypeArray.getInteger(i));
				}
			}

			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("timestamp", timestamp);
			map.put("questionIds", questionIds);
			map.put("sectionCode", sectionCode);
			map.put("recommendTypes", recommendTypes);
			map.put("sources", sources);
			return map;
		}
	}

	/**
	 * 保存推荐习题.
	 * 
	 * @param teacherId
	 *            教师ID
	 * @param sectionCode
	 *            章节进度
	 * @param recommendTypes
	 *            推荐类型
	 * @param questionIds
	 *            习题列表
	 * @param sources
	 *            对应的推荐来源
	 */
	public void setQuestionRecommend(long teacherId, Long sectionCode, List<Integer> recommendTypes,
			List<Long> questionIds, List<String> sources) {
		JSONObject obj = new JSONObject();
		obj.put("timestamp", System.currentTimeMillis());
		obj.put("questionIds", questionIds);
		obj.put("sectionCode", sectionCode);
		obj.put("recommendTypes", recommendTypes);
		obj.put("sources", sources);
		stringOpt.set(this.getQuestionRecommendKey(teacherId), obj.toJSONString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		stringOpt = getRedisTemplate().opsForValue();
	}
}
