package com.lanking.uxb.service.ranking.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;

/**
 * 班级维度答题排名接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月8日
 */
public interface DoQuestionClassRankService {

	DoQuestionClassRank get(long id);

	Map<Long, DoQuestionClassRank> mget(Collection<Long> ids);

	/**
	 * 查询排行榜
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @return
	 */
	List<DoQuestionClassRank> listDoQuestionClassStatTopN(long classId, int startDate, int endDate, int topN);
	
	/**
	 * 查询个人的排行信息
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	DoQuestionClassRank findStudentInClassRank(long classId, int startDate, int endDate, long userId);

	/**
	 * 更新点赞信息
	 * 
	 * @param rankId
	 * @param userId
	 * @return
	 */
	void updateClassPraiseCount(long rankId, long userId);

	/**
	 * 取消点赞
	 * 
	 * @param rankId
	 * @param userId
	 * @param praiseId
	 * @return
	 */
	void cancelPraise(long rankId, long userId, long praiseId);
}
