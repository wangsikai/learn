package com.lanking.uxb.service.payment.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

/**
 * 第三方支付回调订单相关缓存控制.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月11日
 */
@Service
public class CallbackOrderCache extends AbstractCacheService {
	private ValueOperations<String, Integer> opt;

	@Override
	public String getNs() {
		return "pay";
	}

	@Override
	public String getNsCn() {
		return "支付平台回调控制";
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
	public String getKey(OrderBusinessSource source, Long orderID) {
		return assemblyKey(source, orderID);
	}

	/**
	 * 设置处理中的订单.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 */
	public void setPayOrderProcessing(OrderBusinessSource source, Long orderID) {
		opt.set(getKey(source, orderID), 1, 60, TimeUnit.MINUTES);
	}

	/**
	 * 获取处理中的订单.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	public Integer getPayOrderProcessing(OrderBusinessSource source, Long orderID) {
		return opt.get(getKey(source, orderID));
	}

	/**
	 * 取消处理中的订单.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 */
	@SuppressWarnings("unchecked")
	public void invalidPayOrderProcessing(OrderBusinessSource source, Long orderID) {
		getRedisTemplate().delete(getKey(source, orderID));
	}

	/**
	 * 判断订单是否处理中.
	 * 
	 * @param source
	 *            来源
	 * @param orderID
	 *            订单ID
	 * @return
	 */
	public boolean hasProcessing(OrderBusinessSource source, Long orderID) {
		Integer orderProcceingFlag = this.getPayOrderProcessing(source, orderID);
		return orderProcceingFlag == null ? false : true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		opt = getRedisTemplate().opsForValue();
	}
}
