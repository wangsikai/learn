package com.lanking.uxb.service.counter.api.impl;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.CounterDetail;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.counter.api.BizCounterProvider;
import com.lanking.uxb.service.counter.api.CounterService;

/**
 * 
 * <pre>
 * COUNTER计数说明:
 * 
 * 				USR： 
 * 					1：发帖数,2：帖子评论（回复）数 ,3: 用户创建的群组数,4：用户加入的群组数,5：关注数量,6:粉丝数量,7:自建资源,8:已购资源,9:课程数,10:教案数,11:习题集数量,12:收藏教案数,13:收藏习题集数量,14:用户分享资源数,15:用户贡献数，16：用户推荐数,17:用户资源被收录数,18:加入圈子计数,19:已赚取积分 
 * 				POST： 
 * 					1：被转发数,2：被赞数,3：被评论数
 * 				COMMENT：
 * 					1：被赞数,2：被回复数
 * 				GROUP:
 * 					1:群成员数 2:群帖子数
 * 				ALBUM:
 * 					1:相片个数
 * 				ALBUM_PHOTO
 * 					1：相册照片赞数，2：相册照片评论数，3：相册照片分享数
 * 				CLASS:
 * 					1:相册数
 * 				HOMEWORK：
 * 					1:习题数量，2:评价为容易的人数，3:评价为普通的人数，4:评价为困难的人数
 * 				EXERCISE：
 * 					1:习题数量，2:被收藏数
 * 				PLAN:
 * 					1:课时数量，2:被收藏数
 * 				LESSON：
 * 					1：评生动性人数，2：评互动性人数，3：评接受度人数，4：元素数量，20：生动性平均分，21：互动性平均分，22：接受度平均分
 * 				PRODUCT:
 * 					1:被收藏数  2:购买该商品的人数  3：评论该商品的人数  ，4：无分组资源数量，5：资源数量，6:通过检验，7：未通过校验，8：资源总时长，20：评论平均分
 * 				PRODUCT_CATALOG:
 * 					1:资源总数量  2:通过检验  3：未通过校验
 * 				MARKET_RESOURCE:
 * 					1:书院资源总数  2:媒体站资源总数 3:优课堂  4:应用
 * 				RESOURCE_POOL:
 * 					1:习题总数  2:流媒体总数  3:优课总数  4:应用总数 5:通过检验的习题  6：未通过校验的习题 7:通过检验的流媒体  8：未通过校验的流媒体
 * 				QUESTIONS（书本）:
 * 					1:被收藏数
 * 				STREAMS（专辑）:
 * 					1:被收藏数
 * 				QUESTION:
 * 					1：被使用次数
 * 				STREAM:
 * 					1：被使用次数
 * 				USER_RESOURCE:
 * 					1:加入办公桌次数
 * 				VOTE:
 * 					1：被转发数,2：被赞数,3：被评论数,4:投票人数
 * 				VOTE_OPTION:
 * 					1:投票人数
 * 				ANNOUNCE:
 * 					1:评论数
 * 				COURSE:
 * 					1:课程人数
 * 				EXAMS:
 * 					1:试卷总数，2：已发布试卷数，3：待发布试卷数，4：带校验，5：录入中
 * 				BOOKS:
 * 					1:书本总量，2：已发布书本数，3：待发布书本数，4：录入中
 * 				BOOK:
 * 					1：书本内题目总数，2：书本内未分组题目总数
 * 				BOOK_CATALOG：
 * 					1：书本目录下的资源数量
 * 				VENDOR_USER
 * 					1: 今日一校校验次数，2：今日二校校验次数
 * </pre>
 * 
 * <pre>
 * COUNTER-DETAIL说明:
 * 
 * 				POST-USER:
 * 					1：用户转发数，2：用户赞数，3：用户评论次数
 * 				COMMENT-USER：
 * 					1：用户赞数
 * 				ALBUM_PHOTO-USER：
 * 					1：用户赞数，用户评论次数，3：用户分享次数
 * 				LESSON-USER:
 * 					20：用户生动性评分，21：用户互动性评分，23：用户接受度评分
 * 				PRODCUT-USER:
 * 					1:收藏数 ，2：购买数 ，20：评论分数
 * 				QUESTIONS-USER:
 * 					1:收藏数
 * 				STREAMS-USER:
 * 					1：收藏数
 * 				HOMEWORK-USER:
 * 					1：评价程度（0：未评分，1：容易、2：普通、3：困难）
 * 				EXERCISE-USER:
 * 					1:收藏数
 * 				PLAN-USER:
 * 					1:收藏数
 * 				VOTE-UESR:
 * 					1：用户转发数，2：用户赞数，3：用户评论次数，4:用户投票次数
 * 				ANNOUNCE-UESR:
 * 					1：用户评论次数
 * 
 * </pre>
 * 
 * 
 * @version 2014年12月11日
 */
abstract class AbstractBizCounterProvider implements BizCounterProvider {

	@Autowired
	private CounterService counterService;

	@Override
	public Counter getCounter(long bizId) {
		return counterService.getCounter(getBiz(), bizId);
	}

	@Override
	public Map<Long, Counter> getCounters(Collection<Long> bizIds) {
		return counterService.getCounters(getBiz(), bizIds);
	}

	@Override
	public CounterDetail getCounterDetail(long bizId, long otherBizId) {
		return counterService.getCounterDetail(getBiz(), bizId, getOtherBiz(), otherBizId);
	}

	@Override
	public Map<Long, CounterDetail> getCounterDetailsByOtherBizId(long bizId, Collection<Long> otherBizIds) {
		return counterService.getCounterDetailsByOtherBizId(getBiz(), bizId, getOtherBiz(), otherBizIds);
	}

	@Override
	public Map<Long, CounterDetail> getCounterDetailsByBizId(Collection<Long> bizIds, long otherBizId) {
		return counterService.getCounterDetailsByBizId(getBiz(), bizIds, getOtherBiz(), otherBizId);
	}

	@Override
	public Page<CounterDetail> queryCounterDetailsByOtherBizId(long otherBizId, Count count, Pageable pageable) {
		return counterService.queryCounterDetailsByOtherBizId(getBiz(), getOtherBiz(), otherBizId, count, pageable);
	}

	@Override
	public Number getCount(long bizId, Count count) {
		Number num = counterService.getCount(getBiz(), bizId, count);
		return num == null ? 0 : num;
	}

	@Override
	public Map<Count, Long> getCounts(long bizId, Set<Count> count) {
		return counterService.getCounts(getBiz(), bizId, count);
	}

	@Override
	public void counter(long bizId, Count count, Number c) {
		counterService.counter(getBiz(), bizId, count, c);
	}

	@Override
	public void counters(Count count, Map<Long, ? extends Number> cs) {
		counterService.counters(getBiz(), count, cs);
	}

	@Override
	public void counterReset(long bizId, Count count, Number c) {
		counterService.counterReset(getBiz(), bizId, count, c);
	}

	@Override
	public void counterDetail(long bizId, long otherBizId, Count count, Number c) {
		counterService.counterDetail(getBiz(), bizId, getOtherBiz(), otherBizId, count, c);
	}

}
