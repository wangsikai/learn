package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 针对商品的相关计数
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月10日
 */
@Component
public class ProductCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.PRODUCT;
	}

	@Override
	public Biz getOtherBiz() {
		return Biz.NULL;
	}

	/**
	 * 获得商品收藏数
	 * 
	 * @param productId
	 *            商品ID
	 * @return long
	 */
	public long getCollectionCount(long productId) {
		return this.getCount(productId, Count.COUNTER_1).longValue();
	}

	/**
	 * 批量获取收藏数
	 * 
	 * @since 2.1
	 * @param productIds
	 *            商品ID
	 * @return Map
	 */
	public Map<Long, Long> mgetCollectionCount(Collection<Long> productIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(productIds);
		for (Long productId : productIds) {
			Counter counter = counterMap.get(productId);
			if (counter == null) {
				collectionCounts.put(productId, 0L);
			} else {
				collectionCounts.put(productId, counter.getCount1());
			}
		}
		return collectionCounts;
	}

	/**
	 * 记录商品的收藏数
	 * 
	 * @param productId
	 * @param num
	 */
	public void incrCollectionCount(long productId, int num) {
		this.counter(productId, Count.COUNTER_1, num);
	}

	/**
	 * 记录商品的购买次数
	 * 
	 * @param productId
	 *            商品ID
	 */
	public void incrBuyProductCount(long productId, int num) {
		this.counter(productId, Count.COUNTER_2, num);
	}

	/**
	 * 获取商品的购买次数
	 * 
	 * @param productId
	 *            商品ID
	 */
	public long getBuyProductCount(long productId) {
		return this.getCount(productId, Count.COUNTER_2).longValue();
	}

	/**
	 * 获得参与评分的人数
	 * 
	 * @param lessonId
	 *            课时ID
	 * @return
	 */
	public long getGradePeopleNum(long productId) {
		return this.getCount(productId, Count.COUNTER_3).longValue();
	}

	/**
	 * 评分人数计数
	 * 
	 * @param productId
	 *            商品ID
	 * @param delta
	 *            变量
	 */
	public void incrGradePeopleNum(long productId, int delta) {
		this.counter(productId, Count.COUNTER_3, delta);
	}

	/**
	 * 无分组资源计数
	 * 
	 * @param productId
	 *            商品ID
	 * @param delta
	 *            数目
	 */
	public void incrNoCatalogResourceCount(long productId, int delta) {
		this.counter(productId, Count.COUNTER_4, delta);
	}

	/**
	 * 获取商品无分组资源数量
	 * 
	 * @param productId
	 *            商品ID
	 * @return 数目
	 */
	public long getNoCatalogResourceCount(long productId) {
		return this.getCount(productId, Count.COUNTER_4).longValue();
	}

	/**
	 * 商品中资源数量计数
	 * 
	 * @param productId
	 *            商品ID
	 * @param delta
	 *            数目
	 */
	public void incrResourceCount(long productId, int delta) {
		this.counter(productId, Count.COUNTER_5, delta);
	}

	/**
	 * 获取商品中的资源的数量
	 * 
	 * @param productId
	 *            商品ID
	 */
	public long getResourceCount(long productId) {
		return this.getCount(productId, Count.COUNTER_5).longValue();
	}

	/**
	 * 商品中资源数量
	 * 
	 * @param productIds
	 * @return
	 */
	public Map<Long, Long> mgetResourceCount(Collection<Long> productIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(productIds);
		for (Long productId : productIds) {
			Counter counter = counterMap.get(productId);
			if (counter == null) {
				collectionCounts.put(productId, 0L);
			} else {
				collectionCounts.put(productId, counter.getCount5());
			}
		}
		return collectionCounts;
	}

	/**
	 * 通过校验计数
	 * 
	 * @param productId
	 * @param num
	 */
	public void incrPassChecked(long productId, int num) {
		this.counter(productId, Count.COUNTER_6, num);
	}

	/**
	 * 未通过校验计数
	 * 
	 * @param productId
	 * @param num
	 */
	public void incrNoPassChecked(long productId, int num) {
		this.counter(productId, Count.COUNTER_7, num);
	}

	/**
	 * 获取商品资源总时长计数
	 * 
	 * @param productId
	 *            商品ID
	 * @param delta
	 *            数目
	 */
	public void incrTotalDurationCount(long productId, int delta) {
		this.counter(productId, Count.COUNTER_8, delta);
	}

	/**
	 * 获取商品资源总时长
	 * 
	 * @param productId
	 *            商品ID
	 * @return 数目
	 */
	public long getTotalDurationCount(long productId) {
		return this.getCount(productId, Count.COUNTER_8).longValue();
	}

	/**
	 * 批量获取商品资源总时长
	 * 
	 * @param productIds
	 * @return
	 */
	public Map<Long, Long> mgetTotalDurationCount(Collection<Long> productIds) {
		Map<Long, Long> collectionCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(productIds);
		for (Long productId : productIds) {
			Counter counter = counterMap.get(productId);
			if (counter == null) {
				collectionCounts.put(productId, 0L);
			} else {
				collectionCounts.put(productId, counter.getCount8());
			}
		}
		return collectionCounts;
	}

	/**
	 * 获得评分.
	 * 
	 * @param lessonId
	 *            课时ID
	 * @return
	 */
	public double getGrade(long productId) {
		return this.getCount(productId, Count.COUNTER_20).doubleValue();
	}

	/**
	 * 商品被评论计数 <br>
	 * 
	 * 
	 * @param productId
	 *            商品ID
	 * @param c
	 *            评分
	 */
	public void resetGrade(long productId, double c) {
		Counter counter = this.getCounter(productId);
		double avg = (counter == null || counter.getCount20() == null) ? 0.0 : counter.getCount20().doubleValue(); // 当前平均分
		long num = counter == null ? 0 : counter.getCount3(); // 当前评分人数
		if (counter == null) {
			this.counter(productId, Count.COUNTER_20, c);
		} else {
			this.counterReset(productId, Count.COUNTER_20, (avg * num + c) / (num + 1));
		}
	}

}
