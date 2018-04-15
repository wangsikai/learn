package com.lanking.cloud.job.doQuestionRank.DAO;

import java.util.List;

import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * DoQuestionSchoolRank 相关数据库操作接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月19日
 */
public interface DoQuestionSchoolRankDAO {

	/**
	 * 查询个人的排行信息
	 * 
	 * @param schoolId
	 * @param startDate
	 * @param endDate
	 * @param userId
	 * @return
	 */
	DoQuestionSchoolRank findStudentInSchoolRank(long schoolId, int startDate, int endDate, long userId);

	/**
	 * 保存数据
	 * 
	 * @param doQuestionSchoolRank
	 */
	DoQuestionSchoolRank save(DoQuestionSchoolRank doQuestionSchoolRank);

	/**
	 * 取指定时间范围内的所有学校列表
	 */
	List<Long> getSchoolIds(int startDate, int endDate, int start, int size);

	/**
	 * 查询指定时间的校级榜数据
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @param pageable
	 */
	CursorPage<Long, DoQuestionSchoolRank> getSchoolRankPraiseBySchoolId(int startDate, int endDate, Long schoolId,
			CursorPageable<Long> pageable);

	/**
	 * 保存数据
	 * 
	 * @param doQuestionSchoolRanks
	 */
	List<DoQuestionSchoolRank> saves(List<DoQuestionSchoolRank> doQuestionSchoolRanks);
}
