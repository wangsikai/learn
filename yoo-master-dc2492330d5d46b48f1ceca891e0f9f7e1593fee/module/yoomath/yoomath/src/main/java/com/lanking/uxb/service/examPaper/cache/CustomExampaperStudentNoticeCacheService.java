package com.lanking.uxb.service.examPaper.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.taobao.api.internal.util.StringUtils;

/**
 * 学生组卷开卷后 未读信息 缓存服务
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
public class CustomExampaperStudentNoticeCacheService extends AbstractCacheService {
	// 新开卷的记录列表
	private static final String NEED_NOTICE_PAPER_ID_KEY = "p";
	private ValueOperations<String, String> opsVal;

	private String getKey(long studentId) {
		return assemblyKey(NEED_NOTICE_PAPER_ID_KEY, studentId);
	}

	/**
	 * 添加开卷信息至缓存中
	 *
	 * @param studentId
	 *            学生id
	 * @param timestamp
	 *            更新时间戳
	 */
	public void update(long studentId, long timestamp) {
		if (getTimestamp(studentId) < 0) {
			opsVal.set(getKey(studentId), String.valueOf(timestamp));
		}
	}

	/**
	 * 判断是否需要提醒
	 *
	 * @param studentId
	 *            学生id
	 * @param createAt
	 *            最新一条组卷记录时间
	 */
	public boolean has(long studentId, long createAt) {
		String timestamp = opsVal.get(getKey(studentId));
		return !StringUtils.isEmpty(timestamp) && Long.parseLong(timestamp) < createAt;

	}

	/**
	 * 获得时间戳
	 *
	 * @param studentId
	 *            学生id
	 * @return 时间戳
	 */
	public long getTimestamp(long studentId) {
		String timestamp = opsVal.get(getKey(studentId));
		if (StringUtils.isEmpty(timestamp)) {
			return -1L;
		}

		return Long.valueOf(timestamp);
	}

	/**
	 * 清空未读数据
	 *
	 * @param studentId
	 *            学生id
	 */
	public void clear(long studentId) {
		long now = System.currentTimeMillis();
		opsVal.set(getKey(studentId), String.valueOf(now));
	}

	@Override
	public String getNs() {
		return "ceps-n";
	}

	@Override
	public String getNsCn() {
		return "学生组卷提醒缓存";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		this.opsVal = getRedisTemplate().opsForValue();
	}

	@Override
	public SerializerType getSerializerType() {
		return SerializerType.STRING;
	}
}
