package com.lanking.uxb.service.ranking.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;

/**
 * 学校维度答题排名接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月11日
 */
public interface DoQuestionSchoolRankService {

	DoQuestionSchoolRank get(long id);

	Map<Long, DoQuestionSchoolRank> mget(Collection<Long> ids);

	/**
	 * 查询排行榜
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param topN
	 * @return
	 */
	List<DoQuestionSchoolRank> listDoQuestionSchoolRankTopN(long schoolId, int startDate, int endDate, int topN);

	/**
	 * 查询用户排行
	 * 
	 * @param classId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	DoQuestionSchoolRank findStudentInSchoolRank(long schoolId, int startDate, int endDate, long userId);

	/**
	 * 更新点赞信息
	 * 
	 * @param rankId
	 * @param userId
	 * @return
	 */
	void updateSchoolPraiseCount(long rankId, long userId);

	/**
	 * 取消点赞
	 * 
	 * @param rankId
	 * @param userId
	 * @param praiseId
	 * @return
	 */
	void cancelSchoolPraise(long rankId, long userId, long praiseId);
}
