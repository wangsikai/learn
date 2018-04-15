package com.lanking.uxb.service.payment.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 微信订单二维码缓存.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年1月23日
 */
@Service
public class WXQRCodeImageCache extends AbstractCacheService {
	private ValueOperations<String, String> opt;

	@Override
	public String getNs() {
		return "wx-qr";
	}

	@Override
	public String getNsCn() {
		return "微信订单二维码";
	}

	/**
	 * 获得缓存KEY.
	 * 
	 * @param source
	 *            订单来源
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	public String getKey(OrderBusinessSource source, long orderID) {
		return assemblyKey(source, orderID);
	}

	/**
	 * 设置微信二维码缓存.
	 * 
	 * @param source
	 *            订单来源
	 * @param orderID
	 *            订单ID
	 * @param base64
	 *            二维码信息
	 * 
	 * @param timeUnit
	 *            超时时间单位
	 * @param timeout
	 *            超时时间
	 */
	public void setWXQRCodeImageCache(OrderBusinessSource source, Long orderID, String base64, TimeUnit timeUnit,
			long timeout) {
		opt.set(getKey(source, orderID), base64, timeout, timeUnit);
	}

	/**
	 * 获取微信二维码缓存.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	public String getWXQRCodeImageCache(OrderBusinessSource source, Long orderID) {
		return opt.get(getKey(source, orderID));
	}

	/**
	 * 删除微信二维码缓存.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 */
	@SuppressWarnings("unchecked")
	public void invalidWXQRCodeImageCache(OrderBusinessSource source, Long orderID) {
		getRedisTemplate().delete(getKey(source, orderID));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		opt = getRedisTemplate().opsForValue();
	}
}
