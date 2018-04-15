package com.lanking.uxb.rescon.question.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 记录题目符号纠错相关缓存
 * 
 * @since 中央资源库1.3.6
 * @author peng.zhao
 * @version 2017年11月1日
 */
@Service
public class ResconQuestionKatexSpecsCacheService extends AbstractCacheService {

	private SetOperations<String, String> setOpt;
	private ValueOperations<String, Long> longOpt;

	/**
	 * 当前正在处理的题目（所有）
	 */
	private static final String ALL_KATEX_SPECS_QUESTIONS_PROGRESSING_KEY = "apro";
	/**
	 * 已经处理完的题目（所有）
	 */
	private static final String ALL_KATEX_SPECS_QUESTIONS_FINISH_KEY = "af";

	@Override
	public String getNs() {
		return "rq-ks";
	}

	@Override
	public String getNsCn() {
		return "资源平台-题目符号纠错";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		setOpt = getRedisTemplate().opsForSet();
		longOpt = getRedisTemplate().opsForValue();
	}

	/**
	 * 获取所有习题.
	 * 
	 * @return
	 */
	public List<Long> getAllQuestions() {
		String key = assemblyKey(ALL_KATEX_SPECS_QUESTIONS_PROGRESSING_KEY);
		Set<String> caches = setOpt.members(key);
		List<Long> qids = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(caches)) {
			long now = System.currentTimeMillis();
			for (String cache : caches) {
				long questionId = Long.parseLong(cache.split("_")[0]);
				long timestap = Long.parseLong(cache.split("_")[2]);
				if ((now - timestap) / 1000 > 3600) {
					setOpt.remove(key, cache);
				} else {
					qids.add(questionId);
				}
			}
		}
		return qids;
	}

	/**
	 * 获取用户正在处理的习题.
	 * 
	 * @param userId
	 * @return
	 */
	public Long getUserDoingCache(long userId) {
		String key = assemblyKey(ALL_KATEX_SPECS_QUESTIONS_PROGRESSING_KEY);
		Set<String> values = setOpt.members(key);
		long now = System.currentTimeMillis();
		if (CollectionUtils.isNotEmpty(values)) {
			for (String value : values) {
				long uid = Long.parseLong(value.split("_")[1]);
				if (uid == userId) {
					long questionId = Long.parseLong(value.split("_")[0]);
					long timestap = Long.parseLong(value.split("_")[2]);
					if ((now - timestap) / 1000 > 3600) {
						setOpt.remove(key, value);
					} else {
						return questionId;
					}
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 设置正在处理中的习题.
	 * 
	 * @param questionId
	 */
	public void setDoingQuestion(long userId, long questionId) {
		String key = assemblyKey(ALL_KATEX_SPECS_QUESTIONS_PROGRESSING_KEY);
		setOpt.add(key, questionId + "_" + userId + "_" + System.currentTimeMillis());
	}

	/**
	 * 删除正在处理中的习题.
	 * 
	 * @param questionId
	 */
	public void invalidDoingQuestion(long questionId) {
		String key = assemblyKey(ALL_KATEX_SPECS_QUESTIONS_PROGRESSING_KEY);
		Set<String> values = setOpt.members(key);
		if (CollectionUtils.isNotEmpty(values)) {
			for (String value : values) {
				long qid = Long.parseLong(value.split("_")[0]);
				if (qid == questionId) {
					setOpt.remove(key, value);
					break;
				}
			}
		}
	}

	public long getFinishedQuestionsCount() {
		return longOpt.increment(assemblyKey(ALL_KATEX_SPECS_QUESTIONS_FINISH_KEY), 0);
	}

	public long incrFinishedQuestionsCount() {
		return longOpt.increment(assemblyKey(ALL_KATEX_SPECS_QUESTIONS_FINISH_KEY), 1L);
	}
}
