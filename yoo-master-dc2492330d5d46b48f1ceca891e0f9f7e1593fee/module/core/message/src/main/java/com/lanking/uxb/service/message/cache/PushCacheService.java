package com.lanking.uxb.service.message.cache;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 推送相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月30日
 */
@Service
public class PushCacheService extends AbstractCacheService {

	private ValueOperations<String, Long> pushUnReadOpt;

	@Override
	public String getNs() {
		return "push";
	}

	private String getUnReadPushCountKey(Product product, String token) {
		return assemblyKey(product.name(), token);
	}

	public long incrUnReadPushCount(Product product, String token) {
		return pushUnReadOpt.increment(getUnReadPushCountKey(product, token), 1L);
	}

	public void clearUnReadPushCount(Product product, String token) {
		Long unReadCount = pushUnReadOpt.increment(getUnReadPushCountKey(product, token), 0);
		if (unReadCount != 0) {
			pushUnReadOpt.increment(getUnReadPushCountKey(product, token), -unReadCount);
		}
	}

	@Override
	public String getNsCn() {
		return "推送";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		pushUnReadOpt = getRedisTemplate().opsForValue();
	}

}
