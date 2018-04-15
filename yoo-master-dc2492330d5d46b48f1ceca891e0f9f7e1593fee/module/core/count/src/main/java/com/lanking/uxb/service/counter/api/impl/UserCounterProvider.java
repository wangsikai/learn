package com.lanking.uxb.service.counter.api.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.domain.type.Biz;

/**
 * 用户计数统计（无detail类型）
 * 
 * @version 2014年12月11日
 */
@Component
public class UserCounterProvider extends AbstractBizCounterProvider {

	@Override
	public Biz getBiz() {
		return Biz.USER;
	}

	@Override
	public Biz getOtherBiz() {
		return null;
	}

	public void incrPost(long userId, int c) {
		this.counter(userId, Count.COUNTER_1, c);
	}

	public void incrPostComment(long userId, int c) {
		this.counter(userId, Count.COUNTER_2, c);
	}

	public void incrCreateGroup(long userId, int c) {
		this.counter(userId, Count.COUNTER_3, c);
	}

	public void incrJoinGroup(long userId, int c) {
		this.counter(userId, Count.COUNTER_4, c);
	}

	public void incrFollow(long userId, int c) {
		this.counter(userId, Count.COUNTER_5, c);
	}

	public void incrFan(long userId, int c) {
		this.counter(userId, Count.COUNTER_6, c);
	}

	public void incrSelfResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_7, c);
	}

	public void incrBuyResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_8, c);
	}

	public void incrCourse(long userId, int c) {
		this.counter(userId, Count.COUNTER_9, c);
	}

	/**
	 * 教案计数.
	 * 
	 * @param userId
	 * @param c
	 */
	public void incrPlan(long userId, int c) {
		this.counter(userId, Count.COUNTER_10, c);
	}

	public void incrFavoritePlan(long userId, int c) {
		this.counter(userId, Count.COUNTER_12, c);
	}

	@Deprecated
	private void incrExercise(long userId, int c) {
		this.counter(userId, Count.COUNTER_11, c);
	}

	@Deprecated
	private void incrFavoriteExercise(long userId, int c) {
		this.counter(userId, Count.COUNTER_13, c);
	}

	/**
	 * 分享计数
	 * 
	 * @param userId
	 * @param c
	 */
	public void incrShareResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_14, c);
	}

	/**
	 * 用户资源被收藏计数
	 *
	 * @param userId
	 * @param c
	 */
	public void incrIsCollectedResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_17, c);
	}

	/**
	 * 获得用户被收藏的资源计数
	 *
	 * @param userId
	 * @return
	 */
	public long getIsCollectedResource(long userId) {
		return this.getCount(userId, Count.COUNTER_17).longValue();
	}

	public long getShareResource(long userId) {
		return this.getCount(userId, Count.COUNTER_14).longValue();
	}

	public long getPost(long userId) {
		return this.getCount(userId, Count.COUNTER_1).longValue();
	}

	public long getPostComment(long userId) {
		return this.getCount(userId, Count.COUNTER_2).longValue();
	}

	/**
	 * 群组计数.
	 * 
	 * @param userId
	 * @param c
	 */
	public long getCreateGroup(long userId) {
		return this.getCount(userId, Count.COUNTER_3).longValue();
	}

	public long getJoinGroup(long userId) {
		return this.getCount(userId, Count.COUNTER_4).longValue();
	}

	public long getFollow(long userId) {
		return this.getCount(userId, Count.COUNTER_5).longValue();
	}

	public long getFan(long userId) {
		return this.getCount(userId, Count.COUNTER_6).longValue();
	}

	public long getSelfResource(long userId) {
		return this.getCount(userId, Count.COUNTER_7).longValue();
	}

	public long getBuyResource(long userId) {
		return this.getCount(userId, Count.COUNTER_8).longValue();
	}

	public long getCourse(long userId) {
		return this.getCount(userId, Count.COUNTER_9).longValue();
	}

	/**
	 * 获得教案计数.
	 * 
	 * @param userId
	 * @return
	 */
	public long getPlan(long userId) {
		return this.getCount(userId, Count.COUNTER_10).longValue();
	}

	public long getFavoritePlan(long userId) {
		return this.getCount(userId, Count.COUNTER_12).longValue();
	}

	public long getExercise(long userId) {
		return this.getCount(userId, Count.COUNTER_11).longValue();
	}

	public long getFavoriteExercise(long userId) {
		return this.getCount(userId, Count.COUNTER_13).longValue();
	}

	/**
	 * 贡献计数
	 * 
	 * @param userId
	 * @param c
	 */
	public void incrContributeResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_15, c);
	}

	public long getContributeResource(long userId) {
		return this.getCount(userId, Count.COUNTER_15).longValue();
	}

	public Map<Long, Long> mgetCountibuteResource(Set<Long> userIds) {
		Map<Long, Long> memberCounts = Maps.newHashMap();
		Map<Long, Counter> counterMap = this.getCounters(userIds);
		for (Long userId : userIds) {
			Counter counter = counterMap.get(userId);
			memberCounts.put(userId, counter == null ? 0 : counter.getCount15());
		}
		return memberCounts;
	}

	/**
	 * 推荐计数
	 * 
	 * @param userId
	 * @param c
	 */
	public void incrRecommendResource(long userId, int c) {
		this.counter(userId, Count.COUNTER_16, c);
	}

	public long getRecommedResource(long userId) {
		return this.getCount(userId, Count.COUNTER_16).longValue();
	}

	public void incrGotPoint(long userId, int c) {
		this.counter(userId, Count.COUNTER_19, c);
	}

	public long getGotPoint(long userId) {
		return this.getCount(userId, Count.COUNTER_19).longValue();
	}

	/**
	 * 加入圈子计数
	 * 
	 * @param userId
	 * @param c
	 */
	public void incrJoinedCircleNum(long userId, int c) {
		this.counter(userId, Count.COUNTER_18, c);
	}

	public long getJoinedCircleNum(long userId) {
		return this.getCount(userId, Count.COUNTER_18).longValue();
	}
}
