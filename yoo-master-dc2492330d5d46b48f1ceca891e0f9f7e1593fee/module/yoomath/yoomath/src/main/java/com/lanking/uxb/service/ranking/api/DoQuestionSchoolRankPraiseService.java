package com.lanking.uxb.service.ranking.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRankPraise;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 学校维度答题点赞接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月11日
 */
public interface DoQuestionSchoolRankPraiseService {

	DoQuestionSchoolRankPraise get(long id);

	Map<Long, DoQuestionSchoolRankPraise> mget(Collection<Long> ids);

	/**
	 * 取指定用户的点赞
	 * 
	 * @param userId
	 * @return msp key:rankId value:DoQuestionSchoolRankPraise
	 */
	Map<Long, DoQuestionSchoolRankPraise> getUserPraise(long userId);

	/**
	 * 根据rankId和userId取count
	 * 
	 * @param userId
	 * @return count
	 */
	long countByRankId(long rankId, long userId);

	/**
	 * 根据rankId和userId取count
	 * 
	 * @param rankId
	 * @param userId
	 * @return
	 */
	DoQuestionSchoolRankPraise getRankPraiseByRankId(long rankId, long userId);

	/**
	 * 游标查询根据rankId取所有点赞的数据
	 */
	CursorPage<Long, DoQuestionSchoolRankPraise> getRankPraiseByRankId(long rankId, CursorPageable<Long> pageable);
}
