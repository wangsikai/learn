package com.lanking.uxb.rescon.question.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * v3知识点相关缓存.
 * 
 * @author wlche
 * @since 2017-11-07
 */
@Service
public class ResconQuestionKnowledgeV3CacheService extends AbstractCacheService {
	private ValueOperations<String, Long> longOpt;
	private SetOperations<String, String> setOpt;

	@Override
	public String getNs() {
		return "rq-k3";
	}

	@Override
	public String getNsCn() {
		return "资源平台-v3知识点相关缓存";
	}

	/**
	 * 已处理题目个数.
	 */
	private static final String ALREADY_DO_KEY = "a";

	/**
	 * 正在处理中的题目（排除使用）.
	 */
	private static final String DOING_KEY = "b";

	/**
	 * 个人锁定的题目.
	 */
	private static final String SELF_KEY = "c";

	/**
	 * 获取KEY.
	 * 
	 * @param userId
	 *            用户
	 * @param date
	 *            日期
	 * @return
	 */
	private String getUserDoKey(long userId, Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dt = date == null ? "all" : format.format(date);
		return assemblyKey(ALREADY_DO_KEY, dt, userId);
	}

	/**
	 * 获取KEY.
	 * 
	 * @param vendorId
	 *            供应商
	 * @param date
	 *            日期
	 * @return
	 */
	private String getAllDoKey(long vendorId, Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dt = date == null ? "all" : format.format(date);
		return assemblyKey(ALREADY_DO_KEY, vendorId, dt);
	}

	/**
	 * 获取所有正在处理中的题目Key.
	 * 
	 * @return
	 */
	private String getDoingKey() {
		return assemblyKey(DOING_KEY);
	}

	/**
	 * 获取个人锁定的题目Key.
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	private String getSelfDoingKey(long userId) {
		return assemblyKey(SELF_KEY, userId);
	}

	/**
	 * 添加.
	 * 
	 * @param userId
	 */
	public void increment(long userId, long vendorId) {
		Date date = new Date();
		longOpt.increment(this.getUserDoKey(userId, date), 1); // 个人今日
		longOpt.increment(this.getUserDoKey(userId, null), 1); // 个人总数
		longOpt.increment(this.getAllDoKey(vendorId, date), 1); // 全部今日
	}

	/**
	 * 获取个人添加个数.
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public long getSelfDo(long userId, Date date) {
		return longOpt.increment(this.getUserDoKey(userId, date), 0);
	}

	/**
	 * 获取全部添加个数.
	 * 
	 * @param date
	 * @return
	 */
	public long getAllDo(long vendorId, Date date) {
		return longOpt.increment(this.getAllDoKey(vendorId, date), 0);
	}

	/**
	 * 获取用户正在处理的习题.
	 * 
	 * @param userId
	 * @return
	 */
	public Long getUserDoingCache(long userId) {
		Set<String> values = setOpt.members(this.getDoingKey());
		long now = System.currentTimeMillis();
		if (CollectionUtils.isNotEmpty(values)) {
			for (String value : values) {
				long uid = Long.parseLong(value.split("_")[1]);
				if (uid == userId) {
					long questionId = Long.parseLong(value.split("_")[0]);
					long timestap = Long.parseLong(value.split("_")[2]);
					if ((now - timestap) / 1000 > 3600) {
						setOpt.remove(this.getDoingKey(), value);
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
	 * 获取所有正在处理中的习题.
	 * 
	 * @return
	 */
	public Set<Long> getDoingCache() {
		Set<String> values = setOpt.members(this.getDoingKey());
		Set<Long> questionIds = new HashSet<Long>();
		long now = System.currentTimeMillis();
		if (CollectionUtils.isNotEmpty(values)) {
			for (String value : values) {
				long questionId = Long.parseLong(value.split("_")[0]);
				long timestap = Long.parseLong(value.split("_")[2]);
				if ((now - timestap) / 1000 > 3600) {
					setOpt.remove(this.getDoingKey(), value);
				} else {
					questionIds.add(questionId);
				}
			}
		}

		return questionIds;
	}

	/**
	 * 设置正在处理中的习题.
	 * 
	 * @param questionId
	 */
	public void setDoingQuestion(long userId, long questionId) {
		setOpt.add(this.getDoingKey(), questionId + "_" + userId + "_" + System.currentTimeMillis());
	}

	/**
	 * 删除正在处理中的习题.
	 * 
	 * @param questionId
	 */
	public void invalidDoingQuestion(long questionId) {
		Set<String> values = setOpt.members(this.getDoingKey());
		if (CollectionUtils.isNotEmpty(values)) {
			for (String value : values) {
				long qid = Long.parseLong(value.split("_")[0]);
				if (qid == questionId) {
					setOpt.remove(this.getDoingKey(), value);
					break;
				}
			}
		}
	}

	/**
	 * 删除正在处理中的习题（利用用户ID）.
	 * 
	 * @param userId
	 */
	public void invalidDoingQuestionByUser(long userId) {
		Set<String> values = setOpt.members(this.getDoingKey());
		if (CollectionUtils.isNotEmpty(values)) {
			long now = System.currentTimeMillis();
			for (String value : values) {
				long uid = Long.parseLong(value.split("_")[1]);
				long timestap = Long.parseLong(value.split("_")[2]);
				if (uid == userId && (now - timestap) / 1000 > 30) {
					// 30秒后再删除，防止索引更新慢
					setOpt.remove(this.getDoingKey(), value);
				}
			}
		}
	}

	/**
	 * 设置个人锁定题目.
	 * 
	 * @param userId
	 *            用户
	 * @param questionId
	 *            习题ID
	 */
	public void setSelfDoingQuestion(long userId, long questionId) {
		longOpt.set(getSelfDoingKey(userId), questionId, 30, TimeUnit.MINUTES);
	}

	/**
	 * 获取个人锁定题目.
	 * 
	 * @param userId
	 *            用户
	 * @return
	 */
	public Long getSelfDoingQuestion(long userId) {
		return longOpt.get(getSelfDoingKey(userId));
	}

	/**
	 * 删除个人锁定题目.
	 * 
	 * @param userId
	 *            用户
	 */
	@SuppressWarnings("unchecked")
	public void invalidSelfDoingQuestion(long userId) {
		getRedisTemplate().delete(getSelfDoingKey(userId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		longOpt = getRedisTemplate().opsForValue();
		setOpt = getRedisTemplate().opsForSet();
	}
}
