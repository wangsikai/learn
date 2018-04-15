package com.lanking.uxb.service.ranking.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRankPraise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 班级维度答题点赞接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月11日
 */
public interface DoQuestionClassRankPraiseService {

	DoQuestionClassRankPraise get(long id);

	Map<Long, DoQuestionClassRankPraise> mget(Collection<Long> ids);

	/**
	 * 取指定用户的点赞其他人信息
	 * 
	 * @param userId
	 * @return map key:rankId value:DoQuestionClassRankPraise
	 */
	Map<Long, DoQuestionClassRankPraise> getUserPraise(long userId);

	/**
	 * 根据rankId和userId取count
	 * 
	 * @param userId
	 * @return count
	 */
	long countByRankId(long rankId, long userId);

	/**
	 * 根据rankId和userId取RankPraise
	 * 
	 * @param userId
	 * @return count
	 */
	DoQuestionClassRankPraise getRankPraiseByRankId(long rankId, long userId);

	/**
	 * 游标查询根据rankId取所有点赞的数据
	 */
	CursorPage<Long, DoQuestionClassRankPraise> getRankPraiseByRankId(long rankId, CursorPageable<Long> pageable);
}
