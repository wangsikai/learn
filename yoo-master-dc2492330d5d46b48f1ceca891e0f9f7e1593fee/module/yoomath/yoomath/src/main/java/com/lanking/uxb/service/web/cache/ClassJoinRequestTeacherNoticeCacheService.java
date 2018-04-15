package com.lanking.uxb.service.web.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.SerializerType;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;
import com.taobao.api.internal.util.StringUtils;

/**
 * 老师查看学生加入班级申请列表，时间缓存
 * 
 * @author wangsenhao
 *
 */
@Service
public class ClassJoinRequestTeacherNoticeCacheService extends AbstractCacheService {
	private static final String NEED_NOTICE_CLASSAPPLY_ID_KEY = "c";
	private ValueOperations<String, String> opsVal;

	private String getKey(long teacherId) {
		return assemblyKey(NEED_NOTICE_CLASSAPPLY_ID_KEY, teacherId);
	}

	/**
	 * 添加老师查看申请列表最新时间到缓存
	 *
	 * @param teacherId
	 *            老师id
	 * @param timestamp
	 *            更新时间戳
	 */
	public void update(long teacherId, long timestamp) {
		if (getTimestamp(teacherId) < 0) {
			opsVal.set(getKey(teacherId), String.valueOf(timestamp));
		}
	}

	/**
	 * 判断是否需要提醒
	 *
	 * @param teacherId
	 *            老师id
	 * @param createAt
	 *            最新一条申请的时间
	 */
	public boolean has(long teacherId, long updateAt) {
		String timestamp = opsVal.get(getKey(teacherId));
		return !StringUtils.isEmpty(timestamp) && Long.parseLong(timestamp) < updateAt;

	}

	/**
	 * 获得时间戳
	 *
	 * @param teacherId
	 *            老师id
	 * @return 时间戳
	 */
	public long getTimestamp(long teacherId) {
		String timestamp = opsVal.get(getKey(teacherId));
		if (StringUtils.isEmpty(timestamp)) {
			return -1L;
		}
		return Long.valueOf(timestamp);
	}

	/**
	 * 清空未读数据
	 *
	 * @param teacherId
	 *            老师id
	 */
	public void clear(long teacherId) {
		long now = System.currentTimeMillis();
		opsVal.set(getKey(teacherId), String.valueOf(now));
	}

	@Override
	public String getNs() {
		return "cjrt-n";
	}

	@Override
	public String getNsCn() {
		return "老师查看加入班级申请提醒缓存";
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
