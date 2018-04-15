package com.lanking.uxb.rescon.account.cache;

import java.util.Set;

import com.google.common.collect.Sets;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 转换的时候 用，避免一到题目多次被用
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月15日 下午4:41:38
 */
@SuppressWarnings("unchecked")
@Service
public class ResconQuestionConvertCacheService extends AbstractCacheService {

	private SetOperations<String, String> qIdOpt;

	private static final String QUESTION_ASCII_CONVERT_KEY = "qc1";
	private static final String QUESTION_ASCII_CHECK_KEY = "qc2";

	@Override
	public String getNs() {
		return "rc-qc";
	}

	@Override
	public String getNsCn() {
		return "资源平台-题目转化";
	}

	private String getQuestioAsciiConvertKey() {
		return QUESTION_ASCII_CONVERT_KEY;
	}

	private String getQuestioAsciiCheckKey() {
		return QUESTION_ASCII_CHECK_KEY;
	}

	/**
	 * 获得所有转换题目的缓存.
	 * 
	 * @return
	 */
	public Set<String> getConvertAsciiQuestion() {
		Set<String> values = qIdOpt.members(this.getQuestioAsciiConvertKey());
		if (values == null) {
			return Sets.newHashSet();
		} else {
			return values;
		}
	}

	/**
	 * 获得所有校验题目的缓存.
	 * 
	 * @return
	 */
	public Set<String> getCheckAsciiQuestion() {
		Set<String> values = qIdOpt.members(this.getQuestioAsciiCheckKey());
		if (values == null) {
			return Sets.newHashSet();
		} else {
			return values;
		}
	}

	/**
	 * 设置用户转换题目缓存.
	 * 
	 * @param userId
	 *            用户
	 * @param questionId
	 *            题目
	 */
	public void setConvertQuestion(long userId, long questionId) {
		qIdOpt.add(this.getQuestioAsciiConvertKey(), userId + "_" + questionId);
	}

	/**
	 * 设置用户校验题目缓存.
	 * 
	 * @param userId
	 *            用户
	 * @param questionId
	 *            题目
	 */
	public void setCheckQuestion(long userId, long questionId) {
		qIdOpt.add(this.getQuestioAsciiCheckKey(), userId + "_" + questionId);
	}

	/**
	 * 清除所有用户转换、校验题目缓存.
	 */
	public void invalidAll() {
		getRedisTemplate().delete(this.getQuestioAsciiConvertKey());
		getRedisTemplate().delete(this.getQuestioAsciiCheckKey());
	}

	/**
	 * 删除转换题目.
	 * 
	 * @param questionId
	 */
	public void removeConvertQuestion(long userId, long questionId) {
		qIdOpt.remove(this.getQuestioAsciiConvertKey(), userId + "_" + questionId);
	}

	/**
	 * 删除校验题目.
	 * 
	 * @param questionId
	 */
	public void removeCheckQuestion(long userId, long questionId) {
		qIdOpt.remove(this.getQuestioAsciiCheckKey(), userId + "_" + questionId);
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		qIdOpt = getRedisTemplate().opsForSet();
	}
}
