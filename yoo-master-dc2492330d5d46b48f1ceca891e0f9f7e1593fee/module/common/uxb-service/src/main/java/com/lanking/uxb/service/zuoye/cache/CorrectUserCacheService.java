package com.lanking.uxb.service.zuoye.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 短信通知提醒
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月30日
 */
@Service
public final class CorrectUserCacheService extends AbstractCacheService {

	private ValueOperations<String, String> valOpt;

	private String getSmsTagKey() {
		return "time";
	}

	public String getSmsTag() {
		return valOpt.get(getSmsTagKey());
	}

	public void setSmsTag() {
		valOpt.set(getSmsTagKey(), String.valueOf(System.currentTimeMillis()));
	}

	private String getSmsBeforeAutoCommitTagKey(long homeworkId) {
		return assemblyKey("bac", homeworkId);
	}

	public String getSmsBeforeAutoCommitTag(long homeworkId) {
		return valOpt.get(getSmsBeforeAutoCommitTagKey(homeworkId));
	}

	public void setSmsBeforeAutoCommitTag(long homeworkId) {
		valOpt.set(getSmsBeforeAutoCommitTagKey(homeworkId), String.valueOf(System.currentTimeMillis()));
	}

	@Override
	public String getNs() {
		return "cu";
	}

	@Override
	public String getNsCn() {
		return "作业短信通知";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		valOpt = getRedisTemplate().opsForValue();
	}

}
